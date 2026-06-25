<script lang="ts">
	import Pagination from '$lib/components/base/pagination/Pagination.svelte';
	import { TournamentFilterMatchFormStore } from '$lib/client/stores/stores.tournaments';
	import type { Match } from '$lib/types/types';

	export let matches: Match[];
	export const handleInsert = () => {
		filter();
	};

	let sortedMatch: Match[] = [];

	function filter() {
		let name = $TournamentFilterMatchFormStore.name;
		let score = $TournamentFilterMatchFormStore.score;
		let firstScore = Number(score.at(0));
		let secondScore = Number(score.at(2));

		sortedMatch = [];
		for (let i = 0; i < matches.length; i++) {
			if (name === '' && score === '') {
				sortedMatch.push(matches[i]);
			} else if (name === '') {
				if (
					(matches[i].firstPlayerScore === firstScore &&
						matches[i].secondPlayerScore === secondScore) ||
					(matches[i].firstPlayerScore === secondScore &&
						matches[i].secondPlayerScore === firstScore)
				) {
					sortedMatch.push(matches[i]);
				}
			} else if (score === '') {
				if (
					!(
						!matches[i].firstPlayerName.includes(name) && !matches[i].firstPlayerName.includes(name)
					)
				) {
					sortedMatch.push(matches[i]);
				}
			} else {
				if (
					((matches[i].firstPlayerScore === firstScore &&
						matches[i].secondPlayerScore === secondScore) ||
						(matches[i].firstPlayerScore === secondScore &&
							matches[i].secondPlayerScore === firstScore)) &&
					!(
						!matches[i].firstPlayerName.includes(name) && !matches[i].firstPlayerName.includes(name)
					)
				) {
					sortedMatch.push(matches[i]);
				}
			}
		}
	}
</script>

{#await handleInsert() then}
	<Pagination isTopHidden={true}>
		<div class="scroll">
			<section class="games-list">
				<div class="table-header">
					<span>First Player</span>
					<span>Second Player</span>
					<span>Score</span>
					<!-- <span>Additional Info</span> -->
					<span />
				</div>

				{#each sortedMatch as match}
					<button class="match-line">
						<div class="matches-grid">
							<div class="no-wrap content">
								{match.firstPlayerName}
								<span class="rating">
									{#if match.firstPlayerRatingDelta}
										({match.firstPlayerRatingBefore})
										{#if match.firstPlayerRatingDelta > 0}
											<span class="positive">+{match.firstPlayerRatingDelta}</span>
										{:else}
											<span class="negative">{match.firstPlayerRatingDelta}</span>
										{/if}
									{/if}
								</span>
							</div>
							<div class="no-wrap content">
								{match.secondPlayerName}
								<span class="rating">
									{#if match.secondPlayerRatingDelta}
										({match.secondPlayerRatingBefore})
										{#if match.secondPlayerRatingDelta > 0}
											<span class="positive">+{match.secondPlayerRatingDelta}</span>
										{:else}
											<span class="negative">{match.secondPlayerRatingDelta}</span>
										{/if}
									{/if}
								</span>
							</div>
							<div class="score content">
								{match.firstPlayerScore}
								:
								{match.secondPlayerScore}
							</div>
						</div>
					</button>
				{/each}
			</section>
		</div>
	</Pagination>
{/await}

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
	.matches-grid {
		display: grid;
		grid-template-columns: 1fr 1fr 2rem auto;
		gap: 1rem 1rem;
		color: var(--content-color);
		height: 1.1em;
	}
	.match-line {
		background: none;
		border: none;
		cursor: pointer;
		padding: 0;
		border: none;
		width: 100%;
		height: 1.3rem;
		margin-bottom: 0.2rem;
	}
	.match-line:disabled {
		cursor: default;
	}

	.match-line:enabled:hover {
		background-color: var(--secondary-bg-color);
		border-radius: 3px;
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
		grid-template-columns: 1fr 1fr 4rem;
		gap: 1rem 1rem;
	}
	.table-header span {
		color: var(--content-color);
		font-size: var(--fontsize-large);
		font-weight: var(--fontweight-1);
	}

	.rating .positive {
		color: var(--rating-positive-color);
	}
	.rating .negative {
		color: var(--rating-negative-color);
	}
</style>
