<script lang="ts">
  import { Checkbox } from 'bits-ui';
  import { Check } from 'lucide-svelte';
  import { cn } from '$lib/utils.js';

  type $$Props = Checkbox.Props;
  type $$Events = Checkbox.Events;

  let className: $$Props['class'] = undefined;
  export let checked: $$Props['checked'] = false;
  export { className as class };
</script>

<Checkbox.Root
  class={cn(
    'peer box-content h-6 w-6 shrink-0 rounded-5 border-2 border-toggle-border shadow focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 data-[disabled=true]:cursor-not-allowed data-[disabled=true]:opacity-50',
    className
  )}
  bind:checked
  on:click
  {...$$restProps}
>
  <Checkbox.Indicator class={cn('flex h-full w-full items-center justify-center text-current')} let:isChecked>
    <Check class={cn('h-full w-full transition-all ease text-icon-muted', !isChecked && 'text-transparent')} />
  </Checkbox.Indicator>
</Checkbox.Root>
