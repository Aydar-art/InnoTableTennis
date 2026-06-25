<script lang="ts">
	import {
		SORT_FILTER_PLAYER_FORM,
		SortFilterPlayerFormStore,
	} from '$lib/client/stores/stores.forms';
	import Button from '$lib/components/base/Button.svelte';
	import RadioGroup from '$lib/components/base/RadioGroup.svelte';
	import OrderButton from '$lib/components/base/OrderButton.svelte';

	import { onMount } from 'svelte';
	import ResetButton from '$lib/components/base/ResetButton.svelte';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import { page } from '$app/stores';
	import { objectToURLSearchParams } from '$lib/utils';
	import { goto } from '$app/navigation';

	onMount(() => {
		if ($page.url.pathname == '/') {
			let searchParams = $page.url.searchParams;

			$SortFilterPlayerFormStore.name =
				searchParams?.get('name') || $SortFilterPlayerFormStore.name;
			$SortFilterPlayerFormStore.telegramAlias =
				searchParams?.get('telegramAlias') || $SortFilterPlayerFormStore.telegramAlias;
			$SortFilterPlayerFormStore.minRating =
				Number(searchParams?.get('minRating')) || $SortFilterPlayerFormStore.minRating;
			$SortFilterPlayerFormStore.maxRating =
				Number(searchParams?.get('endDateString')) || $SortFilterPlayerFormStore.maxRating;
			if (searchParams?.get('sortBy')) {
				$SortFilterPlayerFormStore.sortBy = searchParams.get('sortBy') as 'rating' | 'name';
			}
			if (searchParams?.get('descending')) {
				$SortFilterPlayerFormStore.descending = searchParams.get('descending') !== 'false';
			}
		}
	});

	let radioValues = ['rating', 'name'];
	let radioLabels = ['Sort by rating', 'Sort by name'];

	const resetForm = function () {
		$SortFilterPlayerFormStore = structuredClone(SORT_FILTER_PLAYER_FORM);
	};

	function handleSubmit(event: SubmitEvent) {
		const url = new URL($page.url);

		const currentPageSize = url.searchParams.get('currentPageSize');

		const formData = new FormData(event.target as HTMLFormElement);

		const searchParams = objectToURLSearchParams({
			...Object.fromEntries(formData),
			currentPageSize,
		});

		const new_url = new URL(`${url.origin}${url.pathname}?${searchParams}`);

		event.preventDefault();
		goto(new_url.href, { replaceState: true, noScroll: true, keepFocus: true });
	}
</script>

<div class="line-2-elems">
	<h2>Filters</h2>
	<ResetButton onClick={resetForm} label="Reset" />
</div>

<form on:submit={handleSubmit} class="filters">
	<div class="column-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="text"
				name="name"
				isFirst={true}
				bind:stringVal={$SortFilterPlayerFormStore.name}
				placeholder="Search by name"
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="text"
				name="telegramAlias"
				bind:stringVal={$SortFilterPlayerFormStore.telegramAlias}
				placeholder="Search by alias"
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="number"
				min="0"
				max="1000"
				name="minRating"
				bind:numberVal={$SortFilterPlayerFormStore.minRating}
				placeholder="Min rating"
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="number"
				min="0"
				max="1000"
				name="maxRating"
				bind:numberVal={$SortFilterPlayerFormStore.maxRating}
				placeholder="Max rating"
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<div class="last-box margin-top">
			<Button dark={false} disabled={false} type={'submit'}>Search</Button>
		</div>
	</div>

	<h2>Sort by</h2>

	<div class="column-2-elems">
		<input hidden name="sortBy" bind:value={$SortFilterPlayerFormStore.sortBy} />
		<RadioGroup
			bind:group={$SortFilterPlayerFormStore.sortBy}
			values={radioValues}
			labels={radioLabels}
		/>
	</div>
	<OrderButton bind:value={$SortFilterPlayerFormStore.descending} />
	<div class="line-2-elems">
		<div class="last-box margin-top">
			<Button dark={false} disabled={false} type={'submit'}>Sort</Button>
		</div>
	</div>
</form>

<style>
	h2 {
		font-size: var(--fontsize-large);
		margin: 0.9em 0;
		font-weight: var(--fontweight-1);
		color: var(--title-color);
	}

	form {
		max-width: 800px;
		font-size: var(--fontsize-medium1);
	}
	.filters {
		margin: 0 auto 1em;
	}
	.column-2-elems {
		margin-top: 1rem;
		display: grid;
		grid-template-columns: repeat(1, 1fr);
		gap: 1rem;
		align-items: end;
	}
	.line-2-elems {
		margin-top: 0.9rem;
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 1rem 0.44rem;
		align-items: end;
	}
	.last-box {
		grid-column: 2;
	}

	.margin-top {
		margin-top: 1em;
	}
</style>
