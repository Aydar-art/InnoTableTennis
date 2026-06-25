<script lang="ts">
	import Pagination from '$lib/components/base/pagination/Pagination.svelte';
	import type { Tournament } from '$lib/types/types';

	import TournamentTableRow from '$lib/components/tables/TournamentTableRow.svelte';
	import { alertPopup } from '$lib/client/popup/popup.handler';
	import { isLeader } from '$lib/client/stores/stores';
	import { enhance } from '$app/forms';

	export let tournaments: Tournament[] = [];
	export let totalPages: number;
	export let chosenId = -1;
	export let isChoosing = false;
	export let editData: Tournament;
	export let mode: string;
</script>

{#if tournaments.length}
	<Pagination lastPageNumber={totalPages}>
		<div class="scroll">
			<section class="games-list">
				<div class="table-header" class:not-leader={!$isLeader}>
					<span>Title</span>
					<span>Dates</span>
					<span>Kf</span>
					<span>Players</span>
					<span />
				</div>

				{#each tournaments as tournament}
					{#if mode === 'delete'}
						<form
							method="POST"
							action="?/deleteTournament"
							use:enhance={async ({ cancel }) => {
								let isConfirmed = await alertPopup(
									'Are you sure that you want to delete this tournament?',
								);
								if (!isConfirmed) {
									isChoosing = true;
									chosenId = -1;
									cancel();
								}
							}}
						>
							<input type="hidden" name="tournamentId" value={tournament.id} />
							<TournamentTableRow {tournament} bind:chosenId bind:editData {isChoosing} {mode} />
						</form>
					{:else}
						<TournamentTableRow {tournament} bind:chosenId bind:editData {isChoosing} {mode} />
					{/if}
				{/each}
			</section>
		</div></Pagination
	>
{:else}
	<p class="details">Oops! There is not a single entity satisfying the query</p>
{/if}

<style>
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
		grid-template-columns: 1fr 1fr 6.5rem 5rem;
		gap: 1rem 1rem;
	}
	.table-header span {
		color: var(--content-color);
		font-size: var(--fontsize-large);
		font-weight: var(--fontweight-1);
	}

	.details {
		font-size: var(--fontsize-medium1);
		margin: 2rem;
	}

	.table-header.not-leader {
		grid-template-columns: 1fr 1fr 6.5rem 5rem;
		padding: 0;
	}
</style>
