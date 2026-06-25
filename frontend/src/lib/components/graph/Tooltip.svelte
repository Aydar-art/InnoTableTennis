<script lang="ts">
	import type * as d3 from 'd3';

	export let tournament: {
		name: string;
		rating: number;
		date: number;
		delta: number;
	};
	export let xScale: d3.ScaleLinear<number, number>;
	export let yScale: d3.ScaleLinear<number, number>;

	$: isGreen = tournament.delta >= 0;

	const tooltipStyle = `position: absolute;
    top: ${yScale(tournament.rating)}px;
    left: ${xScale(tournament.date)}px;`;
</script>

<div class="tooltip" style={tooltipStyle}>
	<h1>Rating : {Math.floor(tournament.rating * 100) / 100}</h1>
	<p>{tournament.name}</p>
	<p class={isGreen ? 'p-green' : 'p-red'}>
		{tournament.delta >= 0 ? '+' : '-'}{Math.floor(tournament.delta * 100) / 100}
	</p>
</div>

<style>
	.tooltip {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		padding: 6px;
		background: var(--content-color);
		color: var(--main-color);
		border-radius: 10px;
		pointer-events: none;
		transition: top 300ms ease, left 300ms ease;
	}
	p {
		margin: 0.1rem;
		color: var(--main-color);
	}
	.p-green {
		color: var(--rating-positive-color);
	}
	.p-red {
		color: var(--rating-negative-color);
	}

	h1 {
		color: var(--main-color);
		font-weight: 700;
		margin-bottom: 0.25rem;
	}
</style>
