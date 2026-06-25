<script lang="ts">
	import Error from '$lib/components/error/Error.svelte';
	import { errors } from '$lib/client/stores/stores';
</script>

<div class="errors-container">
	{#each $errors as error}
		<Error message={error.message} />
	{/each}
</div>

<style>
	div.errors-container {
		position: fixed;
		bottom: 2em;
		display: flex;
		flex-direction: column-reverse;
		gap: 0.5rem;
	}
</style>
