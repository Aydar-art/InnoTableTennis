<script lang="ts">
	import Button from '$lib/components/base/Button.svelte';

	import type { Player } from '$lib/types/types';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import { alertPopup } from '$lib/client/popup/popup.handler';
	import { enhance } from '$app/forms';

	export let player: Player;
	export let chosenId = -1;

	let isSubmissionDisabled = true;

	$: {
		isSubmissionDisabled = !player.name;
	}

	function resetPlayer() {
		player = {} as Player;
		chosenId = -1;
	}
</script>

<h2>Edit Player</h2>

<form
	method="POST"
	action="?/editPlayer"
	use:enhance={async ({ cancel }) => {
		let isConfirmed = await alertPopup(`Are you sure that you want to edit player ${player.name}?`);
		if (!isConfirmed) {
			cancel();
		}

		return async ({ update }) => {
			await update({ reset: false });
			resetPlayer();
		};
	}}
>
	<input type="hidden" name="playerId" value={player.id} />
	<!-- svelte-ignore a11y-label-has-associated-control -->
	<div class="column-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="text"
				name="name"
				placeholder="Player's name"
				required={true}
				isFirst={true}
				defaultValue={player.name}
				bind:stringVal={player.name}
			/>
		</label>
		<label>
			<InputTemplate
				type="text"
				name="telegramAlias"
				placeholder="Player's alias"
				defaultValue={player.telegramAlias}
				bind:stringVal={player.telegramAlias}
			/>
		</label>
	</div>
	<div class="column-1-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="float"
				name="rating"
				placeholder="Rating"
				defaultNumValue={player.rating}
				bind:numberVal={player.rating}
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
		.column-2-elems {
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
