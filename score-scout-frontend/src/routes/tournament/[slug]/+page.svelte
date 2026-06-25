<script lang="ts">
  import TournamentTable from './(components)/TournamentTable.svelte';
  import TournamentHeader from './TournamentHeader.svelte';
  import type { PageData } from './$types';
  import Button from '$lib/components/Button.svelte';
  import { ArrowLeft } from 'lucide-svelte';

  const tournamentInfo = {
    players: [
      {
        id: 0,
        name: 'Svetlana Kuznetsova',
      },
      {
        id: 1,
        name: 'Serena Williams',
      },
      {
        id: 2,
        name: 'Egor Gerasimov',
      },
      {
        id: 3,
        name: 'Daniil Medvedev',
      },
    ],

    matches: [
      {
        firstPlayer: 0,
        secondPlayer: 1,
      },
      {
        firstPlayer: 2,
        secondPlayer: 3,
      },
    ],
  };

  export let data: PageData;
</script>

<svelte:head>
  <title>{data.tournament.title}</title>
  <meta name="description" content="View tournament '{data.tournament.title}'." />
</svelte:head>

<Button class="absolute my-2" on:click={() => history.back()} variant="ghost">
  <ArrowLeft />
  Back
</Button>

<TournamentHeader
  title={data.tournament.title}
  date={new Date(data.tournament.date)}
  participantsCount={data.tournament.participantsCount}
  gamesPlayedCount={data.tournament.gamesPlayedCount}
  customText={data.tournament.customText}
/>
<div class="p-4 md:p-6 w-full">
  <div class="md:grid md:grid-cols-2 gap-6">
    <TournamentTable group="First group" {tournamentInfo} class="mb-4 md:mb-0 md:!w-full" />
    <TournamentTable group="Second group" {tournamentInfo} class="mb-4 md:mb-0 md:!w-full" />
    <TournamentTable group="Third group" {tournamentInfo} class="mb-4 md:mb-0 md:!w-full" />
    <TournamentTable group="Fourth group" {tournamentInfo} class="mb-4 md:mb-0 md:!w-full" />
  </div>
</div>
