<script lang="ts">
	import { page } from '$app/stores';

	import { onDestroy } from 'svelte';

	import Errors from '$lib/components/error/Errors.svelte';

	import { loadedPage } from '$lib/client/stores/stores';
	import PopupCaller from '$lib/components/popup/PopupCaller.svelte';

	let isLoaded = false;

	$: if ($loadedPage == $page.data.title || $loadedPage === null) {
		isLoaded = true;
		$loadedPage = $page.data.title;
	}

	onDestroy(() => {
		isLoaded = false;
		$loadedPage = null;
	});
</script>

<svelte:head>
	<title>InnoTableTennis | {$page.data.title}</title>
	<meta name="author" content="InnoTableTennis club" />
	<meta
		name="description"
		content="Welcome to InnoTableTennis Club! Stay updated with exciting table tennis matches, tournaments, and player statistics. Join us at Innopolis University for a thrilling table tennis experience."
	/>
	<meta
		name="keywords"
		content="table tennis, tennis, Innopolis, Inno, University, InnoTableTennis, Innopolis University, Club, Table tennis club"
	/>
	<link rel="canonical" href="https://tabletennis.innopolis.university/" />
	<meta name="robots" content="index, follow" />
</svelte:head>

{#if isLoaded}
	<PopupCaller />
	<slot />
	<Errors />
{/if}

<style>
	@media (max-width: 800px) {
		* {
			font-size: var(--fontsize-medium2);
		}
	}
</style>
