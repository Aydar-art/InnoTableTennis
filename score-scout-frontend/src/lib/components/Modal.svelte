<script lang="ts">
  import { Dialog } from 'bits-ui';
  import { X } from 'lucide-svelte';
  import { fade, fly } from 'svelte/transition';

  export let open = false;
  export let title: string | undefined = undefined;
  export let description: string | undefined = undefined;
</script>

<Dialog.Root bind:open>
  <Dialog.Portal>
    <Dialog.Overlay transition={fade} transitionConfig={{ duration: 150 }} class="fixed inset-0 z-50 bg-black/80" />
    <Dialog.Content
      transition={fly}
      class="fixed left-[50%] top-[50%] z-50 w-full max-w-[94%] translate-x-[-50%] translate-y-[-50%] rounded-30 border bg-white p-5 shadow-popover outline-none sm:max-w-[490px] md:w-full"
    >
      {#if title}
        <Dialog.Title class="flex w-full items-start text-lg font-semibold tracking-tight text-primary">{title}</Dialog.Title>
      {/if}
      {#if description}
        <Dialog.Description class="text-sm text-foreground-hint py-2">
          {description}
        </Dialog.Description>
      {/if}
      <slot></slot>
      <Dialog.Close
        class="absolute right-5 top-5 rounded-md focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-foreground focus-visible:ring-offset-2 focus-visible:ring-offset-background active:scale-98"
      >
        <div>
          <X class="size-5 text-foreground" />
          <span class="sr-only">Close</span>
        </div>
      </Dialog.Close>
    </Dialog.Content>
  </Dialog.Portal>
</Dialog.Root>
