<script lang="ts">
	import { enhance } from '$app/forms';

	import Button from '$lib/components/base/Button.svelte';

	import { convertDateToString } from '$lib/utils';
	import DropdownInput from '$lib/components/base/inputs/DropdownInput.svelte';
	import type { Match, Player, Tournament } from '$lib/types/types';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import { alertPopup } from '$lib/client/popup/popup.handler';

	export let players: Player[];
	export let tournaments: Tournament[];

	export let match: Match;

	export let chosenId = -1;

	let tournamentTitles = [''];
	let latestTournamentTitle = '';

	let isSubmissionDisabled = true;

	$: {
		isSubmissionDisabled = !(
			match.firstPlayerName &&
			match.secondPlayerName &&
			(match.firstPlayerScore !== 0 || match.secondPlayerScore !== 0) &&
			match.tournamentTitle !== null
		);
	}

	$: playerNames = players.map((player) => player.name);

	$: {
		tournamentTitles = tournaments.map((tournament) => tournament.title);
		latestTournamentTitle = tournamentTitles[0];
	}

	function handleSelectFirstPlayerName(event: CustomEvent) {
		match.firstPlayerName = event.detail;
	}
	function handleSelectSecondPlayerName(event: CustomEvent) {
		match.secondPlayerName = event.detail;
	}
	function handleSelectTournament(event: CustomEvent) {
		match.tournamentTitle = event.detail;
		changeDateByTournamentTitle(match.tournamentTitle);
	}

	function changeDateByTournamentTitle(tournamentTitle: string) {
		const tournament = tournaments.find((tournament) => tournament.title === tournamentTitle);
		if (tournament) {
			const dateString = tournament.startDateString;
			const year = Number(dateString.slice(6, 10));
			const month = Number(dateString.slice(3, 5)) - 1;
			const day = Number(dateString.slice(0, 2));
			match.localDateString = convertDateToString(new Date(year, month, day));
		}
	}

	function resetMatch() {
		match = {} as Match;
		chosenId = -1;
	}
</script>

<h2>Edit Match</h2>

<form
	method="POST"
	action="?/editMatch"
	use:enhance={async ({ cancel }) => {
		let isConfirmed = await alertPopup('Are you sure that you want to edit this match?');
		if (!isConfirmed) {
			cancel();
		}

		return async ({ update }) => {
			await update({ reset: false });
			resetMatch();
		};
	}}
>
	<input type="hidden" name="matchId" value={match.id} />
	<div class="column-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<DropdownInput
				name="firstPlayerName"
				placeholder="First player"
				options={playerNames}
				on:select={handleSelectFirstPlayerName}
				defaultValue={match.firstPlayerName}
				bind:inputVal={match.firstPlayerName}
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<DropdownInput
				name="secondPlayerName"
				placeholder="Second player"
				options={playerNames}
				on:select={handleSelectSecondPlayerName}
				defaultValue={match.secondPlayerName}
				bind:inputVal={match.secondPlayerName}
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
				bind:numberVal={match.firstPlayerScore}
				defaultNumValue={match.firstPlayerScore}
			/>
		</label>

		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="number"
				name="secondPlayerScore"
				placeholder="Second score"
				bind:numberVal={match.secondPlayerScore}
				defaultNumValue={match.secondPlayerScore}
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
				bind:stringVal={match.localDateString}
				defaultValue={match.localDateString}
			/>
		</label>

		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<DropdownInput
				name="tournamentTitle"
				placeholder="Tournament"
				options={tournamentTitles}
				on:select={handleSelectTournament}
				defaultValue={match.tournamentTitle}
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<div class="last-box full-width">
			<Button dark={false} disabled={isSubmissionDisabled} type={'submit'}>Save</Button>
		</div>
	</div>
</form>

<style>
	h2 {
		text-transform: uppercase;
		font-size: var(--fontsize-medium1);
		margin: 1.5em 0;
		font-weight: var(--fontweight-1);
		color: var(--title-color);
	}

	form {
		max-width: 800px;
		margin: 0 auto 3em;
		font-size: var(--fontsize-medium2);
	}
	.column-2-elems {
		margin-top: 1rem;
		display: grid;
		grid-template-columns: repeat(1, 1fr);
		gap: 1.25rem;
		align-items: end;
	}
	.line-2-elems {
		margin-top: 1rem;
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 1.25rem;
		align-items: end;
	}

	.last-box {
		grid-column: 2;
		margin-top: 1.5em;
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
