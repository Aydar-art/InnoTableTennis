<script lang="ts">
	export let group: string;
	export let values: string[];
	export let labels: string[];
</script>

{#each values as value, i}
	<div>
		<input type="radio" id={labels[i]} class="radio" bind:group {value} />
		<label for={labels[i]}>{labels[i]}</label>
	</div>
{/each}

<style>
	.radio {
		position: absolute;
		opacity: 0;
	}
	.radio + label {
		position: relative;
		padding: 0 0 0 2rem;
		cursor: pointer;
	}
	.radio + label:before {
		content: '';
		position: absolute;
		top: 0;
		left: 0.3rem;
		width: 0.9rem;
		height: 0.9rem;
		border: 2px solid var(--content-color);
		border-radius: 2rem;
		background: var(--main-color);
	}
	.radio + label:after {
		content: '';
		position: absolute;
		top: 0.3rem;
		left: 0.6rem;
		width: 0.5rem;
		height: 0.5rem;
		border-radius: 2rem;
		background: var(--content-color);
		opacity: 0;
		transition: 0.2s;
	}
	.radio:checked + label:after {
		opacity: 1;
	}
</style>
