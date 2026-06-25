<script lang="ts">
	import { goto } from '$app/navigation';
	import { isLeader } from '$lib/client/stores/stores';
	import type { Player } from '$lib/types/types';

	import { base } from '$app/paths';

	export let player: Player;
	export let chosenId: number;
	export let editData;
	export let isChoosing: boolean;
	export let mode: string;
	export let position: number;

	$: selected = chosenId === player.id;

	async function handleClick(event: MouseEvent) {
		chosenId = player.id;
		if (mode === 'edit') {
			editData = player;
		} else if (mode === 'view') {
			goto(`${base}/players/${player.id}`);
		}
	}

	function getAlias(alias: string) {
		if (alias) {
			return '@' + alias;
		}
		return '-';
	}
</script>

<button
	class="player-line"
	class:selected
	on:click={handleClick}
	disabled={!isChoosing || selected}
>
	<div class="players-grid" class:not-leader={!$isLeader}>
		<div>
			<span class="position bold">{position}</span>
		</div>
		<div class="no-wrap bold">{player.name}</div>
		<div class="no-wrap">{getAlias(player.telegramAlias)}</div>
		<div class="no-wrap">{player.numberOfWins}/{player.numberOfLosses}</div>
		<div class="rating bold">{player.rating}</div>
	</div>
</button>

<style>
	.player-line {
		background: none;
		border: none;
		cursor: pointer;
		padding: 0;
		border: none;
		width: 100%;
		height: 1.3rem;
		margin-bottom: 0.4rem;
	}
	.position {
		color: var(--content-color);
		display: inline-block;
		text-align: left;
		width: 100%;
	}
	.rating {
		display: inline-block;
		text-align: right;
		width: 100%;
	}
	.bold {
		font-weight: var(--fontweight-1);
	}
	.player-line:disabled {
		cursor: default;
	}

	.player-line:enabled:hover {
		background-color: var(--secondary-bg-color);
		border-radius: 3px;
	}

	.selected {
		background-color: var(--secondary-color);
		border-radius: 3px;
	}

	.selected .position {
		color: var(--main-color);
	}

	.selected .no-wrap {
		color: var(--main-color);
	}

	.no-wrap {
		display: flex;
		align-items: center;
		text-align: left;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.players-grid {
		display: grid;
		grid-template-columns: 1.8rem 1fr 1fr 6.2rem 4.8rem;
		gap: 1rem 1rem;
		color: var(--content-color);
		font-size: var(--fontsize-medium1);
		height: 1.25rem;
	}

	.players-grid.not-leader {
		grid-template-columns: 1.8rem 1fr 1fr 6rem 4.6rem;
	}
</style>
