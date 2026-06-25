<script lang="ts">
  import { ArrowRight } from 'lucide-svelte';

  export let text: string;
  export let href: string;
</script>

<a {href} class="p-4 font-semibold text-xl flex items-center gap-2 transform hover:scale-102 transition-transform ease">
  {text}
  <ArrowRight />
</a>
