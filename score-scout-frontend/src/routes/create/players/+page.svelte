<script lang="ts">
  import Input from '$lib/components/Input.svelte';
  import Navbar from '$lib/components/Navbar.svelte';
  import Page from '$lib/components/Page.svelte';
  import Button from '$lib/components/Button.svelte';
  import { slugify } from '$lib/utils';
  import { CheckCircle, Upload, Plus } from 'lucide-svelte';
  import Table from '$lib/components/Table.svelte';
  import Modal from '$lib/components/Modal.svelte';

  let rankingsColumns = ['Name', 'Rating'];
  let rankingsRows: string[][] = [];
  let name = '';
  let rating = '';
  let openModal = false;
  let tournamentName = '';
</script>

<svelte:head>
  <title>Create tournament (Add players)</title>
  <meta name="description" content="Add players to the tournament." />
</svelte:head>

<Navbar
  title="Create tournament"
  links={[
    { name: 'Choose rating', href: '/create/rating' },
    { name: 'Add players', href: '/create/players', active: true },
    { name: 'Number of stages', href: '/create/stages' },
    { name: 'First stage', href: '/create/stage_1' },
    { name: 'Second stage', href: '/create/stage_2' },
  ]}
></Navbar>

<Page>
  <div class="h-screen w-full bg-white p-4 gap-5 pt-52 px-8">
    <div class="flex flex-col md:flex-row gap-10">
      <div class="flex flex-col">
        <div class="font-bold text-[22px] text-primary pb-5">Add player</div>
        <Input variant="shadowed" class="mt-1 mb-5 w-[300px]" placeholder="Player's name" bind:value={name} />
        <Input variant="shadowed" class="mt-1 w-[300px] mb-8" placeholder="Rating (optional)" bind:value={rating} />
        <Button
          class="w-[140px] mb-9"
          on:click={() => {
            if (name === '') return alert('Name is required');
            if (rating === '') rating = '0';
            if (isNaN(+rating)) return alert('Rating should be a number');
            if (+rating < 0 || +rating > 100) return alert('Rating should be between 0 and 100');
            rankingsRows = [...rankingsRows, [name, rating]];
            name = '';
            rating = '';
          }}
        >
          Add
          <Plus class="w-6 h-6" />
        </Button>
        <div class="flex flex-col items-center gap-2 md:pl-[6rem] md:items-start">
          <div class="text-xl">or upload csv</div>
          <Button variant="secondary">
            Upload
            <Upload class="w-6 h-6" />
          </Button>
        </div>
      </div>
      <div class="ml-auto w-1/4">
        {#key rankingsRows}
          <Table columns={rankingsColumns} rows={rankingsRows} />
        {/key}
      </div>
    </div>
  </div>
</Page>

<Button
  class="fixed bottom-10 right-10 rounded-full text-xl leading-5 gap-2.5 flex items-center justify-center"
  on:click={() => {
    openModal = true;
  }}
>
  Create
  <CheckCircle class="w-7 h-7" />
</Button>
<Modal bind:open={openModal} title="Create tournament" description="Enter tournament name to create it">
  <form method="POST">
    <Input variant="shadowed" class="mt-1 mb-5 w-[300px]" placeholder="Tournament name" name="title" bind:value={tournamentName} />
    <Input class="hidden" name="slug" value={slugify(tournamentName)} />
    <Input class="hidden" name="participantsCount" value={rankingsRows.length} />
    <Button class="w-[140px] pt-2" type="submit">
      Create
      <CheckCircle class="w-6 h-6" />
    </Button>
  </form>
</Modal>
<!--<NavButtons-->
<!--  next={() => {-->
<!--    customGoto('/create/stages');-->
<!--  }}-->
<!--  prev={() => {-->
<!--    customGoto('/create/rating');-->
<!--  }}-->
<!--/>-->
