<script lang="ts">
	import * as db from '$lib/client/requests';
	import type { Player, Tournament } from '$lib/types/types.js';
	import Button from '$lib/components/base/Button.svelte';
	import AddParticipantForm from '$lib/components/forms/AddParticipantForm.svelte';
	import ParticipantsCreateTable from '$lib/components/tables/ParticipantsCreateTable.svelte';
	import type { TournamentStage } from '$lib/types/types.tournaments';
	import { createEventDispatcher } from 'svelte';

	const dispatch = createEventDispatcher();

	export let stage: TournamentStage;
	export let tournament: Tournament;

	let participants = tournament.state.participants;

	$: tournament.state.participants = participants;

	$: numberParticipants = participants.length;

	let participant: Player = {} as Player;

	const nextStage = async function () {
		stage = 'numberGroups';
	};

	function handleParticipantsUpdate(e: CustomEvent) {
		participants = e.detail.participants;

		dispatch('update', { state: { ...tournament.state, participants } });
	}
</script>

<div class="form-list-layout">
	<div class="form">
		{#await db.getAllPlayers() then allPlayers}
			<AddParticipantForm
				{allPlayers}
				playerName={participant.name}
				{participants}
				on:update={handleParticipantsUpdate}
			/>
		{/await}
	</div>
	<div class="participants-list">
		<ParticipantsCreateTable bind:participant {participants} />
		<div class="line-2-elems">
			<span>{numberParticipants} participants in total</span>
			<div class="button">
				<Button on:click={() => nextStage()}>Continue</Button>
			</div>
		</div>
	</div>
</div>

<style>
	.form-list-layout {
		display: grid;
		align-items: center;
		grid-template-columns: 1fr 2fr;
	}
	.form {
		max-width: 350px;
		margin-right: 2rem;
	}
	.participants-list {
		max-width: 900px;
	}
	.line-2-elems {
		margin-top: 1rem;
		margin-left: auto;
		width: 30rem;
		display: grid;
		grid-template-columns: 1.5fr 1fr;
		gap: 1rem;
	}

	span {
		margin: auto;
		font-weight: 500;
		font-size: var(--fontsize-large);
	}

	@media (max-width: 1100px) {
		.form-list-layout {
			display: block;
		}
		.form {
			max-width: 500px;
			margin: 0 auto;
		}
		.participants-list {
			margin: 0 auto;
			overflow-x: hidden;
		}
		.line-2-elems {
			margin-left: 0;
			display: block;
		}
		.button {
			margin: 1rem 0;
			width: 10rem;
		}
	}
</style>
