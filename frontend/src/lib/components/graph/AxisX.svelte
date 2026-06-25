<script lang="ts">
	import type * as d3 from 'd3';

	export let height: number;
	export let xScale: d3.ScaleLinear<number, number>;
	export let margin: { top: number; right: number; bottom: number; left: number };
	export let tournamentsData: Tournament[];

	type Tournament = {
		name: string;
		rating: number;
		date: number;
		delta: number;
	};

	function formatDate(unix: number): string {
		const date = new Date(unix * 1000);
		const month = (date.getMonth() + 1).toString().padStart(2, '0');
		const day = date.getDate().toString().padStart(2, '0');
		return `${day}.${month}`;
	}

	let ticks: number[] = [];
	let xTicks: number[] = [];
	tournamentsData.map((elem) => {
		ticks.push(elem.date);
	});
	xTicks = ticks;
</script>

<g>
	{#each xTicks as tick}
		<text x={xScale(tick)} y={height - margin.bottom} dy="0" dominant-baseline="hanging"
			>{formatDate(tick)}</text
		>
		<line
			x1={xScale(tick)}
			y1={0}
			x2={xScale(tick)}
			y2={height}
			stroke={tick == 0 ? 'black' : '#cecece'}
		/>
	{/each}
</g>

<style>
	text {
		font-size: 1.2rem;
		fill: var(--content-color);
	}
	@media (max-width: 800px) and (min-width: 480px) {
		text {
			font-size: var(--fontsize-medium1);
		}
	}
	@media (max-width: 480px) {
		text {
			font-size: var(--fontsize-medium2);
		}
	}
</style>
