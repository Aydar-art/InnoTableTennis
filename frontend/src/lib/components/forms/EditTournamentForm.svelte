<script lang="ts">
	// import { enhance } from '$app/forms';
	import Button from '$lib/components/base/Button.svelte';

	import type { Tournament } from '$lib/types/types';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import { alertPopup } from '$lib/client/popup/popup.handler';
	import { enhance } from '$app/forms';

	export let tournament: Tournament;
	export let chosenId = -1;

	let isSubmissionDisabled = true;

	$: {
		isSubmissionDisabled = !(
			tournament.title &&
			tournament.startDateString &&
			tournament.endDateString
		);
	}

	function resetTournament() {
		tournament = {} as Tournament;
		chosenId = -1;
	}
</script>

<h2>Edit Tournament</h2>

<form
	method="POST"
	action="?/editTournament"
	use:enhance={async ({ cancel }) => {
		let isConfirmed = await alertPopup('Are you sure that you want to edit this tournament?');
		if (!isConfirmed) {
			cancel();
		}

		return async ({ update }) => {
			await update({ reset: false });
			resetTournament();
		};
	}}
>
	<input type="hidden" name="tournamentId" value={tournament.id} />
	<div class="column-1-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="text"
				name="title"
				placeholder="Tournament title"
				required={true}
				isFirst={true}
				defaultValue={tournament.title}
				bind:stringVal={tournament.title}
			/>
		</label>
	</div>
	<div class="column-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->

		<label>
			<InputTemplate
				type="date"
				name="startDateString"
				placeholder="Start date"
				bind:stringVal={tournament.startDateString}
				defaultValue={tournament.startDateString}
			/>
		</label>

		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="date"
				name="endDateString"
				placeholder="End date"
				bind:stringVal={tournament.endDateString}
				defaultValue={tournament.endDateString}
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<div class="last-box full-width margin-top">
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
	.column-1-elems {
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
	.margin-top {
		margin-top: 1em;
	}
</style>
