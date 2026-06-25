<script lang="ts">
	import { createEventDispatcher } from 'svelte';
	import LeftArrow from '$lib/components/icons/LeftArrow.svelte';
	import RightArrow from '$lib/components/icons/RightArrow.svelte';

	const dispatch = createEventDispatcher();

	let firstPageNumber = 1;
	let middlePageNumber: number;
	let visiblePages: number[];
	let currentPageIndex = 0;

	let sizes = [10, 20, 50, 100, 200];

	export let isTop = true;
	export let currentPageNumber = firstPageNumber;
	export let currentPageSize = 200;
	export let lastPageNumber = 100;
	export let isTopHidden = false;

	$: isSmallLeft = currentPageNumber - firstPageNumber < 2;
	$: isSmallRight = lastPageNumber - currentPageNumber < 2;

	$: currentPageNumber = currentPageNumber < lastPageNumber ? currentPageNumber : lastPageNumber;

	$: {
		if (lastPageNumber - firstPageNumber >= 2)
			visiblePages = [middlePageNumber - 1, middlePageNumber, middlePageNumber + 1];
		else {
			if (lastPageNumber - firstPageNumber == 2) visiblePages = [firstPageNumber];
			else if (lastPageNumber - firstPageNumber == 1) {
				visiblePages = [firstPageNumber, lastPageNumber];
			}
		}
	}

	$: {
		if (currentPageNumber == firstPageNumber) {
			middlePageNumber = firstPageNumber + 1;
			currentPageIndex = 0;
		} else if (currentPageNumber == lastPageNumber) {
			middlePageNumber = lastPageNumber - 1;
			currentPageIndex = 2;
		} else {
			middlePageNumber = currentPageNumber;
			currentPageIndex = 1;
		}
		if (lastPageNumber - firstPageNumber >= 2)
			visiblePages = [middlePageNumber - 1, middlePageNumber, middlePageNumber + 1];
		else if (lastPageNumber - firstPageNumber == 0) visiblePages = [firstPageNumber];
		else if (lastPageNumber - firstPageNumber == 1) {
			visiblePages = [firstPageNumber, lastPageNumber];
			currentPageIndex = Number(!(currentPageNumber == firstPageNumber));
		}
	}

	function handleLeftClick() {
		if (currentPageNumber > firstPageNumber) {
			currentPageNumber -= 1;
		}
		dispatch('click');
	}
	function handleRightClick() {
		if (currentPageNumber < lastPageNumber) {
			currentPageNumber += 1;
		}
		dispatch('click');
	}

	function handleClick(event: Event) {
		currentPageNumber = Number((<HTMLInputElement>event.target).value);
		dispatch('click');
	}

	function handleSizeClick(event: Event) {
		currentPageSize = Number((<HTMLInputElement>event.target).value);
		currentPageNumber = firstPageNumber;
		dispatch('click');
	}
</script>

<div class="container" class:isTop class:isBottom={!isTop}>
	{#if isTop}
		{#if !isTopHidden}
			<div class="pages">
				<button class="arrow" on:click={handleLeftClick}>
					<LeftArrow height="0.9375rem" width="0.5625rem" color={'white'} />
				</button>

				{#if !isSmallLeft}
					<button
						class="first number"
						class:invisible={currentPageNumber - firstPageNumber < 2}
						on:click={handleClick}
						value={firstPageNumber}>{firstPageNumber}</button
					>
					<span class="ellipses" class:invisible={currentPageNumber - firstPageNumber < 2}>...</span
					>
				{/if}
				{#each visiblePages as visiblePage, i}
					<button
						class="number"
						class:current={currentPageIndex == i}
						on:click={handleClick}
						value={visiblePage}>{visiblePage}</button
					>
				{/each}
				{#if !isSmallRight}
					<span class="ellipses" class:invisible={lastPageNumber - currentPageNumber < 2}>...</span>
					<button
						class="last number"
						class:invisible={lastPageNumber - currentPageNumber < 2}
						on:click={handleClick}
						value={lastPageNumber}>{lastPageNumber}</button
					>
				{/if}
				<button class="arrow" on:click={handleRightClick}>
					<RightArrow height="0.9375rem" width="0.5625rem" color={'white'} />
				</button>
			</div>
			<div class="sizes">
				<span class="sizes-label">Show by</span>
				{#each sizes as size}
					<button
						class="number"
						class:current={size === currentPageSize}
						on:click={handleSizeClick}
						value={size}>{size}</button
					>
				{/each}
			</div>
		{/if}
	{/if}
</div>

<style>
	.sizes-label {
		color: white;
		margin-right: 0.8rem;
	}
	.container {
		padding: 0 2.8em;
		max-width: 900px;
		height: 3rem;
		margin: 0 auto;
		display: flex;
		justify-content: space-between;
		align-items: center;
		background-color: var(--secondary-bg-color);
		cursor: default;
	}
	.isTop {
		margin-top: 2em;
		margin-bottom: 0;
		border-radius: 20px 20px 0 0;
	}
	.isBottom {
		border-radius: 0 0 20px 20px;
	}
	.number {
		color: white;
		outline: none;
		border: none;
		background: none;
		font-weight: var(--fontweight-1);
		transition: 0.1s;
	}
	.ellipses {
		font-size: var(--fontsize-medium1);
		color: white;
		cursor: default;
	}
	.sizes .number {
		border-radius: 10px;
	}
	.pages .number {
		border-radius: 50%;
	}
	.arrow {
		background: none;
		border: none;
		transition: 0.1s;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.arrow,
	.number {
		font-size: var(--fontsize-medium1);
		width: 2.6rem;
		height: 2.6rem;
	}
	.number:hover {
		/* box-shadow: 0px 4px 4px 2px rgba(105, 105, 105, 0.7); */
		transform: scale(1.1);
		cursor: pointer;
	}
	.arrow:hover {
		transform: scale(1.1);
		cursor: pointer;
	}
	.invisible {
		visibility: hidden;
	}
	.current {
		background-color: var(--secondary-color);
		color: white;
	}
	.number.current:hover {
		box-shadow: none;
		cursor: default;
		transform: none;
	}
	.pages {
		display: flex;
		align-items: center;
	}
	.sizes {
		display: flex;
		align-items: center;
		gap: 0.3rem;
	}

	@media (max-width: 1160px) {
		.isTop {
			padding: 0 1.5em;
		}
	}
	@media (max-width: 800px) {
		.isTop {
			margin-bottom: 0;
			padding: 0.5rem 0;
			height: 6rem;
			border-radius: 40px 40px 0 0;
		}
		.container {
			flex-direction: column;
		}
		.isBottom {
			border-radius: 0 0 40px 40px;
			height: 3rem;
		}
	}
</style>
