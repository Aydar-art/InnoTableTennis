<script lang="ts">
	import NewLeftArrow from '$lib/components/icons/NewLeftArrow.svelte';

	export let action: () => void;
</script>

<button class="back-btn" on:click={action}>
	<div class="arrow">
		<NewLeftArrow />
	</div>
	Back
</button>

<style>
	.back-btn {
		position: absolute;
		cursor: pointer;
		left: 5%;
		top: 10%;
		width: 10rem;
		color: var(--secondary-color);
		font-size: 1.5em;
		background-color: var(--main-color);
		display: flex;
		flex-direction: row;
		border: 0;
		text-decoration: underline;
		justify-content: center;
		align-items: center;
	}
	.arrow {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 1rem;
		height: 1rem;
	}
</style>
