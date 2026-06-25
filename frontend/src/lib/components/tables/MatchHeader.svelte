<script lang="ts">
	export let title: string;
	export let isMain = false;
</script>

<h3 class:main={isMain} class:not-main={!isMain}>{title}</h3>
{#if !isMain}
	<div class="separator" />
{/if}

<style>
	h3 {
		margin: 0.1em 0;
		color: var(--content-color);
		font-size: var(--fontsize-large);
		font-weight: var(--fontweight-2);
		/* text-transform: uppercase; */
	}
	.separator {
		width: 100%;
		height: 2px;
		background-color: var(--secondary-color);
		margin-top: 0.3rem;
		margin-bottom: 0.6rem;
	}
	.not-main {
		font-size: var(--fontsize-medium2);
		color: var(--not-chosen-font-color);
	}
	.main {
		margin-top: 1.25em;
	}
</style>
