<script lang="ts">
	// import { enhance } from '$app/forms';
	import { ADD_TOURNAMENT_FORM, AddTournamentFormStore } from '$lib/client/stores/stores.forms';
	import Button from '$lib/components/base/Button.svelte';
	import { convertDateToString } from '$lib/utils';
	import ResetButton from '$lib/components/base/ResetButton.svelte';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import { enhance } from '$app/forms';

	let isSubmissionDisabled = true;

	$: {
		isSubmissionDisabled = !(
			$AddTournamentFormStore.title &&
			$AddTournamentFormStore.startDateString &&
			$AddTournamentFormStore.endDateString
		);
	}

	let startDateString = convertDateToString(new Date());
	$AddTournamentFormStore.endDateString = convertDateToString(new Date());

	$: $AddTournamentFormStore.startDateString = startDateString;
	$: $AddTournamentFormStore.endDateString = startDateString;

	function resetForm() {
		$AddTournamentFormStore = structuredClone(ADD_TOURNAMENT_FORM);
	}
</script>

<div class="header-line">
	<h2>Add Tournament</h2>
	<ResetButton onClick={resetForm} label="Reset" />
</div>

<form method="POST" action="?/createTournament" use:enhance>
	<div class="column-1-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label class="elem1">
			<InputTemplate
				type="text"
				name="title"
				placeholder="Tournament title"
				required={true}
				isFirst={true}
				bind:stringVal={$AddTournamentFormStore.title}
			/>
		</label>
	</div>
	<div class="column-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label class="elem2">
			<InputTemplate
				type="date"
				name="startDateString"
				placeholder="Start date"
				bind:stringVal={startDateString}
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label class="elem3">
			<InputTemplate
				type="date"
				name="endDateString"
				placeholder="End date"
				bind:stringVal={$AddTournamentFormStore.endDateString}
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<div class="last-box full-width margin-top">
			<Button dark={false} disabled={isSubmissionDisabled} type={'submit'}>Add tournament</Button>
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
		font-size: var(--fontsize-medium2);
	}
	.column-2-elems {
		margin-top: 1rem;
		display: grid;
		grid-template-columns: repeat(1, 1fr);
		gap: 1rem;
		align-items: end;
	}
	.column-1-elems {
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
		.elem1 {
			display: inline-block;
			grid-column-start: 1;
			grid-column-end: 3;
		}
		.last-box {
			grid-column: 2;
		}
	}
	.margin-top {
		margin-top: 1em;
	}
</style>
