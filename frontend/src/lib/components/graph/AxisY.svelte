<script lang="ts">
	import type * as d3 from 'd3';

	export let width: number;
	export let yScale: d3.ScaleLinear<number, number>;
	export let margin: { top: number; right: number; bottom: number; left: number };
	export let tournamentsData: Tournament[];

	type Tournament = {
		name: string;
		rating: number;
		date: number;
		delta: number;
	};

	let ticks: number[] = [];
	let yTicks: number[] = [];
	let maxRating = 0;
	let minRating = Number.MAX_SAFE_INTEGER;
	tournamentsData.map((elem) => {
		if (elem.rating < minRating) {
			minRating = elem.rating;
		}
		if (elem.rating > maxRating) {
			maxRating = elem.rating;
		}
	});
	let upperBound = (Math.floor(Math.floor(maxRating) / 10) + 1) * 10;
	let lowerBound = (minRating / 10 - 1) * 10;
	const coef =
		Math.round((maxRating - minRating) / 100) * 10 > 10
			? Math.round((maxRating - minRating) / 100) * 10
			: 10;
	while (upperBound >= lowerBound) {
		ticks.push(upperBound);
		upperBound -= coef;
	}
	yTicks = ticks;
</script>

<g transform={`translate(${margin.left} ${margin.top})`}>
	{#each yTicks as tick}
		<text class="tick-text" x={0} y={yScale(tick)} dy={-5} dx={5}
			>{tick} {tick == (maxRating / 10 + 1) * 10 ? ' Rating' : ''}</text
		>
		<line x1={0} y1={yScale(tick)} x2={width} y2={yScale(tick)} stroke={'#cecece'} />
	{/each}
</g>

<style>
	.tick-text {
		font-size: 1.2rem;
		fill: var(--content-color);
	}
	@media (max-width: 800px) and (min-width: 480px) {
		.tick-text {
			font-size: var(--fontsize-medium1);
		}
	}
	@media (max-width: 480px) {
		.tick-text {
			font-size: var(--fontsize-medium2);
		}
	}
</style>
