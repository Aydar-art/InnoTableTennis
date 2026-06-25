<script>
  import Navbar from '$lib/components/Navbar.svelte';
  import Page from '$lib/components/Page.svelte';
  import { customGoto } from '$lib/utils';
  import NavButtons from '../(components)/NavButtons.svelte';
  import TextBlock from '../(components)/TextBlock.svelte';
</script>

<Navbar
  title="Create tournament"
  links={[
    { name: 'Choose rating', href: '/create/rating' },
    { name: 'Add players', href: '/create/players' },
    { name: 'Number of stages', href: '/create/stages', active: true },
    { name: 'First stage', href: '/create/stage_1' },
    { name: 'Second stage', href: '/create/stage_2' },
  ]}
></Navbar>

<Page>
  <div class="flex h-screen w-full bg-white p-4 flex-col gap-5 pt-52 px-8">
    <div class="text-xl font-bold">Choose number of stages in your tournament</div>
    <div class="flex gap-6 flex-col md:flex-row">
      <a href="/create/stage_1">
        <TextBlock title="1 stage" class="p-4 md:pl-6 w-full h-fit md:w-[250px] md:h-[250px]">
          You will have only one stage. It can be groups or bracket.
        </TextBlock>
      </a>
      <a href="/create/stage_2">
        <TextBlock title="2 stage" class="p-4 md:pl-6 w-full h-fit md:w-[250px] md:h-[250px] ">
          You will have two stages. First stage is groups, the second can be groups or bracket. You can have several finals.
        </TextBlock>
      </a>
    </div>
  </div>
</Page>

<NavButtons
  prev={() => {
    customGoto('/create/players');
  }}
/>
