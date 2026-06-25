<script lang="ts">
  import Checkbox from '$lib/components/Checkbox.svelte';
  import Button from '$lib/components/Button.svelte';
  import Input from '$lib/components/Input.svelte';
  import Select from '$lib/components/Select.svelte';
  import Label from '$lib/components/Label.svelte';
  import Textarea from '$lib/components/Textarea.svelte';
  import Switch from '$lib/components/Switch.svelte';
  import Navbar from '$lib/components/Navbar.svelte';
  import Page from '$lib/components/Page.svelte';
  import { Plus, LogOut, CircleCheck } from 'lucide-svelte';
  import { customGoto } from '$lib/utils';
  import Modal from '$lib/components/Modal.svelte';

  let dialogOpen = false;
</script>

<svelte:head>
  <title>Example</title>
  <meta name="description" content="Example page with basic components" />
</svelte:head>

<Navbar
  title="Example page"
  links={[
    { name: 'Home', href: '/' },
    { name: 'About', href: '/about' },
    { name: 'Examples', href: '/example', active: true },
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
    <div class="py-4 md:p-0 md:grid grid-cols-2 gap-16">
      <div class="col-span-1 border-2 border-subtle my-4 md:mt-0 md:border-none md:shadow-form rounded-30 p-8">
        <h1 class="text-3xl font-bold text-center">Buttons</h1>
        <div class="flex gap-2 mt-4 justify-center">
          <Button>Primary</Button>
          <Button variant="secondary">Secondary</Button>
        </div>
        <div class="flex gap-2 mt-2 justify-center">
          <Button variant="ghost">Ghost</Button>
          <Button disabled>Primary</Button>
        </div>
        <div class="flex gap-2 mt-2 justify-center">
          <Button variant="ghost" disabled>Ghost</Button>
          <Button variant="secondary" disabled>Secondary</Button>
        </div>
      </div>
      <div class="col-span-1 border-2 border-subtle my-4 md:mt-0 md:border-none md:shadow-form rounded-30 p-8">
        <h1 class="text-3xl font-bold text-center">Checkbox</h1>
        <div class="flex gap-2 mt-4 justify-center">
          <Checkbox />
          <Checkbox checked />
        </div>
        <div class="flex gap-2 mt-4 justify-center">
          <Checkbox disabled />
          <Checkbox disabled checked />
        </div>
      </div>
      <div class="col-span-1 border-2 border-subtle my-4 md:mt-0 md:border-none md:shadow-form rounded-30 p-8 flex flex-col gap-2 items-center">
        <h1 class="text-3xl font-bold text-center mb-2">Inputs</h1>
        <Label class="w-full">
          Label
          <Input class="w-full mt-1" placeholder="Placeholder" value="Lorem ipsum dolor sit amet consectetur adipisicing elit." />
        </Label>
        <Input class="w-full" placeholder="Disabled" disabled />
      </div>
      <div class="col-span-1 border-2 border-subtle my-4 md:mt-0 md:border-none md:shadow-form rounded-30 p-8 flex flex-col gap-2 items-center">
        <h1 class="text-3xl font-bold text-center mb-2">Select</h1>
        <Select options={['Option 1', 'Option 2', 'Option 3']} class="w-full" />
      </div>
      <div class="col-span-1 border-2 border-subtle my-4 md:mt-0 md:border-none md:shadow-form rounded-30 p-8 flex flex-col gap-2 items-center">
        <h1 class="text-3xl font-bold text-center mb-2">Textarea</h1>
        <Textarea class="w-full" placeholder="Placeholder" value="Lorem ipsum dolor sit amet consectetur adipisicing elit." maxlength={350} />
      </div>
      <div class="col-span-1 border-2 border-subtle my-4 md:mt-0 md:border-none md:shadow-form rounded-30 p-8 flex flex-col gap-2 items-center">
        <h1 class="text-3xl font-bold text-center mb-2">Switch</h1>
        <div class="flex gap-2 mt-4 justify-center">
          <Switch />
          <Switch checked />
        </div>
        <div class="flex gap-2 mt-4 justify-center">
          <Switch disabled />
          <Switch disabled checked />
        </div>
      </div>
      <div class="col-span-1 border-2 border-subtle my-4 md:mt-0 md:border-none md:shadow-form rounded-30 p-8 flex flex-col gap-2 items-center">
        <h1 class="text-3xl font-bold text-center mb-2">Modal</h1>
        <Button on:click={() => (dialogOpen = true)}>Open</Button>
        <Modal
          title="Test modal windows"
          description="You can put any content here. To close it click the X button on top or click outside the modal window."
          bind:open={dialogOpen}
        >
          <div class="flex flex-row py-4">
            <Label class="text-right px-2 items-center">Some name</Label>
            <Input class="w-[8rem] h-[2em]" />
            <Input class="w-[8rem] h-[2em]" />
            <Label class="text-left px-2 items-center">Another name</Label>
          </div>
          <div class="flex justify-center">
            <Button on:click={() => (dialogOpen = false)}
              >Confirm
              <CircleCheck />
            </Button>
          </div>
        </Modal>
      </div>
    </div>
  </div>
</Page>
