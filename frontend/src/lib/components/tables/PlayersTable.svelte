<script lang="ts">
	import Pagination from '$lib/components/base/pagination/Pagination.svelte';
	import type { Player } from '$lib/types/types';

	import { isLeader } from '$lib/client/stores/stores';
	import { enhance } from '$app/forms';
	import { alertPopup } from '$lib/client/popup/popup.handler';
	import PlayersTableRow from '$lib/components/tables/PlayersTableRow.svelte';

	export let players: Player[] = [];
	export let totalPages: number;
	export let chosenId = -1;
	export let isChoosing = false;
	export let editData: Player;
	export let mode: string;

	let currentPageNumber: number;
	let currentPageSize: number;
</script>

{#if players.length}
	<Pagination lastPageNumber={totalPages} bind:currentPageNumber bind:currentPageSize>
		<div class="scroll">
			<section class="games-list">
				<div class="table-header" class:not-leader={!$isLeader}>
					<span>#</span>
					<span>Name</span>
					<span>Telegram Alias</span>
					<span>W/L</span>
					<span>Rating</span>
					<span />
				</div>

				{#each players as player, i}
					{#if mode === 'delete'}
						<form
							method="POST"
							action="?/deletePlayer"
							use:enhance={async ({ cancel }) => {
								let isConfirmed = await alertPopup(
									'Are you sure that you want to delete this player?',
								);
								if (!isConfirmed) {
									isChoosing = true;
									chosenId = -1;
									cancel();
								}
							}}
						>
							<input type="hidden" name="playerId" value={player.id} />
							<PlayersTableRow
								{player}
								bind:chosenId
								bind:editData
								{isChoosing}
								{mode}
								position={(currentPageNumber - 1) * currentPageSize + i + 1}
							/>
						</form>
					{:else}
						<PlayersTableRow
							{player}
							bind:chosenId
							bind:editData
							{isChoosing}
							{mode}
							position={(currentPageNumber - 1) * currentPageSize + i + 1}
						/>
					{/if}
				{/each}
			</section>
		</div></Pagination
	>
{:else}
	<p class="details">Oops! There is not a single entity satisfying the query</p>
{/if}

<style>
	.details {
		font-size: var(--fontsize-medium1);
		margin: 2rem;
	}
	.games-list {
		max-width: 900px;
		min-width: 700px;

		height: 30.5rem;
		margin-top: 1rem;
		font-size: var(--fontsize-medium1);
		overflow-y: scroll;
	}
	.games-list::-webkit-scrollbar {
		display: none;
	}
	.scroll::-webkit-scrollbar {
		display: none;
	}
	.scroll {
		overflow-x: scroll;
	}

	.table-header {
		display: grid;
		grid-template-columns: 1.8rem 1fr 1fr 6.2rem 4.8rem;
		font-weight: var(--fontweight-1);
		gap: 1rem 1rem;
	}

	.table-header span {
		color: var(--content-color);
		font-size: var(--fontsize-large);
	}

	.table-header.not-leader {
		grid-template-columns: 1.8rem 1fr 1fr 6rem 4.6rem;
	}
</style>
