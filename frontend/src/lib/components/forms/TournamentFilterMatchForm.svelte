<script lang="ts">
	// import { enhance } from '$app/forms';
	import { TournamentFilterMatchFormStore } from '$lib/client/stores/stores.tournaments';
	import Button from '$lib/components/base/Button.svelte';

	import { createEventDispatcher } from 'svelte';
	import ResetButton from '$lib/components/base/ResetButton.svelte';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';

	const dispatch = createEventDispatcher();

	let name = $TournamentFilterMatchFormStore.name;
	let score = $TournamentFilterMatchFormStore.score;

	const sortMatch = () => {
		dispatch('update');
	};

	const saveForm = function () {
		score = score.replace(/\s/g, '');
		$TournamentFilterMatchFormStore = {
			name: name,
			score: score,
		};
	};

	const resetForm = function () {
		name = '';
		score = '';
		saveForm();
	};
</script>

<div class="line-2-elems">
	<h2>Filters</h2>
	<ResetButton onClick={resetForm} label="Reset" />
</div>

<form on:submit|preventDefault={sortMatch} on:change={saveForm} class="filters">
	<div class="column-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="text"
				name="name"
				bind:stringVal={name}
				bind:defaultValue={name}
				placeholder="Search by player"
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="text"
				name="score"
				bind:stringVal={score}
				bind:defaultValue={score}
				placeholder="Search by score"
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<div class="last-box margin-top">
			<Button dark={false} disabled={false} type={'submit'}>Search</Button>
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
	.filters {
		margin: 0 auto 1em;
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
		gap: 0.44rem;
		align-items: end;
	}
	.last-box {
		grid-column: 2;
	}

	.margin-top {
		margin-top: 1em;
	}
</style>
