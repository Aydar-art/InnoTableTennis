<script lang="ts">
	export let mode: string;
	export let chosenId;
	export let editData;
</script>

<div class="bar">
	<div class="bar-wrapper">
		<div class:selected={mode === 'add'} class="addButton">
			<button
				on:click={() => {
					mode = 'add';
					chosenId = -1;
					editData = {};
				}}>Add</button
			>
		</div>
		<div class="bar-button" class:selected={mode === 'edit'}>
			<button
				on:click={() => {
					mode = 'edit';
					chosenId = -1;
					editData = {};
				}}>Edit</button
			>
		</div>
		<div class="bar-button" class:selected={mode === 'delete'}>
			<button
				on:click={() => {
					mode = 'delete';
					chosenId = -1;
					editData = {};
				}}>Delete</button
			>
		</div>
	</div>
</div>

<style>
	.bar {
		background-color: var(--main-color);
		display: flex;
		justify-content: end;
		margin-top: 1rem;
	}

	.bar-wrapper {
		background-color: var(--secondary-bg-color);
		display: flex;
		flex-direction: row;
		width: 36rem;
		border-radius: 20px;
		padding: 0.3rem;
	}

	button {
		width: 12rem;
		box-shadow: none;
		border: none;
		background: none;
		padding: 0;
		cursor: pointer;
		font-size: var(--fontsize-medium1);
		color: var(--content-color);
	}

	.selected {
		background-color: var(--secondary-color);
		border-radius: 20px;
		transition: background-color 0.3s ease-in;
	}

	.selected button {
		color: var(--main-color);
		transition: color 0.3s ease-in;
	}

	@media (max-width: 600px) {
		.bar-wrapper {
			width: 24rem;
		}

		button {
			width: 8rem;
			font-size: var(--fontsize-medium2);
		}
	}

	@media (max-width: 480px) {
		.bar-wrapper {
			width: 21rem;
		}

		button {
			width: 7rem;
			font-size: var(--fontsize-medium2);
		}
	}

	@media (max-width: 300px) {
		.bar-wrapper {
			width: 15rem;
		}

		button {
			width: 5rem;
		}
	}
</style>
