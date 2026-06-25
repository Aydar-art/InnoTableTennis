<script lang="ts">
	import { AddPlayerFormStore } from '$lib/client/stores/stores.forms';
	import type { Player } from '$lib/types/types';
	import Button from '$lib/components/base/Button.svelte';
	import ResetButton from '$lib/components/base/ResetButton.svelte';
	import DropdownInput from '$lib/components/base/inputs/DropdownInput.svelte';
	import { alertPopup } from '$lib/client/popup/popup.handler';
	import { createEventDispatcher } from 'svelte';

	const dispatch = createEventDispatcher();

	export let playerName: string;
	export let allPlayers: Player[];
	export let participants: Player[];
	export let numberParticipants = 0;

	let abilityToChange = false;

	$: playerNames = allPlayers.map((player) => player.name);
	$: participantNames = participants.map((player) => player.name);

	let resetDropout: () => void;

	function resetForm() {
		resetDropout();
	}

	function handleSelectPlayerName(event: CustomEvent) {
		playerName = event.detail;
	}

	async function addParticipant() {
		if (participants.length && !abilityToChange) {
			if (await alertPopup('Are you sure? It will delete all saved progress.')) {
				abilityToChange = true;
			}
		} else {
			abilityToChange = true;
		}
		if (abilityToChange) {
			for (let i = 0; i < allPlayers.length; i++) {
				if (allPlayers[i].name === playerName) {
					if (!participantNames.includes(allPlayers[i].name)) {
						participants = [...participants, allPlayers[i]];
						numberParticipants++;
						dispatch('update', { participants });
					}
				}
			}
		}
	}

	async function removeParticipant() {
		if (participants.length && !abilityToChange) {
			if (await alertPopup('Are you sure? It will delete all saved progress.')) {
				abilityToChange = true;
			}
		} else {
			abilityToChange = true;
		}
		if (abilityToChange) {
			for (let i = 0; i < participants.length; i++) {
				if (participants[i].name === playerName) {
					participants.splice(i, 1);
					participants = participants;
					numberParticipants--;
					dispatch('update', { participants });
				}
			}
		}
	}
</script>

<div class="header-line">
	<h2>Add Participant</h2>
	<ResetButton onClick={resetForm} label="Reset" />
</div>

<form
	on:submit|preventDefault={async () => {
		await addParticipant();
		resetForm();
	}}
>
	<div class="input">
		<!-- svelte-ignore a11y-label-has-associated-control -->
		<label>
			<DropdownInput
				name="name"
				placeholder="Player's name"
				options={playerNames}
				on:select={handleSelectPlayerName}
				isFirstInput={true}
				bind:inputVal={playerName}
				bind:reset={resetDropout}
			/>
		</label>
	</div>
	<div class="line-2-elems">
		<Button
			on:click={async () => {
				await removeParticipant();
				resetForm();
			}}>Remove</Button
		>
		<Button type="submit">Add</Button>
	</div>
</form>

<style>
	.input {
		margin-top: 1rem;
	}
	.line-2-elems {
		margin-top: 2rem;
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 1rem;
	}
	.header-line {
		display: flex;
	}

	h2 {
		margin: 0.9em 0;
		font-size: var(--fontsize-large);
		font-weight: var(--fontweight-1);
		color: var(--title-color);
	}
</style>
