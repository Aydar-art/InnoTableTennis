<script lang="ts">
  import { cn } from '$lib/utils.js';
  import { Button } from 'bits-ui';
  import { type VariantProps, tv } from 'tailwind-variants';

  type $$Props = Button.Props & VariantProps<typeof buttonVariants>;
  type $$Events = Button.Events;

  let className: $$Props['class'] = undefined;
  export let variant: $$Props['variant'] = 'primary';
  export let builders: $$Props['builders'] = [];
  export { className as class };

  const buttonVariants = tv({
    base: 'font-bold text-xl leading-6 flex items-center justify-center p-2.5 px-5 rounded-20 gap-3 transition-all ease transform active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed',
    variants: {
      variant: {
        primary: 'text-white bg-primary hover:bg-primary-hover',
        secondary: 'text-primary bg-transparent border-2 border-primary hover:bg-secondary-btn-hover',
        ghost: 'text-primary bg-transparent hover:underline',
      },
    },
    defaultVariants: {
      variant: 'primary',
    },
  });
</script>

<Button.Root {builders} class={cn(buttonVariants({ variant, className }))} type="button" {...$$restProps} on:click on:keydown>
  <slot />
</Button.Root>
