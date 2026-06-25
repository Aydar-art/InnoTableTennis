<script lang="ts">
	import ProfilePaginationBar from '$lib/components/profile/ProfilePaginationBar.svelte';

	import { createEventDispatcher } from 'svelte';

	const dispatch = createEventDispatcher();

	let currentPageNumber: number;
	let currentPageSize: number;
	export let lastPageNumber = 100;
	export let isTopHidden = true;

	function requestNewPage() {
		dispatch('request', { currentPageNumber: currentPageNumber, currentPageSize: currentPageSize });
	}
</script>

<ProfilePaginationBar
	bind:currentPageNumber
	bind:currentPageSize
	on:click={requestNewPage}
	{isTopHidden}
	{lastPageNumber}
/>
<slot />
<ProfilePaginationBar
	bind:currentPageNumber
	bind:currentPageSize
	on:click={requestNewPage}
	isTop={false}
	{lastPageNumber}
/>
