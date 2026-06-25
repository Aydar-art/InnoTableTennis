<script>
  import { page } from '$app/stores';
  import { derived } from 'svelte/store';
  import { User } from 'lucide-svelte';

  const currentPageStore = derived(page, ($page) => $page.url.pathname);
</script>

<div class="absolute top-5 right-10">
  <a
    href="/profile/active"
    class="flex items-center justify-center rounded-full {$currentPageStore.includes('/profile') ? 'bg-primary' : 'bg-nav-inactive'} w-10 h-10"
  >
    {#if $currentPageStore.includes('/profile')}
      <User class="w-6 h-6 text-white" fill="currentColor" />
    {:else}
      <User class="w-6 h-6 text-white" fill="currentColor" />
    {/if}
  </a>
</div>
