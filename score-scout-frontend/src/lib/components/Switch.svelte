<script lang="ts">
  import { Switch as Switch } from 'bits-ui';
  import { cn } from '$lib/utils.js';

  type $$Props = Switch.Props;
  type $$Events = Switch.Events;

  let className: $$Props['class'] = undefined;
  export let checked: $$Props['checked'] = undefined;
  export { className as class };
</script>

<Switch.Root
  bind:checked
  class={cn(
    'peer inline-flex h-[32px] w-[60px] shrink-0 cursor-pointer items-center rounded-full border-2 border-transparent transition-colors outline-none disabled:cursor-not-allowed disabled:opacity-50 data-[state=checked]:bg-primary data-[state=unchecked]:bg-secondary',
    className
  )}
  {...$$restProps}
  on:click
  on:keydown
>
  <Switch.Thumb
    class={cn(
      'pointer-events-none block ml-[3px] h-[22px] w-[22px] rounded-full bg-white transition-transform data-[state=checked]:translate-x-[28px] data-[state=unchecked]:translate-x-0'
    )}
  />
</Switch.Root>
