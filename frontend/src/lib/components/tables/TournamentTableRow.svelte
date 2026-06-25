<script lang="ts">
	import { enhance } from '$app/forms';
	import { goto } from '$app/navigation';
	import { isLeader } from '$lib/client/stores/stores';
	import type { Tournament } from '$lib/types/types';
	import FinishIcon from '$lib/components/icons/FinishIcon.svelte';
	import PlayersIcon from '$lib/components/icons/PlayersIcon.svelte';
	import { alertPopup } from '$lib/client/popup/popup.handler';
	import { base } from '$app/paths';

	export let tournament: Tournament;
	export let chosenId: number;
	export let editData;
	export let isChoosing: boolean;
	export let mode: string;

	$: selected = chosenId === tournament.id;

	async function handleClick(event: MouseEvent) {
		chosenId = tournament.id;
		if (mode == 'edit') {
			editData = tournament;
		} else if (mode === 'create' && tournament.finished === false) {
			goto(`${base}/tournaments/${tournament.id}/create`);
		} else if ((mode === 'create' && tournament.finished === true) || mode === 'view') {
			goto(`${base}/tournaments/${tournament.id}`);
		}
	}
</script>

<button
	class="tournament-line"
	class:selected
	on:click={handleClick}
	disabled={!isChoosing || selected}
>
	<div class="tournaments-grid" class:not-leader={!$isLeader}>
		<div class="no-wrap content">{tournament.title}</div>
		<div class="no-wrap content">
			{tournament.startDateString}-{tournament.endDateString}
		</div>
		<div class="no-wrap content">{tournament.coefficient}</div>
		<div class="content" style="text-align: right;">
			{tournament.numberOfPlayers}
			<PlayersIcon />
		</div>
		{#if isLeader && !tournament.finished && mode === 'add'}
			<form
				method="POST"
				action="?/finishTournament"
				use:enhance={async ({ cancel }) => {
					let isConfirmed = await alertPopup(
						'Are you sure that you want to finish this tournament?',
					);
					if (!isConfirmed) {
						cancel();
					}
				}}
			>
				<input type="hidden" name="tournamentId" value={tournament.id} />
				<button aria-label="Finish" class="finish-btn"><FinishIcon /></button>
			</form>
		{/if}
	</div>
</button>

<style>
	.tournaments-grid {
		display: grid;
		grid-template-columns: 1fr 1fr 6.5rem 3rem 1rem;
		gap: 1rem 1rem;
		color: var(--content-color);
		height: 1.1rem;
	}

	.tournament-line {
		background: none;
		border: none;
		cursor: pointer;
		padding: 0;
		border: none;
		width: 100%;
		height: 1.3rem;
		margin-bottom: 0.4rem;
	}
	.tournament-line:disabled {
		cursor: default;
	}

	.selected {
		background-color: var(--secondary-color);
		border-radius: 3px;
	}

	.selected .no-wrap {
		color: var(--main-color);
	}

	.tournament-line:enabled:hover {
		background-color: var(--secondary-bg-color);
		border-radius: 3px;
	}

	.finish-btn {
		background: none;
		border: none;
		cursor: pointer;
		padding: 0;
		border: none;
		height: 1rem;
		width: 1rem;
	}
	.no-wrap {
		display: flex;
		align-items: center;
		text-align: left;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}
	.content {
		font-size: var(--fontsize-medium1);
	}

	.tournaments-grid.not-leader {
		grid-template-columns: 1fr 1fr 6.5rem 5rem;
		padding: 0;
	}
</style>
