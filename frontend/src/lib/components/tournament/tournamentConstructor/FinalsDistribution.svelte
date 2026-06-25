<script lang="ts">
	import * as db from '$lib/client/requests';
	import { createEventDispatcher } from 'svelte';
	import Button from '$lib/components/base/Button.svelte';
	import FinalsDistributor from '$lib/components/tournament/tournamentConstructor/FinalsDistributor.svelte';
	import BackArrowButton from '$lib/components/base/BackArrowButton.svelte';
	import type { Player, Tournament } from '$lib/types/types';
	import type {
		Final,
		Group,
		SingleEliminationBracket,
		TournamentStage,
	} from '$lib/types/types.tournaments';

	export let numberFinals = 0;
	export let stage: TournamentStage;
	export let finals: Player[][];
	export let tournament: Tournament;

	let types: ('Groups' | 'Single Elimination')[] = [];
	let chosenId: number[];

	const dispatch = createEventDispatcher();

	$: numberGroups = tournament.state.firstStage?.length ?? 0;
	$: numberParticipants = tournament.state.participants.length;

	function back() {
		stage = 'numberFinals';
	}
	async function nextStage() {
		let distribution: Player[][] = new Array(numberFinals).fill(null).map(() => new Array(0));
		let nChosenId: number[] = new Array(chosenId.length).fill(null);
		let temp: Final[] = [];
		for (let i = 0; i < chosenId.length; i++) {
			nChosenId[i] = Math.floor((chosenId[i] - 1) / chosenId.length);
		}
		for (let i = 0; i < nChosenId.length; i++) {
			for (let j = 0; j < finals[i].length; j++) {
				if (finals[i][j]) {
					distribution[nChosenId[i]].push(finals[i][j]);
				}
			}
		}
		for (let i = 0; i < numberFinals; i++) {
			let groupPlayers: Player[] = [];
			for (let j = 0; j < distribution[i].length; j++) {
				groupPlayers.push(distribution[i][j]);
			}
			let newGroup: Group = {
				type: 'Group',
				tournamentTitle: tournament.title,
				players: groupPlayers,
				matches: [],
				id: i,
			};
			let newBracket: SingleEliminationBracket = {
				init: true,
				type: 'SingleEliminationBracket',
				players: groupPlayers,
			} as SingleEliminationBracket;
			if (types[i] === 'Groups') {
				if (temp) {
					temp.push(newGroup);
				} else {
					temp = [];
					temp.push(newGroup);
				}
			}
			if (types[i] === 'Single Elimination') {
				if (temp) {
					temp.push(newBracket);
				} else {
					temp = [];
					temp.push(newBracket);
				}
			}
		}
		if (
			!tournament.state.secondStage ||
			!tournament.state.secondStage.length ||
			tournament.state.secondStage.length != temp.length
		) {
			tournament.state.secondStage = temp;
		} else {
			let equal = true;
			for (let i = 0; i < temp.length; i++) {
				if (tournament.state.secondStage[i].players.length != temp[i].players.length) {
					equal = false;
					break;
				}
			}
			if (!equal) {
				tournament.state.secondStage = temp;
			}
		}
		dispatch('update', { state: tournament.state });
		stage = 'secondStage';
	}
</script>

<BackArrowButton action={back} />

<div class="content">
	<h1>How many finals do you want?</h1>
	<div class="numberFinals">
		{numberFinals}
	</div>
	<div class="distribution">
		<h1>Distribute places between finals</h1>
		<div class="distributor">
			<FinalsDistributor
				bind:numberGroups
				bind:numberParticipants
				bind:numberFinals
				bind:chosenId
				bind:types
			/>
		</div>
		<div class="button">
			<Button type="button" on:click={nextStage}>Confirm</Button>
		</div>
	</div>
</div>

<style>
	.content {
		margin: auto;
		margin-top: 6.25rem;
		width: 64rem;
		text-align: center;
	}
	.distribution {
		margin-top: 4rem;
	}
	.distributor {
		margin-top: 2rem;
	}
	.numberFinals {
		margin: auto;
		margin-top: 2rem;
		max-width: 4rem;
		height: 2rem;
		border-bottom: 4px solid var(--secondary-bg-color, #de9b8a);
		text-align: center;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: var(--fontsize-medium1);
	}
	.button {
		width: 9rem;
		height: 2.5rem;
		margin: 2rem auto;
	}

	h1 {
		font-size: var(--fontsize-large);
		font-weight: var(--fontweight-1);
		letter-spacing: 0.09375rem;
	}

	@media (max-width: 1100px) {
		.content {
			margin: auto;
			margin-top: 6.25rem;
			width: 35rem;
			text-align: center;
		}
		.distributor {
			width: 30rem;
			margin: 0 auto;
		}
	}
</style>
