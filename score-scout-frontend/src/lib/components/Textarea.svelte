<script lang="ts">
  import type { HTMLTextareaAttributes } from 'svelte/elements';
  import { cn } from '$lib/utils.js';

  type FormTextareaEvent<T extends Event = Event> = T & {
    currentTarget: EventTarget & HTMLTextAreaElement;
  };

  type TextareaEvents = {
    blur: FormTextareaEvent<FocusEvent>;
    change: FormTextareaEvent<Event>;
    click: FormTextareaEvent<MouseEvent>;
    focus: FormTextareaEvent<FocusEvent>;
    keydown: FormTextareaEvent<KeyboardEvent>;
    keypress: FormTextareaEvent<KeyboardEvent>;
    keyup: FormTextareaEvent<KeyboardEvent>;
    mouseover: FormTextareaEvent<MouseEvent>;
    mouseenter: FormTextareaEvent<MouseEvent>;
    mouseleave: FormTextareaEvent<MouseEvent>;
    paste: FormTextareaEvent<ClipboardEvent>;
    input: FormTextareaEvent<InputEvent>;
  };

  type $$Props = HTMLTextareaAttributes;
  type $$Events = TextareaEvents;

  let className: $$Props['class'] = undefined;
  export let value: $$Props['value'] = undefined;
  export { className as class };
  export let readonly: $$Props['readonly'] = undefined;
</script>

<span
  class={cn('relative textarea-wrapper after:text-foreground-hint', className)}
  data-current-length={typeof value === 'string' ? value.length : 0}
  data-max-length={$$props.maxlength}
>
  <textarea
    class={cn(
      'transition-all ease resize-none outline-none flex min-h-[150px] w-full rounded-20 border-2 border-secondary bg-white px-6 py-4 text-xl placeholder:text-text-hint focus-visible:border-primary disabled:cursor-not-allowed disabled:opacity-50',
      className
    )}
    bind:value
    {readonly}
    on:blur
    on:change
    on:click
    on:focus
    on:keydown
    on:keypress
    on:keyup
    on:mouseover
    on:mouseenter
    on:mouseleave
    on:paste
    on:input
    {...$$restProps}
  ></textarea>
</span>

<style lang="postcss">
  .textarea-wrapper:after {
    content: attr(data-current-length) '/' attr(data-max-length);
    position: absolute;
    right: 15px;
    bottom: 5px;
    font-size: 14px;
    margin: auto;
    font-family: Nunito;
  }

  .textarea-wrapper textarea::-webkit-scrollbar {
    width: 10px;
  }
</style>
