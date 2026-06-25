<script lang="ts">
	import * as d3 from 'd3';

	import AxisX from '$lib/components/graph/AxisX.svelte';
	import AxisY from '$lib/components/graph/AxisY.svelte';
	import Tooltip from '$lib/components/graph/Tooltip.svelte';
	import type { RatingHistoryItem } from '$lib/types/types.profile';

	export let Data: RatingHistoryItem[];

	const margin = { top: 10, right: 20, left: 0, bottom: 20 };

	const unixMonth = 2629743;

	function parseDateToUnix(dateString: string): number {
		const [day, month, year] = dateString.split('.');
		const date = new Date(`${year}-${month}-${day}`);
		return Math.floor(date.getTime() / 1000);
	}

	let tournamentsData: Tournament[] = [];

	Data.forEach((elem) => {
		let ratingEvent: Tournament = {
			name: elem.tournamentTitle,
			rating: elem.rating,
			date: parseDateToUnix(elem.date),
			delta: elem.delta,
		};
		if (ratingEvent.date > new Date().getUTCDate() - unixMonth * 2) {
			tournamentsData.push(ratingEvent);
		}
	});

	let width = 900;
	let height = 400;

	type Tournament = {
		name: string;
		rating: number;
		date: number;
		delta: number;
	};
	let startDomain: number = Number.MAX_SAFE_INTEGER;
	let endDomain = 0;
	let startLineDomain: number = Number.MAX_SAFE_INTEGER;
	let endLineDomain = 0;
	tournamentsData.map((elem) => {
		if (elem.rating < startDomain) {
			startDomain = elem.rating;
		}
		if (elem.rating > endDomain) {
			endDomain = elem.rating;
		}
		if (elem.date < startLineDomain) {
			startLineDomain = elem.date;
		}
		if (elem.date > endLineDomain) {
			endLineDomain = elem.date;
		}
	});
	const yScale = d3
		.scaleLinear()
		.domain([startDomain * 0.9, endDomain * 1.1])
		.range([height - margin.left - margin.right, 0]);
	$: xScale = d3
		.scaleLinear()
		.domain([
			startLineDomain - (endLineDomain - startLineDomain) * 0.3,
			endLineDomain + (endLineDomain - startLineDomain) * 0.3,
		])
		.range([0, width - margin.top - margin.bottom]);

	let hoveredData: Tournament | null = null;
</script>

<div class="graph">
	<div class="graph-wrapper">
		<h1>Rating history</h1>
		<div
			class="chart-container"
			bind:clientWidth={width}
			on:mouseleave={() => {
				hoveredData = null;
			}}
			role="button"
			tabindex="0"
		>
			<svg {width} {height}>
				<AxisX {height} {xScale} {margin} {tournamentsData} />
				<AxisY {width} {yScale} {margin} {tournamentsData} />
				<g class="circles" transform={`translate(${margin.left} ${margin.top})`}>
					{#each tournamentsData.sort((a, b) => a.date.valueOf() - b.date.valueOf()) as student, i}
						<circle
							cx={xScale(student.date)}
							cy={yScale(student.rating)}
							r={hoveredData && hoveredData == student ? '12' : '10'}
							opacity={hoveredData ? (hoveredData == student ? '1' : '3') : '1'}
							on:mouseover={() => {
								hoveredData = student;
							}}
							on:focus={() => {
								hoveredData = student;
							}}
							role="button"
							aria-label="student data"
							on:mouseleave={() => {
								hoveredData = null;
							}}
							tabindex="0"
						/>
						{#if i > 0}
							<line
								x1={xScale(tournamentsData[i - 1].date)}
								y1={yScale(tournamentsData[i - 1].rating)}
								x2={xScale(student.date)}
								y2={yScale(student.rating)}
								stroke-width="1"
							/>
						{/if}
					{/each}
				</g>
			</svg>
			{#if hoveredData}
				<Tooltip tournament={hoveredData} {xScale} {yScale} />
			{/if}
		</div>
	</div>
</div>

<style>
	circle {
		transition: r 300ms ease, opacity 300ms ease;
		cursor: pointer;
		fill: var(--title-color);
	}

	circle:focus {
		outline: none;
	}
	line {
		stroke: var(--secondary-color);
	}

	h1 {
		font-size: var(--fontsize-x-large);
		font-weight: 700;
		margin-bottom: 0.5rem;
	}
	.graph {
		width: 100%;
		box-sizing: border-box;
		padding: 0% 3% 3% 3%;
		height: 100%;
	}
	.graph-wrapper {
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
		border-radius: 20px;
		padding: 5%;
		box-sizing: border-box;
		box-shadow: 0px 0px 4px 2px rgba(0, 0, 0, 0.25);
	}

	.chart-container {
		position: relative;
	}

	g {
		width: 100%;
		height: 100%;
	}
</style>
