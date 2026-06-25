<script lang="ts">
	import { createEventDispatcher } from 'svelte';
	import { onMount } from 'svelte';

	const dispatch = createEventDispatcher();

	export let options: string[];
	export let name: string;
	export let placeholder: string;
	export let defaultValue = '';
	export let isFirstInput = false;

	export const reset = () => {
		inputVal = defaultValue;
		selectedOption = '';
		filteredOptions = options;
	};

	let filteredOptions: string[] = [];
	let selectedOption = '';
	let isVisible = false;
	let selectedIndex = -1;
	let unorderedList: HTMLUListElement;

	export let inputVal = '';

	let input: HTMLInputElement;

	$: {
		inputVal = defaultValue;
		filteredOptions = options;
		dispatch('select', defaultValue);
	}

	function handleInput(event: Event) {
		inputVal = (event.target as HTMLInputElement).value;

		filteredOptions = options.filter(
			(option) =>
				option.toLowerCase().includes(inputVal.toLocaleLowerCase()) &&
				option.toLowerCase() != inputVal.toLocaleLowerCase(),
		);

		selectedOption = '';
	}

	function handleSelect(event: Event) {
		selectedOption = (<string>(<HTMLElement>event.target).textContent).trim();
		inputVal = selectedOption;
		dispatch('select', selectedOption);
	}

	function handleKeyDown(event: KeyboardEvent) {
		const lastIndex = filteredOptions.length - 1;

		if (event.key === 'ArrowUp' && selectedIndex > 0) {
			event.preventDefault();
			selectedIndex--;

			if (unorderedList) {
				const filteredElement = [...unorderedList.childNodes].filter(
					(node) => node.nodeType === Node.ELEMENT_NODE,
				)[selectedIndex] as HTMLElement;
				filteredElement.focus();
			}
			return;
		} else if (event.key === 'ArrowDown' && selectedIndex < lastIndex) {
			event.preventDefault();
			selectedIndex++;

			if (unorderedList) {
				const filteredElement = [...unorderedList.childNodes].filter(
					(node) => node.nodeType === Node.ELEMENT_NODE,
				)[selectedIndex] as HTMLElement;
				filteredElement.focus();
			}
			return;
		} else if (event.key === 'Enter') {
			selectedOption = filteredOptions[selectedIndex];
			if (selectedOption) inputVal = selectedOption;
			else selectedOption = inputVal;
			dispatch('select', selectedOption);
			selectedIndex = -1;
			(<HTMLElement>document.activeElement).blur();
			event.preventDefault();
			filteredOptions = [];
			return;
		}
	}

	function showOptions() {
		isVisible = true;
		selectedIndex = -1;
	}
	function hideOptions(event: Event) {
		if (!(<HTMLElement>event.target).closest('.component-wrapper')) {
			isVisible = false;
		}
	}

	onMount(() => {
		if (isFirstInput) input.focus();

		window.addEventListener('click', hideOptions);

		return () => {
			window.removeEventListener('click', hideOptions);
		};
	});
</script>

<div class="component-wrapper">
	<input
		type="text"
		on:input={handleInput}
		bind:this={input}
		list="options"
		on:focus={showOptions}
		value={inputVal}
		required
		{name}
		{placeholder}
		autocomplete="off"
		on:keydown={(e) => handleKeyDown(e)}
	/>
	<!-- {#if !('list' in document.createElement('input'))} -->
	{#if isVisible}
		<ul bind:this={unorderedList}>
			{#each filteredOptions as option}
				{#if option.indexOf(selectedOption) !== -1}
					<!-- svelte-ignore a11y-no-noninteractive-tabindex -->
					<!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
					<li on:click={handleSelect} tabindex="0" on:keydown={(e) => handleKeyDown(e)}>
						{option}
					</li>
				{/if}
			{/each}
		</ul>
	{/if}
	<!-- <datalist id="options">
		{#each options as option}
			<option value={option} />
		{/each}
	</datalist> -->
</div>

<style>
	ul {
		position: absolute;
		width: 100%;
		background-color: var(--secondary-bg-color);
		list-style-type: none;
		margin: 0;
		padding: 0;
		max-height: 100px;
		overflow-y: auto;
		box-sizing: border-box;
		border: 0.01em solid var(--not-chosen-font-color);
		font-size: var(--fontsize-medium2);
		z-index: 10;
	}
	li {
		padding: 4px;
		cursor: pointer;
	}
	li:hover {
		background-color: var(--not-chosen-font-color);
	}
	li:focus {
		background-color: var(--secondary-color);
	}
	div,
	input {
		width: 100%;
		height: 100%;
		box-sizing: border-box;
	}
	div {
		position: relative;
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
</style>
