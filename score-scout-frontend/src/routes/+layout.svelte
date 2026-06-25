<script>
  import '../app.css';
  import { LoaderCircle } from 'lucide-svelte';
  import { fade } from 'svelte/transition';
  import { navigating, page } from '$app/stores';
  import ProfileIcon from '$lib/components/ProfileIcon.svelte';

  let showProfileIcon = true;

  $: {
    showProfileIcon = !$page.url.pathname.includes('/auth/');
  }
</script>

{#if showProfileIcon}
  <ProfileIcon />
{/if}

{#if $navigating}
  <div class="fixed w-full h-full backdrop-blur-xl bg-black bg-opacity-50 z-50" in:fade={{ duration: 100 }} out:fade={{ duration: 100 }}>
    <div class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex items-center gap-2">
      <LoaderCircle class="text-red-100 w-10 h-10 animate-spin" />
    </div>
  </div>
{/if}

<slot />
