<script lang="ts">
	import type { Player } from '$lib/types/types';

	export let numberOfGames: number;
	export let places: Player[] | [] = [];
	export let numberOfParticipants: number;
</script>

<div class="info-blocks-panel">
	<div class="info-block">
		<div class="name">Games played</div>
		<div class="value">{numberOfGames}</div>
		<div class="description">in total</div>
	</div>
	{#if places.length}
		<div class="info-block places">
			<div class="name">Places</div>
			{#each places as place, i}
				<div class="place-line" class:hidden={i > 2}>
					<div class="place">{i + 1}</div>
					<div class="player">{place.name}</div>
				</div>
			{/each}
			<div class="footer" />
		</div>
	{/if}
	<div class="info-block">
		<div class="name">Number of participants</div>
		<div class="value">{numberOfParticipants}</div>
		<div class="description">in total</div>
	</div>
</div>

<style>
	.info-blocks-panel {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		box-sizing: border-box;
		width: 100%;
		max-width: 75rem;
		height: 100%;
		padding: 3% 3% 3% 3%;
		margin: auto;
		margin-top: -5%;
		gap: 1.5rem;
	}
	.info-block {
		background-color: var(--main-color);
		border: 0px solid black;
		box-shadow: 0px 0px 4px 2px rgba(0, 0, 0, 0.25);
		border-radius: 20px;
		/* width: 32%; */
		width: 100%;
		min-height: 9rem;
		height: 13rem;
		box-sizing: border-box;
		display: flex;
		flex-direction: column;
		justify-content: center;
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
	.place-line {
		display: flex;
		justify-content: left;
		gap: 1rem;
		align-items: center;
	}
	.place {
		margin-left: 1rem;
		width: 2rem;
		font-size: 3rem;
		font-weight: var(--fontweight-1);
		color: var(--secondary-color);
		letter-spacing: 0.15625rem;
	}
	.player {
		font-size: var(--fontsize-large);
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
	.footer {
		margin-bottom: 1rem;
	}
	.hidden {
		display: none;
	}
	@media (max-width: 800px) and (min-width: 480px) {
		.name {
			font-size: var(--fontsize-medium);
		}
		.value {
			font-size: 2.5rem;
		}
		.description {
			font-size: var(--fontsize-medium);
		}
		.place-line {
			display: block;
			text-align: center;
		}
		.place {
			margin: 0;
			width: 100%;
			font-size: 2rem;
			font-weight: var(--fontweight-2);
		}
		.player {
			font-size: 1rem;
			font-weight: var(--fontweight-2);
		}
		.hidden {
			display: none;
		}
	}
	@media (max-width: 480px) {
		.info-block {
			margin: 0 auto;
			height: 10rem;
			width: 10rem;
		}
		.name {
			font-size: var(--fontsize-medium);
		}
		.places .name {
			height: 2rem;
		}
		.value {
			font-size: 2.5rem;
		}
		.description {
			font-size: var(--fontsize-medium);
		}
		.place-line {
			display: block;
			text-align: center;
		}
		.place {
			margin: 0;
			width: 100%;
			font-size: var(--fontsize-medium);
			font-weight: var(--fontweight-2);
		}
		.player {
			font-size: var(--fontsize-medium);
			font-weight: var(--fontweight-2);
		}
		.hidden {
			display: none;
		}
	}
</style>
