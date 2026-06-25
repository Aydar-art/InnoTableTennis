<script>
	import { fly } from 'svelte/transition';
	import { Moon, Sun } from 'lucide-svelte';
	import { browser } from '$app/environment';
	import { DEFAULT_THEME } from '$lib/client/defaults';

	let theme = (browser && localStorage.getItem('color-scheme')) || DEFAULT_THEME;

	$: {
		document.documentElement.setAttribute('color-scheme', theme);
		localStorage.setItem('color-scheme', theme);
	}

	function toggleTheme() {
		theme = theme === 'dark' ? 'light' : 'dark';
	}
</script>

<button on:click={toggleTheme} aria-label="Toggle theme">
	{#if theme === 'dark'}
		<div in:fly={{ y: 10 }}>
			<Sun />
		</div>
	{:else}
		<div in:fly={{ y: -10 }}><Moon /></div>
	{/if}
</button>

<style>
	button {
		padding: 0;
		font-weight: inherit;
		background: none;
		border: none;
		box-shadow: none;
		overflow: hidden;
		border-radius: 2rem;
		background-color: var(--main-color);
		cursor: pointer;
	}

	button > * {
		display: flex;
	}
</style>
