<script lang="ts">
	import MatchHeader from '$lib/components/tables/MatchHeader.svelte';
	import ProfilePagination from '$lib/components/profile/ProfilePagination.svelte';
	import type { ProfileMatch } from '$lib/types/types.profile';

	import { isLeader } from '$lib/client/stores/stores';

	export let dataMatches: ProfileMatch[];
</script>

{#if dataMatches.length}
	<ProfilePagination>
		<div class="scroll">
			<section class="games-list">
				<div class="table-header" class:not-leader={!$isLeader}>
					<span>Opponent</span>
					<span>Rating</span>
					<span>Score</span>
					<span />
				</div>
				<MatchHeader title={dataMatches[0].tournamentTitle} isMain={true} />
				<MatchHeader title={dataMatches[0].date} />

				{#each dataMatches as match, i}
					{#if i != 0 && dataMatches[i].tournamentTitle != dataMatches[i - 1].tournamentTitle}
						<MatchHeader title={dataMatches[i].tournamentTitle} isMain={true} />
						<MatchHeader title={dataMatches[i].date} />
					{:else if i != 0 && dataMatches[i].date != dataMatches[i - 1].date}
						<MatchHeader title={dataMatches[i].date} />
					{/if}
					<div class="matches-grid">
						<div class="no-wrap content">
							{match.opponentName}
							<span class="rating">
								{#if match.opponentRatingBefore}
									({match.opponentRatingBefore})
								{/if}
							</span>
						</div>
						<div class="no-wrap content">
							{#if match.delta > 0}
								<span class="positive">+{match.delta}</span>
							{:else if match.delta < 0}
								<span class="negative">{match.delta}</span>
							{:else}
								<span class="zero">{match.delta}</span>
							{/if}
						</div>
						<div class="score content">
							{match.score}
						</div>
					</div>
				{/each}
			</section>
		</div></ProfilePagination
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
	.matches-grid {
		display: grid;
		grid-template-columns: 3fr 1fr 2rem auto;
		gap: 1rem 1rem;
		color: var(--content-color);
		height: 1.1em;
	}

	.score {
		white-space: nowrap;
	}

	.no-wrap {
		display: flex;
		align-items: center;
		text-align: left;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}
	.content {
		font-size: var(--fontsize-medium1);
	}
	.table-header {
		display: grid;
		grid-template-columns: 3fr 1fr 4rem;
		gap: 1rem 1rem;
	}
	.table-header span {
		color: var(--content-color);
		font-size: var(--fontsize-large);
		font-weight: var(--fontweight-1);
	}
	.positive {
		color: var(--rating-positive-color);
	}
	.negative {
		color: var(--rating-negative-color);
	}
</style>
