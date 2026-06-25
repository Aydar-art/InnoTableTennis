<script lang="ts">
	import AscendingIcon from '$lib/components/icons/AscendingIcon.svelte';
	import DescendingIcon from '$lib/components/icons/DescendingIcon.svelte';

	export let value = true;
</script>

<div class="line-2-elems">
	<label class="sorting-order" id="sorting-order-descending">
		<input type="radio" id="descending" name="descending" bind:group={value} value={true} />
		<DescendingIcon disabled={!value} />
	</label>
	<label class="sorting-order">
		<input type="radio" id="ascending" name="descending" bind:group={value} value={false} />
		<AscendingIcon disabled={value} />
	</label>
</div>

<style>
	.line-2-elems {
		margin-top: 1rem;
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 1.88rem;
	}
	input {
		box-sizing: border-box;
		border: none;
		border-bottom: 5px solid var(--secondary-bg-color);
		padding: 0.5em 0;
		color: var(--not-chosen-font-color);
		background-color: var(--main-color);
		font-size: var(--fontsize-medium1);
		transition: 0.2s linear;
	}
	input:focus {
		outline: none;
		color: var(--content-color);
		border-bottom: 5px solid var(--secondary-color);
	}
	#sorting-order-descending {
		display: flex;
		justify-content: end;
	}
	.sorting-order input {
		display: none;
	}
</style>
