<script lang="ts">
  import Button from '$lib/components/Button.svelte';
  import Navbar from '$lib/components/Navbar.svelte';
  import Page from '$lib/components/Page.svelte';
  import { Plus, LogOut } from 'lucide-svelte';
  import { customGoto } from '$lib/utils';
</script>

<svelte:head>
  <title>Main page</title>
  <meta name="description" content="Main page" />
</svelte:head>

<Navbar
  title="Home page"
  links={[
    { name: 'Home', href: '/', active: true },
    { name: 'About', href: '/about' },
    { name: 'Examples', href: '/example' },
    { name: 'Sign In', href: '/auth/signin' },
    { name: 'Sign Up', href: '/auth/signup' },
  ]}
>
  <div slot="before" class="flex justify-end md:justify-start">
    <Button variant="ghost" class="mt-10 mx-6 md:mx-10 text-2xl md:text-lg gap-3 md:gap-2 flex items-center justify-center">
      <LogOut class="w-8 h-8 md:w-6 md:h-6" />
      Logout
    </Button>
  </div>
  <div slot="after" class="flex justify-end">
    <Button
      class="rounded-full mb-8 mr-8 text-xl md:text-base leading-5 gap-2.5 flex items-center justify-center"
      on:click={() => {
        customGoto('/create/rating');
      }}
    >
      <Plus class="w-7 h-7 md:w-5 md:h-5" />
      Create tournament
    </Button>
  </div>
</Navbar>
<Page>
  <div class="h-screen w-full bg-white p-4 text-xl">
    <p class="text-primary font-bold mb-4 mt-16">Welcome to the Home page!</p>
    <p class="mb-2">In the menu to the right, you can find the following options:</p>
    <ul>
      <li><span class="text-primary font-medium">Home</span> - navigate to the home page</li>
      <li><span class="text-primary font-medium">About</span> - learn more about the website and developers</li>
      <li><span class="text-primary font-medium">Examples</span> - explore some example content</li>
      <li><span class="text-primary font-medium">Sign In</span> - sign in to your account</li>
      <li><span class="text-primary font-medium">Sign Up</span> - create a new account</li>
    </ul>
  </div>
</Page>
