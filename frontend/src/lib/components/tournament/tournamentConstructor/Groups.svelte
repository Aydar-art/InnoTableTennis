<script lang="ts">
	import { createEventDispatcher } from 'svelte';
	import type { Player, Tournament } from '$lib/types/types';
	import Button from '$lib/components/base/Button.svelte';
	import RestartIcon from '$lib/components/icons/RestartIcon.svelte';
	import TournamentGroup from '$lib/components/tournament/tournamentConstructor/TournamentGroup.svelte';
	import type { TournamentStage } from '$lib/types/types.tournaments';

	export let stage: TournamentStage;
	export let finals: Player[][];
	export let tournament: Tournament;

	const dispatch = createEventDispatcher();

	$: numberParticipants = tournament.state.participants.length;

	$: numberGroups = tournament.state.firstStage?.length;

	$: {
		if (!finals && tournament.state.firstStage && tournament.state.firstStage.length) {
			let groupAmount = tournament.state.firstStage.length;
			let maxPlayersAmount = Math.ceil(tournament.state.participants.length / groupAmount);
			finals = new Array(maxPlayersAmount).fill(null).map(() => new Array(groupAmount).fill(null));
		}
	}

	const changeParticipants = function () {
		stage = 'addParticipants';
	};
	const changeNumberGroups = function () {
		stage = 'numberGroups';
	};
	const nextStage = function () {
		prerenderPlaces();
		stage = 'continue';
	};
	async function updateTournament(e: CustomEvent) {
		let newGroup = e.detail;
		if (tournament.state.firstStage) {
			tournament.state.firstStage[e.detail] = newGroup;
		}
		dispatch('update', { state: tournament.state });
	}
	async function updatePlaces(e: CustomEvent, id: number) {
		let players = e.detail;
		for (let i = 0; i < players.length; i++) {
			finals[i][id] = players[i];
		}
	}
	function prerenderPlaces() {
		if (tournament.state.firstStage) {
			for (let i = 0; i < tournament.state.firstStage.length; i++) {
				for (let j = 0; j < tournament.state.firstStage[i].players.length; j++) {
					finals[j][i] = tournament.state.firstStage[i].players[j];
				}
			}
		}
	}
</script>

<div class="groups-layout">
	<div class="form">
		<h1>Next games</h1>
		<h1>Settings</h1>
		<span class="setting-line">
			Participants - {numberParticipants}
			<button on:click={() => changeParticipants()} class="restart-button">
				<RestartIcon />
			</button>
		</span>
		<span class="setting-line">
			Groups - {numberGroups}
			<button on:click={() => changeNumberGroups()} class="restart-button">
				<RestartIcon />
			</button>
		</span>
		<span class="setting-line">Finish the group stage of the tournament</span>
		<div class="finish-button">
			<Button type="button" on:click={() => nextStage()}>Finish</Button>
		</div>
	</div>
	<div class="tables">
		{#if tournament.state}
			{#if tournament.state.firstStage}
				{#each tournament.state.firstStage as group}
					<TournamentGroup
						finalInfo={group}
						tournamentDate={tournament.startDateString}
						isInConstructor
						on:update={updateTournament}
						on:finalize={(event) => {
							updatePlaces(event, group.id);
						}}>Group {group.id + 1}</TournamentGroup
					>
				{/each}
			{/if}
		{/if}
	</div>
</div>

<style>
	.groups-layout {
		display: grid;
		grid-auto-flow: column;
		align-items: center;
		grid-template-columns: 1fr 1.5fr;
	}
	.form {
		max-width: 35rem;
	}
	.tables {
		max-width: 90rem;
	}
	h1 {
		font-size: var(--fontsize-large);
		font-weight: var(--fontweight-1);
		letter-spacing: 0.09375rem;
	}
	.setting-line {
		margin-top: 1rem;
		display: flex;
		align-items: center;
	}
	.restart-button {
		background: none;
		border: none;
		cursor: pointer;
		margin-left: 1rem;
		padding: 0;
		height: 1.1rem;
		width: 1.1rem;
	}
	.finish-button {
		width: 9rem;
		height: 2.75rem;
		margin: auto;
		margin-top: 2rem;
	}

	@media (max-width: 1100px) {
		.groups-layout {
			display: block;
		}
		.form {
			max-width: 500px;
			margin: 0 auto;
		}
		.tables {
			margin: 0 auto;
		}
	}
</style>
