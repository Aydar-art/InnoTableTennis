<script lang="ts">
	import { page } from '$app/stores';
</script>

<div class="container">
	<h1>
		{$page.status}
		{#if $page.error}
			{$page.error.message}
		{/if}
	</h1>
	{#if $page.status >= 500 && $page.status < 600}
		<div>Server currently is under maintenance</div>
	{/if}
	{#if $page.status == 401}
		<div>Username or password is wrong</div>
	{/if}
</div>

<style>
	h1 {
		font-size: var(--fontsize-x-large);
		margin-bottom: 0.1em;
	}
	.container {
		width: 100%;
		margin: 5em 0;
		display: flex;
		flex-direction: column;
		justify-content: center;
		font-size: var(--fontsize-large);
	}
</style>
