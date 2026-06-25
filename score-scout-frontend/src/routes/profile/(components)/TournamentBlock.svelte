<script lang="ts">
  import { cn } from '$lib/utils.js';
  import { User, Zap, Star, Trash } from 'lucide-svelte';
  import Button from '$lib/components/Button.svelte';
  import Input from '$lib/components/Input.svelte';

  type $$Props = {
    title: string;
    slug: string;
    date: Date | string;
    class?: string | undefined;
    participantsCount: number;
    gamesPlayedCount: number;
    customText: string;
  };

  export let title: string;
  export let slug: string;
  export let date: Date | string;
  export let participantsCount: number;
  export let gamesPlayedCount: number;
  export let customText: string;

  let className: $$Props['class'] = undefined;
  export { className as class };

  function formatDate(date: Date | string) {
    if (typeof date === 'string') {
      return new Date(date).toLocaleDateString();
    }
    return date.toLocaleDateString();
  }

  function kostil(event: Event) {
    if ((event.target as HTMLElement).tagName === 'BUTTON') {
      event.preventDefault();
      (event.target as HTMLElement).closest('form')?.submit();
    }
  }
</script>

<a href={`/tournament/${slug}`} class={cn('shadow-[0_0_4px_2px_#0000003f] hover pl-6 pr-6 rounded-[16px] py-5', className)} on:click={kostil}>
  <div class="flex flex-wrap justify-between items-center">
    <div class="text-2xl font-bold">{title}</div>
    <div class="text-foreground-select-option mt-1">{formatDate(date)}</div>
  </div>
  <div class="flex items-center mt-4">
    <User class="w-6 h-6 mr-2 text-nav-inactive" fill="currentColor" />
    <p class="text-foreground-select-option leading-7 font-medium outline-none">
      <span class="font-bold text-black">{participantsCount}</span> participants
    </p>
  </div>
  <div class="flex items-center mt-2">
    <Zap class="w-6 h-6 mr-2 text-nav-inactive" fill="currentColor" />
    <p class="text-foreground-select-option leading-7 font-medium outline-none">
      <span class="font-bold text-black">{gamesPlayedCount}</span> games played
    </p>
  </div>
  <div class="flex flex-wrap justify-between items-center">
    <div class="flex items-center mt-2">
      <Star class="w-6 h-6 mr-2 text-nav-inactive" fill="currentColor" />
      <p class="leading-7 font-medium outline-none">{customText}</p>
    </div>
    <form method="POST">
      <Input class="hidden" name="slug" value={slug} />
      <Button type="submit" variant="ghost" class="text-sm">
        <Trash class="w-6 h-6" />
        Delete
      </Button>
    </form>
  </div>
</a>
