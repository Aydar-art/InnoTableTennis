<script lang="ts">
	import { enhance } from '$app/forms';

	import MatchHeader from '$lib/components/tables/MatchHeader.svelte';
	import Pagination from '$lib/components/base/pagination/Pagination.svelte';
	import type { Match } from '$lib/types/types';

	import { isLeader } from '$lib/client/stores/stores';
	import { alertPopup } from '$lib/client/popup/popup.handler';
	import MatchesTableRow from '$lib/components/tables/MatchesTableRow.svelte';

	export let chosenId = -1;
	export let isChoosing = false;
	export let mode = 'add';
	export let editData: Match;
	export let matches: Match[] = [];
	export let totalPages = 0;

	let oneTournament = true;
</script>

{#if matches.length}
	<Pagination lastPageNumber={totalPages}>
		<div class="scroll">
			<section class="games-list">
				<div class="table-header" class:not-leader={!$isLeader}>
					<span>First Player</span>
					<span>Second Player</span>
					<span>Score</span>
					<span />
				</div>
				{#if oneTournament}
					<MatchHeader title={matches[0].tournamentTitle} isMain={true} />
					<MatchHeader title={matches[0].localDateString} />
				{/if}

				{#each matches as match, i}
					{#if i != 0 && matches[i].tournamentTitle != matches[i - 1].tournamentTitle}
						<MatchHeader title={matches[i].tournamentTitle} isMain={true} />
						<MatchHeader title={matches[i].localDateString} />
					{:else if i != 0 && matches[i].localDateString != matches[i - 1].localDateString}
						<MatchHeader title={matches[i].localDateString} />
					{/if}
					{#if mode === 'delete'}
						<form
							method="POST"
							action="?/deleteMatch"
							use:enhance={async ({ cancel }) => {
								let isConfirmed = await alertPopup(
									'Are you sure that you want to delete this match?',
								);
								if (!isConfirmed) {
									isChoosing = true;
									chosenId = -1;
									cancel();
								}
							}}
						>
							<input type="hidden" name="matchId" value={match.id} />
							<MatchesTableRow {match} bind:chosenId bind:editData {isChoosing} {mode} />
						</form>
					{:else}
						<MatchesTableRow {match} bind:chosenId bind:editData {isChoosing} {mode} />
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
		grid-template-columns: 1fr 1fr 4rem;
		gap: 1rem 1rem;
	}
	.table-header span {
		color: var(--content-color);
		font-size: var(--fontsize-large);
		font-weight: var(--fontweight-1);
	}
</style>
