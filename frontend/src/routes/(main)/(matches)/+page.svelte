<script lang="ts">
	import * as db from '$lib/client/requests.js';

	import AddMatchForm from '$lib/components/forms/AddMatchForm.svelte';
	import MatchesTable from '$lib/components/tables/MatchesTable.svelte';
	import ToggleCheckboxButton from '$lib/components/base/ToggleCheckboxButton.svelte';
	import SortFilterMatchForm from '$lib/components/forms/SortFilterMatchForm.svelte';
	import EditSwitchBar from '$lib/components/navigation/EditSwitchBar.svelte';
	import EditMatchForm from '$lib/components/forms/EditMatchForm.svelte';
	import type { Match } from '$lib/types/types';
	import { isLeader } from '$lib/client/stores/stores.js';
	import { handleError } from '$lib/client/handleError.js';

	export let form;
	export let data;

	$: if (form?.error) {
		handleError(form.error);
	}
	$: if (data.error) {
		handleError(data.error);
	}

	let editData: Match = {} as Match;
	let isEditing = false;
	let chosenId = -1;
	let mode = 'add';
	$: isChoosing = (mode === 'edit' || mode === 'delete') && isEditing;

	async function getFormData() {
		const playersPromise = db.getAllPlayers();
		const tournamentsPromise = db.getAllTournaments();
		const [players, tournaments] = await Promise.all([playersPromise, tournamentsPromise]);
		return { players, tournaments };
	}
</script>

<svelte:head>
	<meta
		name="description"
		content="Explore the latest table tennis matches of InnoTableTennis Club members here. Stay updated with thrilling competitions, results, and player performances."
	/>
	<meta
		name="keywords"
		content="table tennis matches, InnoTableTennis Club, table tennis competitions, player performances, match results"
	/>
</svelte:head>

<div class="form-list-layout">
	<div class="form">
		{#if isEditing}
			{#await getFormData() then resp}
				{#if mode === 'add'}
					<AddMatchForm players={resp.players} tournaments={resp.tournaments} />
				{:else if mode === 'edit'}
					{#if chosenId === -1}
						Please choose a match to edit
					{:else}
						<EditMatchForm
							players={resp.players}
							tournaments={resp.tournaments}
							match={editData}
							bind:chosenId
						/>
					{/if}
				{:else if mode === 'delete'}
					Please choose a match to delete
				{/if}
			{/await}
		{:else}
			<SortFilterMatchForm />
		{/if}
	</div>

	<div class="matches-list">
		{#if $isLeader}
			<div class="edit-mode">
				<ToggleCheckboxButton
					bind:checked={isEditing}
					bind:chosenId
					bind:editData
					bind:mode
					label={'Edit Mode'}
				/>
				<span />
			</div>
		{/if}

		{#if isEditing}
			<div class="edit-switch-bar">
				<EditSwitchBar bind:mode bind:chosenId bind:editData />
			</div>
		{/if}
		<MatchesTable
			bind:mode
			bind:isChoosing
			bind:chosenId
			bind:editData
			matches={data.matches}
			totalPages={data.totalPages}
		/>
	</div>
</div>

<style>
	.form-list-layout {
		display: grid;
		grid-auto-flow: column;
		align-items: center;
		grid-template-columns: 1fr 2fr;
	}
	.form {
		max-width: 350px;
		margin-right: 2rem;
	}
	.edit-mode {
		text-align: right;
		margin-top: 2rem;
	}
	.matches-list {
		max-width: 900px;
	}

	@media (max-width: 1100px) {
		.form {
			max-width: 500px;
			margin: 0 auto;
		}
		.form-list-layout {
			display: block;
		}
		.matches-list {
			margin: 0 auto;
		}
	}
</style>
