<script lang="ts">
	import { onMount } from 'svelte';
	import { changeDateDottedFormat, changeDateFormat } from '$lib/utils';

	export let type: string;
	export let name: string;
	export let placeholder: string;

	export let defaultValue = '';
	export let defaultNumValue: number | null = null;
	export let required = false;
	export let isFirst = false;

	export let min = '0';
	export let max = '10';

	export let stringVal = defaultValue;
	export let numberVal = defaultNumValue;

	let dateVal = stringVal ? changeDateFormat(stringVal) : '';
	$: {
		if (type === 'date') {
			dateVal = stringVal ? changeDateFormat(stringVal) : '';
		}
	}
	function handleDateChange() {
		stringVal = dateVal ? changeDateDottedFormat(dateVal) : '';
	}
	$: isDateEmpty = dateVal == '' ? true : false;

	export let textAlignCenter = false;

	export const reset = () => {
		stringVal = defaultValue;
		numberVal = defaultNumValue;
	};

	let input: HTMLInputElement | HTMLTextAreaElement;

	onMount(() => {
		if (isFirst) input.focus();
	});
</script>

{#if type === 'number'}
	<input
		type="number"
		{min}
		{max}
		{name}
		bind:value={numberVal}
		{placeholder}
		{required}
		bind:this={input}
		class:text-center={textAlignCenter}
	/>
{:else if type === 'float'}
	<input
		type="text"
		{name}
		bind:value={numberVal}
		{placeholder}
		{required}
		bind:this={input}
		class:text-center={textAlignCenter}
	/>
{:else if type === 'text'}
	<input type="text" {name} bind:value={stringVal} {placeholder} {required} bind:this={input} />
{:else if type === 'password'}
	<input type="password" {name} bind:value={stringVal} {placeholder} {required} bind:this={input} />
{:else if type === 'textarea'}
	<textarea {name} bind:value={stringVal} {placeholder} {required} bind:this={input} />
{:else if type === 'date'}
	<div class="date-container">
		<input type="text" {placeholder} />
		<input
			type="date"
			{name}
			bind:value={dateVal}
			on:change={handleDateChange}
			{placeholder}
			{required}
			bind:this={input}
			class="date"
			class:isDateEmpty
			class:text-center={textAlignCenter}
		/>
	</div>
{/if}

<style>
	textarea {
		overflow-y: scroll;
		border-radius: 20px;
		resize: none;
		box-sizing: border-box;
		border: 3px solid var(--secondary-bg-color);
		padding: 0.75em;
		width: 100%;
		height: 100%;
		color: var(--not-chosen-font-color);
		background-color: var(--main-color);
		font-size: var(--fontsize-medium1);
	}
	input {
		width: 100%;
		height: 100%;
		box-sizing: border-box;
		border: none;
		border-bottom: 5px solid var(--secondary-bg-color);
		padding: 0.5em 0;
		color: var(--not-chosen-font-color);
		background-color: var(--main-color);
		font-size: var(--fontsize-medium1);
		transition: 0.2s linear;
	}
	.date-container {
		width: 100%;
		height: 100%;
		box-sizing: border-box;
	}
	input:focus {
		outline: none;
		color: var(--content-color);
		border-bottom: 5px solid var(--secondary-color);
	}

	textarea:focus {
		outline: none;
		color: var(--content-color);
	}

	input:disabled,
	textarea:disabled {
		background-color: var(--secondary-bg-color);
		color: var(--not-chosen-font-color);
		box-shadow: none;
		cursor: default;
	}
	.date-container {
		height: 2.8rem;
	}
	.date {
		position: relative;
		top: -2.8rem;
	}
	.isDateEmpty {
		opacity: 0;
	}
	.date:focus {
		opacity: 1;
	}
	.text-center {
		text-align: center;
	}
</style>
