<script lang="ts">
	import { enhance } from '$app/forms';

	import Button from '$lib/components/base/Button.svelte';
	import { ADD_MATCH_FORM, AddMatchFormStore } from '$lib/client/stores/stores.forms';

	import { convertDateToString } from '$lib/utils';
	import DropdownInput from '$lib/components/base/inputs/DropdownInput.svelte';

	import type { Player, Tournament } from '$lib/types/types';
	import ResetButton from '$lib/components/base/ResetButton.svelte';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';

	export let players: Player[];
	export let tournaments: Tournament[];

	let tournamentTitles = [''];
	let latestTournamentTitle = '';

	$: playerNames = players.map((player) => player.name);

	tournamentTitles = tournaments.map((tournament) => tournament.title);
	latestTournamentTitle = tournamentTitles[0];

	if (!$AddMatchFormStore.tournamentTitle) {
		$AddMatchFormStore.tournamentTitle = latestTournamentTitle;
		changeDateByTournamentTitle($AddMatchFormStore.tournamentTitle);
	}

	let isSubmissionDisabled = true;

	$: {
		isSubmissionDisabled = !(
			$AddMatchFormStore.firstPlayerName &&
			$AddMatchFormStore.secondPlayerName &&
			($AddMatchFormStore.firstPlayerScore !== 0 || $AddMatchFormStore.secondPlayerScore !== 0) &&
			$AddMatchFormStore.tournamentTitle !== null
		);
	}

	$AddMatchFormStore.localDateString = convertDateToString(new Date());

	let inputResets = new Array(2);

	function resetForm() {
		$AddMatchFormStore = structuredClone(ADD_MATCH_FORM);
		inputResets.forEach((reset) => {
			reset();
		});
	}

	function handleSelectFirstPlayerName(event: CustomEvent) {
		$AddMatchFormStore.firstPlayerName = event.detail;
	}
	function handleSelectSecondPlayerName(event: CustomEvent) {
		$AddMatchFormStore.secondPlayerName = event.detail;
	}
	function handleSelectTournament(event: CustomEvent) {
		$AddMatchFormStore.tournamentTitle = event.detail;
		changeDateByTournamentTitle($AddMatchFormStore.tournamentTitle);
	}

	function changeDateByTournamentTitle(tournamentTitle: string) {
		const tournament = tournaments.find((tournament) => tournament.title === tournamentTitle);

		if (tournament) {
			const dateString = tournament.startDateString;
			const year = Number(dateString.slice(6, 10));
			const month = Number(dateString.slice(3, 5)) - 1;
			const day = Number(dateString.slice(0, 2));

			$AddMatchFormStore.localDateString = convertDateToString(new Date(year, month, day));
		}
	}
</script>

<div class="header-line">
	<h2>Add Match</h2>
	<ResetButton onClick={resetForm} label="Reset" />
</div>

<form method="POST" action="?/createMatch" use:enhance>
	<div class="column-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<DropdownInput
				name="firstPlayerName"
				placeholder="First player"
				options={playerNames}
				on:select={handleSelectFirstPlayerName}
				isFirstInput={true}
				defaultValue={$AddMatchFormStore.firstPlayerName}
				bind:reset={inputResets[0]}
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<DropdownInput
				name="secondPlayerName"
				placeholder="Second player"
				options={playerNames}
				defaultValue={$AddMatchFormStore.secondPlayerName}
				on:select={handleSelectSecondPlayerName}
				bind:reset={inputResets[1]}
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="number"
				name="firstPlayerScore"
				placeholder="First score"
				bind:numberVal={$AddMatchFormStore.firstPlayerScore}
			/>
		</label>

		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="number"
				name="secondPlayerScore"
				placeholder="Second score"
				bind:numberVal={$AddMatchFormStore.secondPlayerScore}
			/>
		</label>
	</div>
	<div class="column-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="date"
				name="localDateString"
				placeholder="Date"
				bind:stringVal={$AddMatchFormStore.localDateString}
			/>
		</label>

		<!-- svelte-ignore a11y-label-has-associated-control -->

		<label>
			<DropdownInput
				name="tournamentTitle"
				placeholder="Tournament"
				defaultValue={$AddMatchFormStore.tournamentTitle}
				options={tournamentTitles}
				on:select={handleSelectTournament}
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<div class="last-box full-width">
			<Button dark={false} disabled={isSubmissionDisabled} type={'submit'}>Add match</Button>
		</div>
	</div>
</form>

<style>
	h2 {
		font-size: var(--fontsize-large);
		margin: 0.9em 0;
		font-weight: var(--fontweight-1);
		color: var(--title-color);
	}

	form {
		max-width: 800px;
		font-size: var(--fontsize-medium1);
	}
	.column-2-elems {
		margin-top: 1rem;
		display: grid;
		grid-template-columns: repeat(1, 1fr);
		gap: 1rem;
		align-items: end;
	}
	.line-2-elems {
		margin-top: 1rem;
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 1rem;
		align-items: end;
	}
	.header-line {
		display: flex;
	}

	.last-box {
		grid-column: 2;
		margin-top: 1.5em;
	}
	.full-width {
		width: 100%;
	}

	@media (max-width: 800px) {
		.line-2-elems {
			grid-template-columns: repeat(2, 1fr);
		}
		.last-box {
			grid-column: 2;
		}
	}
</style>
