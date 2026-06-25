<script lang="ts">
  import Button from '$lib/components/Button.svelte';
  import Navbar from '$lib/components/Navbar.svelte';
  import Page from '$lib/components/Page.svelte';
  import { Plus, LogOut } from 'lucide-svelte';
  import { customGoto } from '$lib/utils';
</script>

<Navbar
  title="About page"
  links={[
    { name: 'Home', href: '/' },
    { name: 'About', href: '/about', active: true },
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
  <div class="h-screen w-full bg-white p-4">
    <div class="text-2xl font-bold text-primary mt-16">About</div>
    <div class="text-lg mt-4">
      We are a team of Innopolis University students who are passionate about web development and software engineering. Our goal is to create a
      platform for organizing and participating in tournaments. The project is open-source, written in SvelteKit, TailwindCSS, TypeScript and PostCSS.
      It uses SQLite for the database. You can find the source code on our
      <a href="https://github.com/InnoTableTennis/score-scout-frontend" class="text-primary">GitHub repository</a> under the GNU Affero General Public
      License v3.0.
    </div>
  </div></Page
>
