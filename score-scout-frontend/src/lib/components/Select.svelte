<script lang="ts">
  import type { HTMLAttributes } from 'svelte/elements';
  import { cn } from '$lib/utils.js';
  import { Select, type Selected } from 'bits-ui';
  import { ChevronDown, Check } from 'lucide-svelte';
  import { fly } from 'svelte/transition';

  export let options: string[] = [];
  export let selected: string | undefined = undefined;

  const optionsDict = options.map((option) => ({
    value: option,
    label: option,
  }));

  type $$Props = HTMLAttributes<HTMLSelectElement> & {
    options?: string[];
    selected?: string;
  };
  type $$Events = Required<Select.ItemEvents>;

  let className: $$Props['class'] = undefined;
  export { className as class };

  let isOpen = false;
  let currentSelect: Selected<string> | undefined = undefined;
  if (selected) {
    currentSelect = optionsDict.find((option) => option.value === selected);
  }

  function handleSelectedChange(selectedOption: Selected<string> | undefined) {
    currentSelect = selectedOption;
    selected = selectedOption?.value;
  }
</script>

<div class={cn('relative', className)}>
  <Select.Root items={optionsDict} onOpenChange={(open) => (isOpen = open)} selected={currentSelect} onSelectedChange={handleSelectedChange}>
    <Select.Trigger
      class="py-15px px-5 outline-none flex items-center justify-between w-full bg-white border-2 transition-all ease rounded-20 {isOpen
        ? 'border-primary'
        : 'border-secondary'}"
      aria-label="Select a theme"
    >
      <Select.Value class="text-xl font-medium leading-6 text-foreground-hint" placeholder="Choose one" />
      <ChevronDown class="ml-auto size-6 text-icon-muted" />
    </Select.Trigger>
    <Select.Content
      class="mt-0.5 w-[calc(100% + 40px)] rounded-20 border-2 border-primary bg-white outline-none"
      transition={fly}
      transitionConfig={{ duration: 150, y: 20 }}
      sideOffset={0}
    >
      {#each optionsDict as option}
        <Select.Item
          class="flex w-full select-none items-center text-xl text-foreground-select-option leading-6 py-15px px-5 outline-none transition-all data-[highlighted]:bg-muted hover:bg-gray-300 last:rounded-br-20 last:rounded-bl-20 first:rounded-tl-20 first:rounded-tr-20"
          value={option.value}
          label={option.label}
          on:click
          on:pointermove
          on:focusin
        >
          {option.label}
          <Select.ItemIndicator class="ml-auto" asChild={false}>
            <Check />
          </Select.ItemIndicator>
        </Select.Item>
      {/each}
    </Select.Content>
    <Select.Input name="favoriteFruit" />
  </Select.Root>
</div>
