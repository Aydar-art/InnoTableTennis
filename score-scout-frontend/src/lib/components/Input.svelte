<script lang="ts">
  import type { HTMLInputAttributes } from 'svelte/elements';
  import { cn } from '$lib/utils.js';
  import { type VariantProps, tv } from 'tailwind-variants';

  type FormInputEvent<T extends Event = Event> = T & {
    currentTarget: EventTarget & HTMLInputElement;
  };

  interface InputEvents {
    blur: FormInputEvent<FocusEvent>;
    change: FormInputEvent<Event>;
    click: FormInputEvent<MouseEvent>;
    focus: FormInputEvent<FocusEvent>;
    focusin: FormInputEvent<FocusEvent>;
    focusout: FormInputEvent<FocusEvent>;
    keydown: FormInputEvent<KeyboardEvent>;
    keypress: FormInputEvent<KeyboardEvent>;
    keyup: FormInputEvent<KeyboardEvent>;
    mouseover: FormInputEvent<MouseEvent>;
    mouseenter: FormInputEvent<MouseEvent>;
    mouseleave: FormInputEvent<MouseEvent>;
    mousemove: FormInputEvent<MouseEvent>;
    paste: FormInputEvent<ClipboardEvent>;
    input: FormInputEvent<InputEvent>;
    wheel: FormInputEvent<WheelEvent>;
  }

  type $$Props = HTMLInputAttributes & VariantProps<typeof inputVariants>;
  type $$Events = InputEvents;

  let className: $$Props['class'] = undefined;
  export let value: $$Props['value'] = '';
  export { className as class };
  export let variant: $$Props['variant'] = 'primary';
  export let readonly: $$Props['readonly'] = false;

  const inputVariants = tv({
    base: 'rounded-20 p-2.5 px-5 text-foreground-primary text-lg md:text-xl leading-7 font-medium focus:border-primary outline-none transition-all placeholder-foreground-hint disabled:opacity-50 disabled:cursor-not-allowed',
    variants: {
      variant: {
        primary: 'border-2 border-secondary',
        shadowed: 'shadow-[0_0_4px_2px_#0000003f]',
      },
    },
    defaultVariants: {
      variant: 'primary',
    },
  });
</script>

<input
  class={cn(inputVariants({ variant, className }))}
  bind:value
  {readonly}
  on:blur
  on:change
  on:click
  on:focus
  on:focusin
  on:focusout
  on:keydown
  on:keypress
  on:keyup
  on:mouseover
  on:mouseenter
  on:mouseleave
  on:mousemove
  on:paste
  on:input
  on:wheel|passive
  {...$$restProps}
/>
