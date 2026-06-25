<script lang="ts">
	import {
		SORT_FILTER_MATCH_FORM,
		SortFilterMatchFormStore,
	} from '$lib/client/stores/stores.forms';
	import Button from '$lib/components/base/Button.svelte';

	import RadioGroup from '$lib/components/base/RadioGroup.svelte';
	import ResetButton from '$lib/components/base/ResetButton.svelte';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import OrderButton from '$lib/components/base/OrderButton.svelte';

	import { page } from '$app/stores';
	import { goto } from '$app/navigation';
	import { objectToURLSearchParams } from '$lib/utils';
	import { onMount } from 'svelte';

	onMount(() => {
		if ($page.url.pathname == '/') {
			let searchParams = $page.url.searchParams;

			$SortFilterMatchFormStore.name = searchParams?.get('name') || $SortFilterMatchFormStore.name;
			$SortFilterMatchFormStore.score =
				searchParams?.get('score') || $SortFilterMatchFormStore.score;
			$SortFilterMatchFormStore.minDateString =
				searchParams?.get('minDateString') || $SortFilterMatchFormStore.minDateString;
			$SortFilterMatchFormStore.minDateString =
				searchParams?.get('minDateString') || $SortFilterMatchFormStore.minDateString;
			$SortFilterMatchFormStore.maxDateString =
				searchParams?.get('maxDateString') || $SortFilterMatchFormStore.maxDateString;
			if (searchParams?.get('sortBy')) {
				$SortFilterMatchFormStore.sortBy = searchParams.get('sortBy') as 'date';
			}
			if (searchParams?.get('descending')) {
				$SortFilterMatchFormStore.descending = searchParams.get('descending') !== 'false';
			}
		}
	});

	$: $SortFilterMatchFormStore.score = $SortFilterMatchFormStore.score.replace(/\s/g, '');

	let radioValues = ['date'];
	let radioLabels = ['Sort by date'];

	const resetForm = function () {
		$SortFilterMatchFormStore = structuredClone(SORT_FILTER_MATCH_FORM);
	};

	function handleSubmit(event: SubmitEvent) {
		const url = new URL($page.url);

		const currentPageSize = url.searchParams.get('currentPageSize');

		const formData = new FormData(event.target as HTMLFormElement);

		const searchParams = objectToURLSearchParams({
			...Object.fromEntries(formData),
			minDateString: $SortFilterMatchFormStore.minDateString,
			maxDateString: $SortFilterMatchFormStore.maxDateString,
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

<form class="filters" on:submit={handleSubmit}>
	<div class="column-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="text"
				name="name"
				isFirst={true}
				bind:stringVal={$SortFilterMatchFormStore.name}
				bind:defaultValue={$SortFilterMatchFormStore.name}
				placeholder="Search by player"
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="text"
				name="score"
				bind:stringVal={$SortFilterMatchFormStore.score}
				bind:defaultValue={$SortFilterMatchFormStore.score}
				placeholder="Search by score"
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="date"
				name="minDateString"
				placeholder="Min date"
				bind:stringVal={$SortFilterMatchFormStore.minDateString}
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="date"
				name="maxDateString"
				placeholder="Max date"
				bind:stringVal={$SortFilterMatchFormStore.maxDateString}
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<div class="last-box margin-top">
			<Button dark={false} disabled={false} type={'submit'}>Search</Button>
		</div>
	</div>

	<h2>Sort by</h2>

	<div class="column-1-elems">
		<input hidden name="sortBy" bind:value={$SortFilterMatchFormStore.sortBy} />
		<RadioGroup
			bind:group={$SortFilterMatchFormStore.sortBy}
			values={radioValues}
			labels={radioLabels}
		/>
	</div>
	<OrderButton bind:value={$SortFilterMatchFormStore.descending} />
	<div class="line-2-elems">
		<div class="last-box full-width margin-top">
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
	.column-1-elems {
		margin-top: 1rem;
		display: grid;
		grid-template-columns: repeat(1, 1fr);
		gap: 1rem;
		align-items: end;
	}
	.line-2-elems {
		margin-top: 1rem;
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 0.44rem;
		align-items: end;
	}
	.last-box {
		grid-column: 2;
	}
	.full-width {
		width: 100%;
	}

	.margin-top {
		margin-top: 1em;
	}
</style>
