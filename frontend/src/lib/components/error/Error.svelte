<script lang="ts">
	import { fade } from 'svelte/transition';
	export let message = 'Unexpected Error Happened';
</script>

<div class="message" transition:fade>{message}</div>

<style>
	div.message {
		/* word-break: break-all; */
		border-radius: 0px 15px 15px 0px;
		padding: 1em 2em;
		text-align: left;
		background-color: var(--error-back-color);
		color: var(--main-color);
		min-width: 7em;
		max-width: 20em;
		font-family: inherit;
	}
</style>
