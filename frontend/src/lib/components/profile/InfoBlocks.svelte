<script lang="ts">
	import { slide } from 'svelte/transition';

	import DownExpandIcon from '$lib/components/icons/DownExpandIcon.svelte';
	import UpCompressIcon from '$lib/components/icons/UpCompressIcon.svelte';
	import type { Stats } from '$lib/types/types.profile';

	export let profileStats: Stats;
	let statsBlockStatus = false;

	const normalizeNumber = (stat: number) => {
		return Math.floor(stat * 100) / 100;
	};

	const toggleStats = () => {
		statsBlockStatus = !statsBlockStatus;
	};
</script>

<div class="wrapper">
	<div class="column">
		<div class="info-blocks-panel">
			<div class="info-block">
				<div class="name">Tournament played</div>
				<div class="value">{profileStats.tournamentsParticipated}</div>
				<div class="description">in total</div>
			</div>
			<div class="info-block">
				<div class="name">Rating</div>
				<div class="value">{profileStats.rating}</div>
				<div class="description">at this moment</div>
			</div>
			<div class="info-block">
				<div class="name">Win/Lose</div>
				<div class="value">{profileStats.matchesWon}/{profileStats.matchesLost}</div>
				<div class="description">in total</div>
			</div>
		</div>
		<div class="advanced-stats">
			<div class="main-stat-block">
				<div class="stat-header">
					<div class="other-stat">Other statistics</div>
				</div>
				<div class="row">
					<div class="stat-subblock">
						<div class="name">Rating position</div>
						<div class="value">{profileStats.ranking}</div>
					</div>
				</div>
				<div class="row">
					<div class="stat-subblock">
						<div class="name">Games played</div>
						<div class="value">{profileStats.matchesPlayed}</div>
					</div>
					<div class="stat-subblock">
						<div class="name">Win persentage</div>
						<div class="value">
							{Math.floor(profileStats.winPercentage * 100)
								? Math.floor(profileStats.winPercentage * 100)
								: 0}%
						</div>
					</div>
				</div>
				{#if statsBlockStatus}
					<div transition:slide>
						<div class="row">
							<div class="stat-subblock">
								<div class="name">Winning streak</div>
								<div class="value">{profileStats.winningStreak}</div>
							</div>
							<div class="stat-subblock">
								<div class="name">Losing streak</div>
								<div class="value">{profileStats.losingStreak}</div>
							</div>
							<div class="stat-subblock">
								<div class="name">Points won</div>
								<div class="value">{profileStats.pointsWon}</div>
							</div>
						</div>
						<div class="row">
							<div class="stat-subblock">
								<div class="name">Points lost</div>
								<div class="value">{profileStats.pointsLost}</div>
							</div>
							<div class="stat-subblock">
								<div class="name">Average match points</div>
								<div class="value">
									{normalizeNumber(profileStats.averageMatchPoints)
										? normalizeNumber(profileStats.averageMatchPoints)
										: 0}
								</div>
							</div>
							<div class="stat-subblock">
								<div class="name">Average match delta</div>
								<div class="value">
									{normalizeNumber(profileStats.averageMatchDelta)
										? normalizeNumber(profileStats.averageMatchDelta)
										: 0}
								</div>
							</div>
						</div>
					</div>
				{/if}
				<div class="expand-button-container">
					<button on:click={toggleStats} class="expand-button">
						{#if !statsBlockStatus}
							Show More
							<div class="button-icon">
								<DownExpandIcon />
							</div>
						{:else}
							Show less
							<div class="button-icon">
								<UpCompressIcon />
							</div>
						{/if}
					</button>
				</div>
			</div>
		</div>
	</div>
</div>

<style>
	.wrapper {
		width: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}
	.column {
		width: 100%;
		height: 100%;
	}
	.info-blocks-panel {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		box-sizing: border-box;
		width: 100%;
		height: 100%;
		padding: 0% 3% 3% 3%;
		margin-top: -5%;
	}
	.advanced-stats {
		box-sizing: border-box;
		padding: 0% 3% 3% 3%;
		height: 100%;
	}
	.main-stat-block {
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
		border-radius: 20px;
		box-shadow: 0px 0px 4px 2px rgba(0, 0, 0, 0.25);
	}
	.stat-header {
		min-height: 3rem;
		display: flex;
		flex-direction: row;
		align-items: flex-start;
		box-sizing: border-box;
		padding: 1rem;
	}
	.other-stat {
		font-size: var(--fontsize-x-large);
		font-weight: 700;
	}
	.row {
		display: flex;
		flex-direction: row;
		justify-content: center;
		margin-bottom: 3rem;
	}
	.stat-subblock {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		min-height: 5rem;
		max-width: 33%;
		width: 30%;
		margin: 0rem 2rem;
	}
	.info-block {
		background-color: var(--main-color);
		border: 0px solid black;
		box-shadow: 0px 0px 4px 2px rgba(0, 0, 0, 0.25);
		border-radius: 20px;
		width: 32%;
		min-height: 9rem;
		box-sizing: border-box;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
	.name {
		text-align: center;
		display: flex;
		flex-direction: column;
		justify-content: center;
		font-size: var(--fontsize-large);
		font-weight: 700;
		width: 100%;
		height: 100%;
	}
	.value {
		text-align: center;
		display: flex;
		flex-direction: column;
		justify-content: center;
		font-size: 3rem;
		width: 100%;
		height: 100%;
	}
	.description {
		text-align: center;
		display: flex;
		flex-direction: column;
		justify-content: center;
		font-weight: 700;
		align-items: center;
		width: 100%;
		height: 100%;
	}
	.expand-button-container {
		display: flex;
		flex-direction: row;
		justify-content: center;
		margin: 1rem;
	}
	.expand-button {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		border: 0px;
		background: var(--main-color);
		font-size: var(--fontsize-medium);
		color: var(--content-color);
		font-weight: 700;
	}
	.button-icon {
		width: 3rem;
		height: 3rem;
	}
	@media (max-width: 800px) and (min-width: 480px) {
		.value {
			font-size: 2.5rem;
		}
		.description {
			font-size: var(--fontsize-medium);
		}
		.row {
			margin-bottom: 2rem;
		}
		.stat-subblock {
			margin: 0rem 1rem;
		}
	}
	@media (max-width: 480px) {
		.name {
			font-size: var(--fontsize-medium);
		}
		.value {
			font-size: 2.2rem;
		}
		.description {
			font-size: var(--fontsize-medium);
		}
		.row {
			margin-bottom: 1rem;
		}
		.stat-subblock {
			margin: 0rem 0.5rem;
		}
	}
</style>
