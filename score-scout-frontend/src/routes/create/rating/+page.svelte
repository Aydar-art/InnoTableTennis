<script>
  import Switch from '$lib/components/Switch.svelte';
  import Navbar from '$lib/components/Navbar.svelte';
  import Page from '$lib/components/Page.svelte';
  import NavButtons from '../(components)/NavButtons.svelte';
  import { customGoto } from '$lib/utils';
</script>

<svelte:head>
  <title>Create tournament (Rating)</title>
  <meta name="description" content="Choose rating system for the tournament." />
</svelte:head>

<Navbar
  title="Create tournament"
  links={[
    { name: 'Choose rating', href: '/create/rating', active: true },
    { name: 'Add players', href: '/create/players' },
    { name: 'Number of stages', href: '/create/stages' },
    { name: 'First stage', href: '/create/stage_1' },
    { name: 'Second stage', href: '/create/stage_2' },
  ]}
></Navbar>

<Page>
  <div class="flex h-screen w-full bg-white p-4 flex-row gap-5 pt-[225px] pl-[50px]">
    <Switch />
    <div class="text-lg text-foreground-hint">Use rating system</div>
  </div>
</Page>
<NavButtons
  next={() => {
    customGoto('/create/players');
  }}
/>
