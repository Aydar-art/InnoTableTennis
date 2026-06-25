<script lang="ts">
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import PaginationBar from '$lib/components/base/pagination/PaginationBar.svelte';
	import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '$lib/client/defaults';

	let url: URL;
	export let currentPageNumber = 0;
	export let currentPageSize = 0;
	$: {
		url = new URL($page.url);
		currentPageNumber = Number(url.searchParams.get('currentPageNumber') || DEFAULT_PAGE_NUMBER);
		currentPageSize = Number(url.searchParams.get('currentPageSize') || DEFAULT_PAGE_SIZE);
	}
	export let lastPageNumber = 1;
	export let isTopHidden = false;

	function requestNewPage() {
		const url = new URL($page.url);

		url.searchParams.delete('currentPageNumber');
		url.searchParams.delete('currentPageSize');

		const searchParams = new URLSearchParams({
			currentPageNumber: String(currentPageNumber),
			currentPageSize: String(currentPageSize),
		}).toString();

		const new_url = new URL(`${url.origin}${url.pathname}?${searchParams}&${url.searchParams}`);
		goto(new_url.href, { replaceState: true, noScroll: true });
	}
</script>

<PaginationBar
	bind:currentPageNumber
	bind:currentPageSize
	on:click={requestNewPage}
	{isTopHidden}
	{lastPageNumber}
/>
<slot />
<PaginationBar
	bind:currentPageNumber
	bind:currentPageSize
	on:click={requestNewPage}
	isTop={false}
	{lastPageNumber}
/>
