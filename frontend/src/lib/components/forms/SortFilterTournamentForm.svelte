<script lang="ts">
	// import { enhance } from '$app/forms';
	import {
		SORT_FILTER_TOURNAMENT_FORM,
		SortFilterTournamentFormStore,
	} from '$lib/client/stores/stores.forms';
	import Button from '$lib/components/base/Button.svelte';
	import RadioGroup from '$lib/components/base/RadioGroup.svelte';
	import OrderButton from '$lib/components/base/OrderButton.svelte';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import ResetButton from '$lib/components/base/ResetButton.svelte';
	import { page } from '$app/stores';
	import { objectToURLSearchParams } from '$lib/utils';
	import { goto } from '$app/navigation';
	import { onMount } from 'svelte';

	onMount(() => {
		if ($page.url.pathname == '/') {
			let searchParams = $page.url.searchParams;

			const sortBy = searchParams?.get('sortBy') as 'date' | 'players' | 'coefficient';

			$SortFilterTournamentFormStore.title =
				searchParams?.get('title') || $SortFilterTournamentFormStore.title;
			$SortFilterTournamentFormStore.maxParticipants =
				Number(searchParams?.get('maxParticipants')) ||
				$SortFilterTournamentFormStore.maxParticipants;
			$SortFilterTournamentFormStore.minParticipants =
				Number(searchParams?.get('minParticipants')) ||
				$SortFilterTournamentFormStore.minParticipants;
			$SortFilterTournamentFormStore.endDateString =
				searchParams?.get('endDateString') || $SortFilterTournamentFormStore.endDateString;
			$SortFilterTournamentFormStore.startDateString =
				searchParams?.get('startDateString') || $SortFilterTournamentFormStore.startDateString;
			if (['date', 'players', 'kf'].includes(sortBy)) {
				$SortFilterTournamentFormStore.sortBy = sortBy;
			}
			if (searchParams?.get('descending')) {
				$SortFilterTournamentFormStore.descending = searchParams.get('descending') !== 'false';
			}
		}
	});

	let radioValues = ['date', 'players', 'coefficient'];
	let radioLabels = ['Sort by date', 'Sort by number of players', 'Sort by kf'];

	const resetForm = function () {
		$SortFilterTournamentFormStore = structuredClone(SORT_FILTER_TOURNAMENT_FORM);
	};

	function handleSubmit(event: SubmitEvent) {
		const url = new URL($page.url);

		const currentPageSize = url.searchParams.get('currentPageSize');

		const formData = new FormData(event.target as HTMLFormElement);

		const searchParams = objectToURLSearchParams({
			...Object.fromEntries(formData),
			endDateString: $SortFilterTournamentFormStore.endDateString,
			startDateString: $SortFilterTournamentFormStore.startDateString,
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
	<div class="column-1-elems">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="text"
				name="title"
				isFirst={true}
				bind:stringVal={$SortFilterTournamentFormStore.title}
				placeholder="Search by title"
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
				name="minParticipants"
				bind:numberVal={$SortFilterTournamentFormStore.minParticipants}
				placeholder="Min participants"
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="number"
				min="0"
				max="1000"
				name="maxParticipants"
				bind:numberVal={$SortFilterTournamentFormStore.maxParticipants}
				placeholder="Max participants"
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="date"
				name="startDateString"
				placeholder="Start date"
				bind:stringVal={$SortFilterTournamentFormStore.startDateString}
			/>
		</label>
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<InputTemplate
				type="date"
				name="endDateString"
				placeholder="End date"
				bind:stringVal={$SortFilterTournamentFormStore.endDateString}
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<div class="last-box full-width margin-top">
			<Button dark={false} disabled={false} type={'submit'}>Search</Button>
		</div>
	</div>

	<h2>Sort by</h2>

	<div class="column-3-elems">
		<input hidden name="sortBy" bind:value={$SortFilterTournamentFormStore.sortBy} />
		<RadioGroup
			bind:group={$SortFilterTournamentFormStore.sortBy}
			values={radioValues}
			labels={radioLabels}
		/>
	</div>
	<OrderButton bind:value={$SortFilterTournamentFormStore.descending} />
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
		margin: 0 auto 1rem;
	}
	.column-1-elems {
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
	}
	.column-3-elems {
		margin-top: 1rem;
		display: grid;
		grid-template-columns: repeat(1, 1fr);
		gap: 1rem;
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
