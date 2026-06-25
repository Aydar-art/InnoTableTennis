<script lang="ts">
	import { enhance } from '$app/forms';
	import Button from '$lib/components/base/Button.svelte';
	import { ADD_PLAYER_FORM, AddPlayerFormStore } from '$lib/client/stores/stores.forms';

	import ResetButton from '$lib/components/base/ResetButton.svelte';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';

	let isSubmissionDisabled = true;

	$: {
		isSubmissionDisabled = !$AddPlayerFormStore.name;
	}

	function resetForm() {
		$AddPlayerFormStore = structuredClone(ADD_PLAYER_FORM);
	}
</script>

<div class="header-line">
	<h2>Add Player</h2>
	<ResetButton onClick={resetForm} label="Reset" />
</div>

<form method="POST" action="?/createPlayer" use:enhance>
	<div class="column-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="text"
				name="name"
				placeholder="Player's name"
				required={true}
				isFirst={true}
				bind:stringVal={$AddPlayerFormStore.name}
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="text"
				name="telegramAlias"
				bind:stringVal={$AddPlayerFormStore.telegramAlias}
				placeholder="Player's alias"
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
				defaultNumValue={$AddPlayerFormStore.initialRating}
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<div class="last-box full-width margin-top">
			<Button dark={false} disabled={isSubmissionDisabled} type={'submit'}>Add player</Button>
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
