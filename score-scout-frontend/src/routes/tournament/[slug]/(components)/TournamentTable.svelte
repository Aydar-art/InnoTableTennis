<script lang="ts">
  import { Trophy, Swords } from 'lucide-svelte';
  import { cn } from '$lib/utils';

  interface Player {
    id: number;
    name: string;
  }

  interface Match {
    firstPlayer: number;
    secondPlayer: number;
  }

  interface TournamentInfo {
    players: Player[];
    matches: Match[];
  }

  export let group: string;
  export let tournamentInfo: TournamentInfo;

  type $$Props = {
    group: string;
    tournamentInfo: TournamentInfo;
    class?: string | undefined;
  };

  function arePlayingTogether(i: number, j: number): boolean {
    return tournamentInfo.matches.some(
      (match) => (match.firstPlayer === i && match.secondPlayer === j) || (match.firstPlayer === j && match.secondPlayer === i)
    );
  }

  let className: $$Props['class'] = undefined;
  export { className as class };
</script>

<div
  class={cn('w-full md:w-[500px] gap-0 rounded-8 md:rounded-20 grid tournament-table shadow-[0_0_4px_2px_#0000003f]', className)}
  style="--players-number: {tournamentInfo.players.length}"
>
  <div class="!justify-start font-semibold text-primary">
    {group}
  </div>
  {#each Array(tournamentInfo.players.length) as _, i}
    <div>{i + 1}</div>
  {/each}
  <div>pts</div>
  <div class="last-x">
    <Trophy class="w-3 h-3 md:w-6 md:h-6" />
  </div>
  {#each tournamentInfo.players as player, i}
    <div class={cn('!justify-start', i == tournamentInfo.players.length - 1 ? 'last-y' : '')}>{player.name}</div>
    {#each Array(tournamentInfo.players.length + 2) as _, j}
      <div
        class={cn(
          j == i ? 'bg-[#e7e7e7]' : '',
          j == tournamentInfo.players.length + 1 ? 'last-x' : '',
          i == tournamentInfo.players.length - 1 ? 'last-y' : ''
        )}
      >
        {#if arePlayingTogether(i, j)}
          <Swords class="w-3 h-3 md:w-6 md:h-6 text-primary" />
        {/if}
      </div>
    {/each}
  {/each}
</div>

<style lang="postcss">
  .tournament-table {
    --cell-height: 3rem;
    grid-template-columns: 1fr repeat(calc(var(--players-number) + 2), var(--cell-height));
  }

  .tournament-table > div {
    padding: 1rem;
    height: var(--cell-height);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1rem;
    line-height: 1.5rem;
  }

  .tournament-table > div:not(.last-x) {
    border-right: 2px solid #e7e7e7;
  }

  .tournament-table > div:not(.last-y) {
    border-bottom: 2px solid #e7e7e7;
  }

  @media screen and (max-width: 768px) {
    .tournament-table {
      --cell-height: 1.75rem;
    }

    .tournament-table > div {
      font-size: 0.7rem;
      line-height: 1rem;
      padding: 0.5rem;
    }
  }
</style>
