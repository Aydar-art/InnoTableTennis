<script lang="ts">
  import Button from '$lib/components/Button.svelte';
  import { ArrowLeft, ArrowRight } from 'lucide-svelte';

  export let next: (() => void) | undefined = undefined;
  export let prev: (() => void) | undefined = undefined;
</script>

<div class="flex flex-row pb-[20px] mr-12 fixed bottom-0 right-0">
  {#if prev !== undefined}
    <Button class="mr-4" variant="secondary" on:click={prev}>
      <ArrowLeft class="w-6 h-6" />
      Back
    </Button>
  {/if}
  {#if next !== undefined}
    <Button on:click={next}>
      Next
      <ArrowRight class="w-6 h-6" />
    </Button>
  {/if}
</div>
