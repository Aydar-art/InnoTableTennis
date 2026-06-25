<script lang="ts">
  import { cn } from '$lib/utils';

  export let title: string;

  type $$Props = {
    title: string;
    class?: string | undefined;
  };

  let className: $$Props['class'] = undefined;
  export { className as class };
</script>

<div
  class={cn(
    'hover rounded-[16px] shadow-[0_0_4px_2px_#0000003f] text-primary transition-colors ease-in-out hover:text-white hover:bg-primary group',
    className
  )}
>
  <div class="text-xl font-bold">{title}</div>
  <p class="text-black group-hover:text-white text-base leading-7 font-medium outline-none text-placeholder-foreground-hint">
    <slot />
  </p>
</div>
