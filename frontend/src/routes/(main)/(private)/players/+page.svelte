<script lang="ts">
	import AddPlayerForm from '$lib/components/forms/AddPlayerForm.svelte';
	import PlayersTable from '$lib/components/tables/PlayersTable.svelte';
	import SortFilterPlayerForm from '$lib/components/forms/SortFilterPlayerForm.svelte';
	import type { Player } from '$lib/types/types';
	import ToggleCheckboxButton from '$lib/components/base/ToggleCheckboxButton.svelte';
	import EditSwitchBar from '$lib/components/navigation/EditSwitchBar.svelte';
	import EditPlayerForm from '$lib/components/forms/EditPlayerForm.svelte';
	import { isLeader } from '$lib/client/stores/stores.js';
	import { handleError } from '$lib/client/handleError.js';

	export let data;
	export let form;

	$: if (form?.error) {
		handleError(form.error);
	}
	$: if (data.error) {
		handleError(data.error);
	}

	let editData: Player = {} as Player;
	let isEditing = false;
	let chosenId = -1;
	let mode = 'view';
	$: isChoosing = ((mode === 'edit' || mode === 'delete') && isEditing) || mode === 'view';
</script>

<svelte:head>
	<meta
		name="description"
		content="Discover the talented members of InnoTableTennis Club here. Check out their ratings, aliases, and win/loss records. Explore the achievements and skills of our dedicated players."
	/>
	<meta
		name="keywords"
		content="table tennis club members, InnoTableTennis Club, player ratings, player aliases, win/loss records, talented players"
	/>
</svelte:head>

<div class="form-list-layout">
	{#if isEditing}
		{#if mode === 'add'}
			<div class="form">
				<AddPlayerForm />
			</div>
		{:else if mode === 'edit'}
			{#if chosenId === -1}
				Please choose a player to edit
			{:else}
				<div class="form">
					<EditPlayerForm player={editData} bind:chosenId />
				</div>
			{/if}
		{:else if mode === 'delete'}
			Please choose a player to delete
		{/if}
	{:else}
		<div class="form">
			<SortFilterPlayerForm />
		</div>
	{/if}

	<div class="players-list">
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
		<PlayersTable
			bind:mode
			bind:isChoosing
			bind:chosenId
			bind:editData
			players={data.players}
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
	.players-list {
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
		}
		.players-list {
			margin: 0 auto;
			overflow-x: hidden;
		}
	}
</style>
