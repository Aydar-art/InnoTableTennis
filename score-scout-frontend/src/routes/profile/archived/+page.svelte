<script lang="ts">
  import Navbar from '$lib/components/Navbar.svelte';
  import Page from '$lib/components/Page.svelte';
  import TournamentBlock from '../(components)/TournamentBlock.svelte';
  import Button from '$lib/components/Button.svelte';
  import { Plus, LogOut } from 'lucide-svelte';
  import { customGoto } from '$lib/utils';
  import type { ITournament } from '$lib/types';

  import type { PageData } from './$types';

  export let data: PageData;

  const email = data.user;
  const tournaments: ITournament[] = data.tournaments;
</script>

<svelte:head>
  <title>{email} tournaments</title>
  <meta name="description" content="View active tournaments for user '{email}'." />
</svelte:head>

<Navbar
  title={email}
  links={[
    { name: 'Active tournaments', href: '/profile/active' },
    { name: 'Archived tournaments', href: '/profile/archived', active: true },
  ]}
  customTitleStyles="text-xl md:text-xl text-black"
>
  <div slot="before" class="flex justify-end md:justify-start">
    <Button
      variant="ghost"
      class="mt-10 mx-6 md:mx-10 text-2xl md:text-lg gap-3 md:gap-2 flex items-center justify-center"
      on:click={() => {
        document.cookie = 'token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT';
        customGoto('/');
      }}
    >
      <LogOut class="w-8 h-8 md:w-6 md:h-6" />
      Logout
    </Button>
  </div>
  <div slot="after" class="flex justify-end">
    <Button
      class="rounded-full mb-8 mr-8 text-xl md:text-base leading-5 gap-2.5 flex items-center justify-center"
      on:click={() => {
        customGoto('/create/rating');
      }}
    >
      <Plus class="w-7 h-7 md:w-5 md:h-5" />
      Create tournament
    </Button>
  </div>
</Navbar>

<Page>
  <div class="flex h-screen w-full bg-white p-4 flex-col gap-5 pt-[120px] pl-[50px]">
    <div class="flex flex-col gap-[40px]">
      {#each tournaments as tournament}
        <TournamentBlock {...tournament} />
      {/each}
    </div>
  </div>
</Page>
