<script lang="ts">
	import AddTournamentForm from '$lib/components/forms/AddTournamentForm.svelte';
	import TournamentTable from '$lib/components/tables/TournamentTable.svelte';
	import ToggleCheckboxButton from '$lib/components/base/ToggleCheckboxButton.svelte';
	import SortFilterTournamentForm from '$lib/components/forms/SortFilterTournamentForm.svelte';

	import type { Tournament } from '$lib/types/types';
	import EditSwitchBar from '$lib/components/navigation/EditSwitchBar.svelte';
	import EditTournamentForm from '$lib/components/forms/EditTournamentForm.svelte';
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

	let editData: Tournament = {} as Tournament;
	let isEditing = false;
	let chosenId = -1;
	let mode = '';
	$: isChoosing = mode === 'edit' || mode === 'delete' || mode === 'create' || mode === 'view';
	$: if (!isEditing) {
		mode = $isLeader ? 'create' : 'view';
	}
</script>

<svelte:head>
	<meta
		name="description"
		content="Explore all table tennis tournaments at InnoTableTennis Club. Check the date coefficients, number of participants, and use filters and sorting options to find the tournaments that interest you the most. Stay updated with our exciting tournament events."
	/>
	<meta
		name="keywords"
		content="table tennis tournaments, InnoTableTennis Club, date coefficients, number of participants, tournament filters, tournament sorting, exciting events"
	/>
</svelte:head>

<div class="form-list-layout">
	{#if isEditing}
		{#if mode === 'add'}
			<div class="form">
				<AddTournamentForm />
			</div>
		{:else if mode === 'edit'}
			{#if chosenId === -1}
				Please choose a tournament to edit
			{:else}
				<div class="form">
					<EditTournamentForm tournament={editData} bind:chosenId />
				</div>
			{/if}
		{:else if mode === 'delete'}
			Please choose a tournament to delete
		{/if}
	{:else}
		<div class="form">
			<SortFilterTournamentForm />
		</div>
	{/if}
	<div class="tournaments-list">
		{#if isLeader}
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
		<TournamentTable
			bind:mode
			bind:isChoosing
			bind:chosenId
			bind:editData
			tournaments={data.tournaments}
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
	.tournaments-list {
		max-width: 900px;
	}
	.edit-mode {
		text-align: right;
		margin-top: 2rem;
	}

	@media (max-width: 1100px) {
		.form {
			max-width: 500px;
			margin: 0 auto;
		}
		.form-list-layout {
			display: block;
			margin: 0;
		}
		.tournaments-list {
			margin: 0 auto;
		}
	}
</style>
