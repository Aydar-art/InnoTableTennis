<script lang="ts">
	import * as db from '$lib/client/requests';
	import type { Match, Player, Tournament } from '$lib/types/types';
	import { Play, Dot } from 'lucide-svelte';
	import { convertDateToStringDash } from '$lib/utils';
	import { alertPopup } from '$lib/client/popup/popup.handler';
	import { isLeader } from '$lib/client/stores/stores';
	import { alertInputPopup } from '$lib/client/popup/popup.handler.input';
	import { TSMap } from 'typescript-map';
	import { createEventDispatcher, onMount } from 'svelte';
	import type { Final, SingleEliminationBracket } from '$lib/types/types.tournaments';

	const dispatch = createEventDispatcher();

	let emptyPlayer = {
		id: 0,
		name: '',
		numberOfGames: 0,
		numberOfLosses: 0,
		numberOfWins: 0,
		rating: 0,
		telegramAlias: '',
		winRate: 0,
	} as Player;

	const isPowerOfTwo = (x: number) => {
		while (x > 1) {
			if (x % 2) return false;
			x /= 2;
		}
		return true;
	};

	// function to sort players for the first round
	const sortPlayers = (players: Player[]) => {
		let firstLayer: number[] = [1, -1];
		let id = 2,
			len = players.length;
		while (id <= len) {
			if (isPowerOfTwo(id - 1) && id > 2) {
				let tmp: number[] = [];
				for (let i = 0; i < firstLayer.length; i++) {
					tmp.push(firstLayer[i]);
					tmp.push(-1);
				}
				firstLayer = [...tmp];
			}
			let idx = 0,
				mxValue = -1;
			for (let i = 0; i < firstLayer.length; i += 2) {
				if (firstLayer[i + 1] == -1 && firstLayer[i] > mxValue) {
					mxValue = firstLayer[i];
					idx = i + 1;
				}
			}
			firstLayer[idx] = id;
			id++;
		}
		const ret: Player[] = [];
		for (let i = 0; i < firstLayer.length; i++) {
			if (firstLayer[i] === -1) {
				ret.push(emptyPlayer);
			} else {
				ret.push(players[firstLayer[i] - 1]);
			}
		}
		return ret;
	};

	// function to create raw matches
	function createMatch(firstPlayer: Player, secondPlayer: Player): Match {
		let localDateString = convertDateToStringDash(new Date());
		let newMatch = {
			firstPlayerScore: 0,
			secondPlayerScore: 0,
			firstPlayerName: firstPlayer.name,
			secondPlayerName: secondPlayer.name,
			localDateString: localDateString,
			id: 0,
			firstPlayerRatingDelta: 0,
			secondPlayerRatingDelta: 0,
			firstPlayerRatingBefore: 0,
			secondPlayerRatingBefore: 0,
			tournamentTitle: tournament.title,
		};
		return newMatch;
	}

	// fucntion to add finished matches to database
	async function addMatchToDB(matchIdx: number) {
		let matchToAdd = data.allMatches[matchIdx];
		let localDateString = convertDateToStringDash(new Date());
		let createdMatch = await db.createMatch(
			matchToAdd.firstPlayerName,
			matchToAdd.secondPlayerName,
			matchToAdd.firstPlayerScore,
			matchToAdd.secondPlayerScore,
			matchToAdd.tournamentTitle,
			localDateString,
		);
		data.allMatches[matchIdx] = createdMatch;
		dispatch('update', data);
	}

	// function to create layout for empty bracket
	function createLayout(): void {
		// create matches for first round from list of players
		for (let i = 0; i < data.players.length - 1; i += 2) {
			let firstPlayer = data.players[i];
			let secondPlayer = data.players[i + 1];

			data.allMatches.push(createMatch(firstPlayer, secondPlayer));
			data.rounds[0].push(data.allMatches.length - 1);
		}

		// connect first round with second round
		arrangeMatches(data.rounds[0], 0);

		// // forward the players to the second round for those who do not have an opponent
		for (let i = 0; i < data.rounds[0].length; i++) {
			if (
				data.allMatches[data.rounds[0][i]].firstPlayerName === '' ||
				data.allMatches[data.rounds[0][i]].secondPlayerName === ''
			) {
				// data.allMatches.splice(data.rounds[0][i] , 1);
				setWinner(data.rounds[0][i]);
			}
		}
		// set up connection for the rest of the matches
		for (let i = 1; i < roundAmount - 1; i++) {
			arrangeMatches(data.rounds[i], i);
		}
	}

	// function to set up connections between matches
	function arrangeMatches(matchesList: number[], round: number): void {
		let nextMatch: Match = {} as Match;

		let updateMatch = false;

		// create new match
		nextMatch = createMatch(emptyPlayer, emptyPlayer);
		data.allMatches.push(nextMatch);

		// push the new match to the next round
		data.rounds[round + 1].push(data.allMatches.length - 1);

		// set up connection between the initial match and the future one
		data.matchesNetwork.set(matchesList[0].toString(), (data.allMatches.length - 1).toString());

		for (let i = 1; i < matchesList.length; i++) {
			// check if a new next match should be created
			if (updateMatch) {
				nextMatch = createMatch(emptyPlayer, emptyPlayer);
				data.allMatches.push(nextMatch);
				data.rounds[round + 1].push(data.allMatches.length - 1);
			}
			// update the flag
			updateMatch = !updateMatch;

			data.matchesNetwork.set(matchesList[i].toString(), (data.allMatches.length - 1).toString());
		}
	}

	// function to check if the winner of a match should go as first player to the future match
	function isFirstWinner(initMatch: number, nextMatch: number) {
		let keys = Array.from(data.matchesNetwork.keys());

		let initIndex = 0;
		let neighbourIndex = 0;

		for (let i = 0; i < keys.length; i++) {
			let currentMatch = Number(data.matchesNetwork.get(keys[i]));
			if (currentMatch === nextMatch) {
				if (keys[i] === initMatch.toString()) {
					initIndex = i;
				} else {
					neighbourIndex = i;
				}
			}
		}

		return initIndex < neighbourIndex;
	}

	// function to mark the match as being in progress
	async function handleMatchStart(matchIdx: number) {
		let isConfirmed = await alertPopup('Are you sure that you want to start this match?');
		if (!isConfirmed) return;

		data.inProgressMatches = [...data.inProgressMatches, matchIdx];
		dispatch('update', data);
	}

	function findPlayer(name: string): Player {
		for (let i = 0; i < data.players.length; i++) {
			if (data.players[i].name === name) {
				return data.players[i];
			}
		}
		return {} as Player;
	}

	// function to set a winner of the match
	// and forward the name of the winner to the next one
	function setWinner(matchIdx: number): void {
		let winner = '';

		// if the player plays with empty player, forward him to the next match
		if (data.allMatches[matchIdx].firstPlayerName === '') {
			winner = data.allMatches[matchIdx].secondPlayerName;
		} else if (data.allMatches[matchIdx].secondPlayerName === '') {
			winner = data.allMatches[matchIdx].firstPlayerName;
		} else {
			// check the scores and set the winner and loser
			if (
				data.allMatches[matchIdx].firstPlayerScore > data.allMatches[matchIdx].secondPlayerScore
			) {
				winner = data.allMatches[matchIdx].firstPlayerName;
				data.leaderBoard.push(findPlayer(data.allMatches[matchIdx].secondPlayerName));
			} else {
				data.leaderBoard.push(findPlayer(data.allMatches[matchIdx].firstPlayerName));
				winner = data.allMatches[matchIdx].secondPlayerName;
			}
		}

		// get the future match
		let matchToUpdate = Number(data.matchesNetwork.get(matchIdx.toString()));

		// if there is no next match, set the winner of the final
		if (matchToUpdate) {
			if (isFirstWinner(matchIdx, matchToUpdate)) {
				data.allMatches[matchToUpdate].firstPlayerName = winner;
			} else {
				data.allMatches[matchToUpdate].secondPlayerName = winner;
			}
		} else {
			data.winner = winner;
			data.leaderBoard.push(findPlayer(winner));
			dispatch('finalize', data.leaderBoard);
		}
		dispatch('update', data);
	}

	async function handleMatchEdit(matchIdx: number) {
		let nextMatchIdx = Number(data.matchesNetwork.get(matchIdx.toString()));
		if (data.inProgressMatches.includes(nextMatchIdx)) {
			dispatch('error', 'The next match is in progress, cannot edit');
			return;
		} else if (data.finishedMatches.includes(nextMatchIdx)) {
			dispatch('error', 'The next match is finished, cannot edit');
			return;
		}

		let scores = await alertInputPopup(
			'Please input the new results of the match',
			data.allMatches[matchIdx].firstPlayerName,
			data.allMatches[matchIdx].secondPlayerName,
		);

		let matchToEdit = data.allMatches[matchIdx];

		if (scores) {
			matchToEdit.firstPlayerScore = Number(scores.firstScore);
			matchToEdit.secondPlayerScore = Number(scores.secondScore);

			setWinner(matchIdx);

			db.editMatch(
				matchToEdit.id.toString(),
				matchToEdit.firstPlayerName,
				matchToEdit.secondPlayerName,
				matchToEdit.firstPlayerScore,
				matchToEdit.secondPlayerScore,
				matchToEdit.tournamentTitle,
				matchToEdit.localDateString,
			);
			dispatch('update', data);
		}
	}

	// function to finish the match
	async function handleMatchEnd(matchIdx: number) {
		// show the form in a popup
		let scores = await alertInputPopup(
			'Please input the results of the match',
			data.allMatches[matchIdx].firstPlayerName,
			data.allMatches[matchIdx].secondPlayerName,
		);

		if (scores) {
			if (scores.firstScore == 0 && scores.secondScore === 0) {
				dispatch('error', 'Cannot set both scores to zero!');
				return;
			}
			// set the scores
			data.allMatches[matchIdx].firstPlayerScore = Number(scores.firstScore);
			data.allMatches[matchIdx].secondPlayerScore = Number(scores.secondScore);

			// delete the match from in prgress matches
			data.inProgressMatches.splice(data.inProgressMatches.indexOf(matchIdx), 1);

			// set the winner of the match
			setWinner(matchIdx);

			data.finishedMatches = [...data.finishedMatches, matchIdx];
			data.inProgressMatches = data.inProgressMatches;

			addMatchToDB(matchIdx);
			dispatch('update', data);
		}
	}

	function isFirstRound(round: number[]) {
		if (round.length !== data.rounds[0].length) {
			return false;
		}
		for (let i = 0; i < data.rounds[0].length; i++) {
			if (data.rounds[0][i] !== round[i]) {
				return false;
			}
		}
		return true;
	}

	function isHidden(round: number[], matchIdx: number) {
		return (
			(data.allMatches[matchIdx].firstPlayerName === '' ||
				data.allMatches[matchIdx].secondPlayerName === '') &&
			isFirstRound(round)
		);
	}

	export let tournament: Tournament;
	export let dataFinal: Final;
	let data = dataFinal as SingleEliminationBracket;

	if (data.allMatches === undefined) {
		data.leaderBoard = [];
		data.allMatches = [];
		data.finishedMatches = [];
		data.inProgressMatches = [];
		data.matchesNetwork = new TSMap<string, string>();
		data.rounds = [];
		data.winner = '';
	}

	function build() {
		data.players = sortPlayers(data.players);
		data.init = false;
		roundAmount = Math.ceil(Math.log2(data.players.length));
		for (let i = 0; i < roundAmount; i++) {
			data.rounds.push([]);
		}
		createLayout();

		dispatch('update', data);
	}

	let roundAmount = 0;
	$: if (dataFinal) {
		data = dataFinal as SingleEliminationBracket;
	}
	onMount(() => {
		if (data.init) {
			build();
		}
	});
</script>

<div class="bracket-wrapper">
	<div class="bracket">
		{#each data.rounds as round}
			<ul>
				{#each round as matchIdx}
					<li class:hidden={isHidden(round, matchIdx)}>
						<div class="names-wrapper">
							<p>{data.allMatches[matchIdx].firstPlayerName}</p>
							<p>{data.allMatches[matchIdx].secondPlayerName}</p>
						</div>

						<button
							class:hidden={tournament.finished ||
								data.allMatches[matchIdx].firstPlayerName === '' ||
								data.allMatches[matchIdx].secondPlayerName === '' ||
								(!$isLeader &&
									data.allMatches[matchIdx].firstPlayerScore === 0 &&
									data.allMatches[matchIdx].secondPlayerScore === 0)}
							disabled={!$isLeader}
							on:click={() => {
								if (data.inProgressMatches.includes(matchIdx)) {
									handleMatchEnd(matchIdx);
								} else if (data.finishedMatches.includes(matchIdx)) {
									handleMatchEdit(matchIdx);
								} else {
									handleMatchStart(matchIdx);
								}
							}}
						>
							{#if data.inProgressMatches.includes(matchIdx) && $isLeader}
								<Dot />
							{:else if data.finishedMatches.includes(matchIdx)}
								<div class="scores-wrapper">
									{data.allMatches[matchIdx].firstPlayerScore}:{data.allMatches[matchIdx]
										.secondPlayerScore}
								</div>
							{:else if $isLeader}
								<Play />
							{/if}
						</button>
					</li>
				{/each}
			</ul>
		{/each}
		<ul>
			<li>
				<p>{data.winner}</p>
			</li>
		</ul>
	</div>
</div>

<style>
	.hidden {
		visibility: hidden;
	}

	button {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 2px;
		font-weight: inherit;
		font-size: var(--fontsize-medium1);
		border: none;
		box-shadow: none;
		overflow: hidden;
		border-radius: 5px;
		background-color: var(--secondary-bg-color);
		color: var(--content-color);
		cursor: pointer;
		height: 1.5rem;
	}

	.scores-wrapper {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 2px;
		font-weight: inherit;
		font-size: var(--fontsize-medium1);
		border: none;
		box-shadow: none;
		overflow: hidden;
		border-radius: 5px;
		background-color: var(--secondary-bg-color);
		color: var(--content-color);
		cursor: pointer;
		height: 1.5rem;
	}

	.bracket {
		display: flex;
		flex-direction: row;
		height: 150%;
		width: 100%;
	}

	ul {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		height: 100%;
		width: 100%;
		flex-grow: 1;
	}

	ul li {
		width: 90%;
		margin: auto;
		max-height: 50px;
		height: 100%;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: flex-end;
		gap: 1rem;
	}

	.names-wrapper {
		min-width: 80%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 2rem;
		gap: 1rem;
	}

	p {
		min-width: 15rem;
		min-height: 1rem;
		border-bottom: 2px solid var(--secondary-bg-color);
	}

	.bracket-wrapper {
		overflow-x: scroll;
		overflow-y: scroll;
		max-width: 900px;
		min-width: 700px;
		height: 30.5rem;
		margin-top: 1rem;
		font-size: var(--fontsize-medium1);
	}

	.bracket-wrapper::-webkit-scrollbar {
		display: none;
	}
</style>
