<script lang="ts">
	import type { Match, Player } from '$lib/types/types';
	import { alertInputPopup } from '$lib/client/popup/popup.handler.input';
	import { createMatch, editMatch } from '$lib/client/requests';
	import { createEventDispatcher, onMount } from 'svelte';
	import { isLeader } from '$lib/client/stores/stores';
	import type { Final, Group } from '$lib/types/types.tournaments';
	import type { OverlayOutput } from '$lib/types/types.popup';
	const dispatch = createEventDispatcher();
	export let finalInfo: Final;
	export let tournamentDate = '';
	export let isInConstructor = false;

	let groupInfo = finalInfo as Group;
	let data = [] as Player[];
	if (groupInfo.players && groupInfo.players.length > 0) {
		data = groupInfo.players;
	}
	let order: number[][][];
	let tour: number[] = new Array(data.length);
	let tablePlaying: boolean[][] = new Array(data.length)
		.fill(null)
		.map(() => new Array(data.length).fill(false));
	let tablePoints: number[][] = new Array(data.length)
		.fill(null)
		.map(() => new Array(data.length).fill(null));
	let sumPoints: number[] = new Array(data.length).fill(0);
	let finalPlaces: number[] = new Array(data.length).fill(-1);
	let matchID: number[][] = new Array(data.length)
		.fill(null)
		.map(() => new Array(data.length).fill(null));
	let tableResults: number[][] = new Array(data.length)
		.fill(null)
		.map(() => new Array(data.length).fill(null));
	let finalPlayers: Player[] = new Array(data.length);
	const findPlayerNumber = (name: string) => {
		for (let i = 0; i < data.length; i++) {
			if (data[i].name == name) {
				return i;
			}
		}
		return -1;
	};
	if (groupInfo.matches && groupInfo.matches.length > 0) {
		for (let a of groupInfo.matches) {
			let first = findPlayerNumber(a.firstPlayerName);
			let second = findPlayerNumber(a.secondPlayerName);
			tableResults[first][second] = a.firstPlayerScore;
			tableResults[second][first] = a.secondPlayerScore;
			matchID[first][second] = matchID[second][first] = a.id;
		}
	}
	const generateOrder = () => {
		let orderN = data.length;
		let orderM = orderN - 1 + (orderN % 2);
		order = new Array(Math.floor(orderN / 2))
			.fill(null)
			.map(() => new Array(orderM).fill(null).map(() => new Array(2).fill(null)));
		for (let i = 0; i < orderM; i++) {
			order[0][i][0] = 1;
		}
		for (let i = orderM - 1; i >= 0; i--) {
			order[0][i][1] = orderM - i + 1;
		}
		if (orderN % 2 == 1) {
			order[0][0] = [2, orderN];
		}
		for (let i = 1; i < Math.floor(orderN / 2); i++) {
			order[i][0] = [order[i - 1][0][0] + 1, order[i - 1][0][1] - 1];
		}
		for (let i = 1; i < Math.floor(orderN / 2); i++) {
			let temp = [order[i][0][0], order[i][0][1]] as number[];
			for (let j = 1; j < orderM; j++) {
				temp = [temp[0] == 2 ? orderN : temp[0] - 1, temp[1] == 2 ? orderN : temp[1] - 1];
				if (orderN % 2 == 1) {
					temp[0] = temp[1] == orderN ? temp[0] + 1 : temp[0];
					temp[1] = temp[0] == orderN ? temp[1] + 1 : temp[1];
				}
				order[i][j] = temp;
			}
		}
	};
	const generateTour = () => {
		tour.fill(0);
		if (data.length % 2 == 1) {
			tour[0] = 1;
		}
	};
	const generatePlaces = () => {
		let placesTribune: number[] = Array.from(Array(data.length).keys());
		placesTribune.sort(function (a, b) {
			return sumPoints[b] - sumPoints[a];
		});
		let temp: number[] = [];
		const sortTemp = () => {
			let scores: number[] = new Array(data.length).fill(0);
			for (let i = 0; i < temp.length; i++) {
				for (let j = 0; j < temp.length; j++) {
					if (i != j) {
						let first: number = temp[i];
						let second: number = temp[j];
						scores[first] += tableResults[first][second] - tableResults[second][first];
					}
				}
			}
			temp.sort(function (a, b) {
				if (scores[a] == scores[b]) {
					return b - a;
				}
				return scores[a] - scores[b];
			});
		};
		for (let i = 0; i < data.length; i++) {
			if (!temp.length || sumPoints[temp[0]] == sumPoints[placesTribune[i]]) {
				temp.push(placesTribune[i]);
			} else {
				sortTemp();
				for (let j = i - 1; j >= i - temp.length; j--) {
					placesTribune[j] = temp[i - j - 1];
				}
				temp = [placesTribune[i]];
			}
		}
		sortTemp();
		for (let i = data.length - 1; i >= data.length - temp.length; i--) {
			placesTribune[i] = temp[data.length - i - 1];
		}
		for (let i = 0; i < data.length; i++) {
			if (finalPlaces[placesTribune[i]] != i) {
				finalPlaces[placesTribune[i]] = i;
			}
		}
		for (let i = 0; i < data.length; i++) {
			finalPlayers[i] = data[placesTribune[i]];
		}
		dispatch('finalize', finalPlayers);
	};
	const findTable = (num: number) => {
		if (num >= tour.length || tour[num] >= data.length - 1 + (data.length % 2)) {
			return -2;
		}
		for (let i = 0; i < Math.floor(data.length / 2); i++) {
			if (order[i][tour[num]].includes(num + 1)) {
				return i;
			}
		}
		return -1;
	};
	const checkTable = (row: number, column: number) => {
		if (
			tour[row] >= order[0].length ||
			tour[column] >= order[0].length ||
			tour[row] != tour[column]
		) {
			return false;
		}
		if (
			findTable(row) == findTable(column) &&
			order[findTable(row)][tour[row]].includes(column + 1) &&
			(tableResults[row][column] || tableResults[column][row])
		) {
			tour[row]++;
			tour[column]++;
			if (findTable(row) == -1) {
				tour[row]++;
			}
			if (findTable(column) == -1) {
				tour[column]++;
			}
			return false;
		}
		return findTable(row) == findTable(column);
	};
	const setMatchID = (currentMatch: Match) => {
		const first = findPlayerNumber(currentMatch.firstPlayerName);
		const second = findPlayerNumber(currentMatch.secondPlayerName);

		console.log(matchID);
		console.log(first, second);

		matchID[first][second] = matchID[second][first] = currentMatch.id;
	};
	const matchFinished = (row: number, column: number) => {
		if (checkTable(row, column)) {
			tour[row]++;
			tour[column]++;
			if (tour[row] >= order[0].length || tour[column] >= order[0].length) {
				return;
			}
		}
	};
	async function setMatch(
		row: number,
		column: number,
		first: number,
		second: number,
		date: string,
	) {
		if (!tableResults[row][column] && !tableResults[column][row]) {
			const match = await createMatch(
				data[row].name,
				data[column].name,
				first,
				second,
				groupInfo.tournamentTitle,
				date,
			);
			setMatchID(match);
			let newMatch = {} as Match;
			newMatch.firstPlayerName = data[row].name;
			newMatch.secondPlayerName = data[column].name;
			newMatch.firstPlayerScore = first;
			newMatch.secondPlayerScore = second;
			newMatch.id = matchID[row][column];
			groupInfo.matches.push(newMatch);
		} else {
			editMatch(
				matchID[row][column].toString(),
				data[row].name,
				data[column].name,
				first,
				second,
				groupInfo.tournamentTitle,
				date,
			);
			for (let changedMatch of groupInfo.matches) {
				if (changedMatch.id == matchID[row][column]) {
					if (findPlayerNumber(changedMatch.firstPlayerName) == row) {
						changedMatch.firstPlayerScore = first;
						changedMatch.secondPlayerScore = second;
					} else {
						changedMatch.firstPlayerScore = second;
						changedMatch.secondPlayerScore = first;
					}
					break;
				}
			}
		}
		let newGroup: Group = {
			type: groupInfo.type,
			tournamentTitle: groupInfo.tournamentTitle,
			players: data,
			matches: groupInfo.matches,
			id: groupInfo.id,
		};
		dispatch('update', newGroup);
	}
	async function handleWaiting(firstName: string, secondName: string) {
		const first = findPlayerNumber(firstName);
		const second = findPlayerNumber(secondName);
		let results: OverlayOutput | null = await alertInputPopup(
			'Write the score:',
			firstName,
			secondName,
			tournamentDate,
		);
		if (results && results.firstScore != results.secondScore) {
			const firstScore = String(results.firstScore);
			const secondScore = String(results.secondScore);
			const matchDate = results.date;
			if (first != -1 && second != -1) {
				tablePoints[first][second] = firstScore > secondScore ? 2 : 1;
				tablePoints[second][first] = secondScore > firstScore ? 2 : 1;
				countPoints(first);
				countPoints(second);
				setMatch(first, second, Number(firstScore), Number(secondScore), matchDate);
				tableResults[first][second] = Number(firstScore);
				tableResults[second][first] = Number(secondScore);
				if (tablePlaying[first][second]) {
					tablePlaying[first][second] = false;
					tablePlaying[second][first] = false;
					matchFinished(first, second);
				}
				if (allMatchesPlayed()) {
					generatePlaces();
				}
			}
		}
	}
	const waitingForMatchResult = (event: MouseEvent, row: number, column: number) => {
		event.preventDefault();
		handleWaiting(data[row].name, data[column].name);
	};
	const waitingForMatchResultImmediately = (event: MouseEvent, row: number, column: number) => {
		event.preventDefault();
		tablePlaying[row][column] = tablePlaying[column][row] = true;
		handleWaiting(data[row].name, data[column].name);
	};
	const matchStarted = (event: MouseEvent, row: number, column: number) => {
		event.preventDefault();
		tablePlaying[row][column] = tablePlaying[column][row] = true;
	};

	const allMatchesPlayed = () => {
		for (let i = 0; i < data.length; i++) {
			for (let j = 0; j < data.length; j++) {
				if (i != j && !tableResults[i][j] && !tableResults[j][i]) {
					return false;
				}
			}
		}
		return true;
	};
	const countPoints = (row: number) => {
		sumPoints[row] = 0;
		for (let i = 0; i < data.length; i++) {
			sumPoints[row] += tablePoints[row][i];
		}
		return sumPoints[row];
	};
	const generatePoints = () => {
		for (let i = 0; i < data.length; i++) {
			for (let j = 0; j < data.length; j++) {
				if (tableResults[i][j] > tableResults[j][i]) {
					tablePoints[i][j] = 2;
				}
				if (tableResults[i][j] < tableResults[j][i]) {
					tablePoints[i][j] = 1;
				}
			}
		}
		for (let i = 0; i < data.length; i++) {
			countPoints(i);
		}
	};
	if (allMatchesPlayed()) {
		generatePlaces();
	}
	$: if (finalInfo) {
		groupInfo = finalInfo as Group;
		data = [] as Player[];
		if (groupInfo.players && groupInfo.players.length > 0) {
			data = groupInfo.players;
		}
		tableResults = new Array(data.length).fill(null).map(() => new Array(data.length).fill(null));
		if (groupInfo.matches && groupInfo.matches.length > 0) {
			for (let a of groupInfo.matches) {
				let first = findPlayerNumber(a.firstPlayerName);
				let second = findPlayerNumber(a.secondPlayerName);
				tableResults[first][second] = a.firstPlayerScore;
				tableResults[second][first] = a.secondPlayerScore;
				matchID[first][second] = matchID[second][first] = a.id;
			}
		}
		tour = new Array(data.length);
		tablePlaying = new Array(data.length).fill(null).map(() => new Array(data.length).fill(false));
		tablePoints = new Array(data.length).fill(null).map(() => new Array(data.length).fill(null));
		sumPoints = new Array(data.length).fill(0);
		finalPlaces = new Array(data.length).fill(-1);
		generateOrder();
		generateTour();
		generatePoints();
		if (allMatchesPlayed()) {
			generatePlaces();
		}
	}
	onMount(() => {
		if (allMatchesPlayed()) {
			dispatch('finalize', finalPlayers);
		}
	});
</script>

{#if data.length > 0}
	<h1><slot /></h1>
	<form class="table-container">
		<table>
			<tr>
				<th />
				{#each Array.from({ length: data.length }, (_, i) => i + 1) as number}
					<th>{number}</th>
				{/each}
				<th class="last-column">Points</th>
				<th class="last-column">Place</th>
			</tr>
			{#each data as row, rowIndex}
				<tr>
					<td class="name-cell">{row.name}</td>
					{#each Array(tour.length + 2) as cell, index (index)}
						{#if index < tour.length}
							{#if index === rowIndex && cell === cell}
								<td class="useless-cell">
									<div class="useless-cell-background" />
								</td>
							{:else}
								<td>
									{#if (tableResults[rowIndex][index] || tableResults[index][rowIndex]) && !checkTable(rowIndex, index)}
										<button
											class="results-cell"
											disabled={!$isLeader || !isInConstructor}
											on:click={(event) => {
												waitingForMatchResult(event, rowIndex, index);
											}}
										>
											{tablePoints[rowIndex][index]}<br />{tableResults[rowIndex][index]} : {tableResults[
												index
											][rowIndex]}
										</button>
									{:else if rowIndex !== index && checkTable(rowIndex, index) && tablePlaying[rowIndex][index]}
										<button
											class="finish-match-button match-button"
											disabled={!$isLeader || !isInConstructor}
											on:click={(event) => {
												waitingForMatchResult(event, rowIndex, index);
											}}
										/>
									{:else if rowIndex !== index && checkTable(rowIndex, index) && !tablePlaying[rowIndex][index]}
										<button
											class="start-match-button match-button"
											disabled={!$isLeader || !isInConstructor}
											on:click={(event) => {
												matchStarted(event, rowIndex, index);
											}}
										/>
									{:else if rowIndex !== index}
										<button
											class="invisible-match-button"
											disabled={!$isLeader || !isInConstructor}
											on:click={(event) => {
												waitingForMatchResultImmediately(event, rowIndex, index);
											}}
										/>
									{/if}
								</td>
							{/if}
						{/if}
						{#if index === tour.length}
							<td>{sumPoints[rowIndex]}</td>
						{/if}
						{#if index === tour.length + 1}
							<td>{allMatchesPlayed() ? finalPlaces[rowIndex] + 1 : ''}</td>
						{/if}
					{/each}
				</tr>
			{/each}
		</table>
	</form>
{/if}

<style>
	h1 {
		font-weight: var(--fontweight-2);
		font-size: var(--fontsize-large);
		margin-bottom: 1rem;
		text-align: center;
	}
	.table-container {
		width: 100%;
		margin-bottom: 3rem;
	}
	table {
		margin-left: auto;
		margin-right: auto;
		border-collapse: separate;
		border-spacing: 0;
		overflow: hidden;
		width: max-content;
		border-radius: 10px;
		box-shadow: 0 0 5px var(--not-chosen-font-color);
	}
	tr,
	td,
	th {
		border-right: 1px solid var(--content-color);
		border-bottom: 1px solid var(--content-color);
		text-align: center;
		height: 3rem;
		width: 3rem;
		vertical-align: middle;
	}
	th {
		width: max-content;
	}
	tr td:last-child,
	tr th:last-child {
		border-right: none;
	}
	tr:last-child td {
		border-bottom: none;
	}
	.name-cell {
		width: 8rem;
		padding-left: 0.2rem;
		padding-right: 0.2rem;
	}
	.last-column {
		width: 5rem;
	}
	.useless-cell {
		height: 3rem;
		width: 3rem;
		padding: 0;
	}
	.useless-cell-background {
		height: 3rem;
		width: 3rem;
		background: var(--content-color);
		opacity: 0.1;
	}
	.results-cell {
		height: 100%;
		width: 100%;
		background: var(--main-color);
		color: var(--content-color);
		font-size: var(--fontsize-medium2);
		border: none;
		padding: 0;
		margin: 0;
		cursor: pointer;
		white-space: nowrap;
	}
	.match-button {
		height: 1.5rem;
		width: 1.5rem;
		border-radius: 100%;
		cursor: pointer;
		border: none;
	}
	.invisible-match-button {
		height: 100%;
		width: 100%;
		cursor: pointer;
		border: none;
		opacity: 0;
	}
	.start-match-button {
		background: var(--secondary-color);
	}
	.finish-match-button {
		background: var(--content-color);
	}
	button:disabled {
		cursor: unset;
	}
	@media (max-width: 800px) {
		.table-container {
			width: 100%;
			overflow-x: auto;
			overflow-y: hidden;
			white-space: nowrap;
			padding: 1rem;
		}
		table {
			width: max-content;
		}
	}
	@media (max-width: 480px) {
		.table-container {
			width: 100%;
			overflow-x: auto;
			overflow-y: hidden;
			white-space: nowrap;
			padding: 1rem;
		}
		table {
			width: max-content;
		}
		tr,
		td,
		th {
			height: 2rem;
			width: 2rem;
			padding: 0.1rem;
		}
		td:has(.match-button) {
			vertical-align: unset;
			padding: 0;
		}
		.name-cell {
			padding: 0.4rem;
			width: 5rem;
		}
		.last-column {
			width: 2rem;
		}
		.match-button {
			border-radius: 0;
			width: 100%;
			height: 100%;
			margin: 0;
			padding: 0;
			vertical-align: unset;
		}
	}
</style>
