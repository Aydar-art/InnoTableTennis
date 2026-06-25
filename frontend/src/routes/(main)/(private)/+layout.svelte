<script lang="ts">
	export let data;
</script>

{#if data.isAuthorized}
	<slot />
{:else}
	<p class="details">Oops! That page doesn't exist or it is private</p>
{/if}

<style>
	.details {
		font-size: var(--fontsize-medium1);
		text-align: center;
		margin-top: 15rem;
	}
</style>
