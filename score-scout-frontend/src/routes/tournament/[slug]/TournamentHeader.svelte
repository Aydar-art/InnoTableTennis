<script lang="ts">
  import { cn, slugify } from '$lib/utils.js';
  import { User, Zap, Star, Trophy, Pencil } from 'lucide-svelte';
  import Button from '$lib/components/Button.svelte';
  import Modal from '$lib/components/Modal.svelte';
  import Input from '$lib/components/Input.svelte';

  export let title: string;
  export let date: Date | string;
  export let participantsCount: number;
  export let gamesPlayedCount: number;
  export let customText: string;

  let className: string | undefined = undefined;
  export { className as class };

  function formatDate(date: Date | string) {
    if (typeof date === 'string') {
      return new Date(date).toLocaleDateString();
    }
    return date.toLocaleDateString();
  }

  let editOpen = false;
</script>

<Modal bind:open={editOpen} title="Change tournament title" description="Enter the new title for the tournament">
  <form method="POST">
    <Input type="text" name="title" placeholder="Tournament title" value={title} class="mb-4" />
    <input type="hidden" name="slug" value={slugify(title)} />
    <input type="hidden" name="action" value="edit" />
    <Button type="submit">Save</Button>
  </form>
</Modal>

<div class={cn('flex flex-col md:flex-row p-6 bg-secondary hover pt-20 w-full max-w-full', className)}>
  <div class="flex-1 flex flex-col">
    <div class="flex items-center gap-3">
      <div class="text-2xl font-bold text-primary">
        {title}
      </div>
      <Button
        class="h-6 w-6 font-normal p-0"
        variant="ghost"
        on:click={() => {
          editOpen = true;
        }}
      >
        <Pencil class="w-6 h-6 text-primary" />
      </Button>
    </div>
    <div class="flex items-center mt-4">
      <User class="w-6 h-6 mr-2 text-nav-inactive" fill="currentColor" />
      <p class="text-black leading-7 font-medium outline-none">
        <span class="font-bold text-black">{participantsCount}</span> participants
      </p>
    </div>
    <div class="flex items-center mt-2">
      <Zap class="w-6 h-6 mr-2 text-nav-inactive" fill="currentColor" />
      <p class="text-black leading-7 font-medium outline-none">
        <span class="font-bold text-black">{gamesPlayedCount}</span> games played
      </p>
    </div>
    <div class="flex items-center mt-2">
      <Star class="w-6 h-6 mr-2 text-nav-inactive" fill="currentColor" />
      <p class="leading-7 font-medium outline-none"><span class="font-bold text-black">{customText}</span></p>
    </div>
  </div>

  <div class="flex flex-col justify-between ml-6 mt-4 md:mt-0">
    <div class="text-black text-right md:text-right mb-2">
      {formatDate(date)}
    </div>

    {#if customText === 'In progress'}
      <div class="flex justify-end mt-auto mb-5">
        <form method="POST">
          <input type="hidden" name="slug" value={slugify(title)} />
          <input type="hidden" name="action" value="archive" />
          <Button class="text-[20px] font-normal" type="submit">
            Finish tournament
            <Trophy class="w-6 h-6 ml-2 text-white" fill="currentColor" />
          </Button>
        </form>
      </div>
    {/if}
  </div>
</div>
