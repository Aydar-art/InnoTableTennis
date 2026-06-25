<script lang="ts">
	import type { Match } from '$lib/types/types';

	export let match: Match;
	export let chosenId: number;
	export let editData;
	export let isChoosing: boolean;
	export let mode: string;

	$: selected = chosenId === match.id;

	async function handleClick(event: MouseEvent) {
		chosenId = match.id;
		if (mode == 'edit') editData = match;
	}
</script>

<button class="match-line" class:selected on:click={handleClick} disabled={!isChoosing || selected}>
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

<style>
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
		padding-bottom: 0.2rem;
		border: none;
		width: 100%;
		height: 1.3rem;
		box-sizing: border-box;
	}

	.match-line:disabled {
		cursor: default;
	}

	.match-line:enabled:hover {
		background-color: var(--secondary-bg-color);
		border-radius: 3px;
	}

	.selected,
	.match-line:enabled:focus {
		background-color: var(--secondary-color);
		border-radius: 3px;
	}

	.selected .no-wrap,
	.match-line:focus .no-wrap {
		color: var(--main-color);
	}

	.selected .score,
	.match-line:focus .score {
		color: var(--main-color);
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
	.rating .positive {
		color: var(--rating-positive-color);
	}
	.rating .negative {
		color: var(--rating-negative-color);
	}
</style>
