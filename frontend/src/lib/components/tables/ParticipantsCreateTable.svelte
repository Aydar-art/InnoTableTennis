<script lang="ts">
	import type { Player } from '$lib/types/types';
	import Pagination from '$lib/components/base/pagination/Pagination.svelte';

	export let participant: Player;
	export let participants: Player[];

	$: participants.sort(function (player1, player2) {
		if (player1.rating < player2.rating) {
			return 1;
		} else if (player1.rating === player2.rating) {
			return 0;
		} else {
			return -1;
		}
	});

	function getAlias(alias: string) {
		if (alias) {
			return '@' + alias;
		}
		return '-';
	}
</script>

<Pagination isTopHidden={true}>
	<div class="scroll">
		<section class="games-list">
			<div class="table-header">
				<span>#</span>
				<span>Name</span>
				<span>Telegram Alias</span>
				<span>Rating</span>
				<!-- <span>Additional Info</span> -->
				<span />
			</div>

			{#each participants as player, i}
				<button
					class="player-line"
					on:click|preventDefault={() => {
						participant = player;
					}}
				>
					<div class="players-grid">
						<div>
							<span class="position bold">{i + 1}</span>
						</div>
						<div class="no-wrap bold">{player.name}</div>
						<div class="no-wrap">{getAlias(player.telegramAlias)}</div>
						<div class="rating bold">{player.rating}</div>
					</div>
				</button>
			{/each}
		</section>
	</div>
</Pagination>

<style>
	.games-list {
		max-width: 900px;
		min-width: 700px;

		height: 29rem;
		margin-top: 1rem;
		font-size: var(--fontsize-medium1);
		overflow-y: scroll;
	}
	.games-list::-webkit-scrollbar {
		display: none;
	}
	.scroll::-webkit-scrollbar {
		display: none;
	}
	.scroll {
		overflow-x: scroll;
	}
	.players-grid {
		display: grid;
		grid-template-columns: 1.8rem 1fr 1fr 4.8rem;
		gap: 1rem 1rem;
		color: var(--content-color);
		font-size: var(--fontsize-medium1);
		height: 1.25rem;
	}

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
	.player-line:disabled {
		cursor: default;
	}

	.player-line:enabled:hover {
		background-color: var(--secondary-bg-color);
		border-radius: 3px;
	}

	.no-wrap {
		display: flex;
		align-items: center;
		text-align: left;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.table-header {
		display: grid;
		grid-template-columns: 1.8rem 1fr 1fr 4.8rem;
		font-weight: var(--fontweight-1);
		gap: 1rem 1rem;
	}

	.table-header span {
		color: var(--content-color);
		font-size: var(--fontsize-large);
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
</style>
