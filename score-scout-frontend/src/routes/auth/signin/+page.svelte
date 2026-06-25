<script lang="ts">
  import FullScreenForm from '../(components)/FullScreenForm.svelte';
  import AuthToggleBtn from '../(components)/AuthToggleBtn.svelte';
  import Input from '$lib/components/Input.svelte';
  import Button from '$lib/components/Button.svelte';
  import { LoaderCircle, SendHorizontal } from 'lucide-svelte';
  import { Field, Control, FieldErrors } from 'formsnap';
  import type { PageData } from './$types';
  import { schema } from './schema';
  import { zodClient } from 'sveltekit-superforms/adapters';
  import { superForm } from 'sveltekit-superforms';
  import Label from '$lib/components/Label.svelte';
  import { toast, Toaster } from 'svelte-sonner';
  import { customGoto } from '$lib/utils';
  import { onMount } from 'svelte';

  export let data: PageData;

  const form = superForm(data.form, {
    validators: zodClient(schema),
    onSubmit: () => {
      isLoading = true;
    },
    onResult: (event) => {
      isLoading = false;
      if (event.result.status === 200) {
        toast.success("You've successfully signed in!");
        if ('data' in event.result && typeof event.result.data === 'object' && 'token' in event.result.data)
          document.cookie = `token=${event.result.data.token}; path=/`;
        customGoto('/profile/active');
      } else {
        toast.error("Couldn't sign in, please try again.");
      }
    },
  });
  const { form: formData, enhance } = form;

  let isLoading = false;
  let mounted = false;

  onMount(() => {
    mounted = true;
  });
</script>

<svelte:head>
  <title>Sign in</title>
  <meta name="description" content="Sign in to your account" />
</svelte:head>

<FullScreenForm>
  <form
    method="POST"
    class="flex gap-2.5 flex-col p-10 w-full md:w-[560px] rounded-30 transition-all ease duration-300"
    class:shadow-form={mounted}
    class:border-subtle={!mounted}
    class:border={!mounted}
    use:enhance
  >
    <h1 class="text-4xl font-bold mb-7">Sign in</h1>
    <Field {form} name="email">
      <Control let:attrs>
        <Label>
          E-mail
          <Input {...attrs} bind:value={$formData.email} placeholder="Write your email" disabled={isLoading} class="w-full mt-1" />
        </Label>
      </Control>
      <FieldErrors class="text-red-500" />
    </Field>
    <Field {form} name="password">
      <Control let:attrs>
        <Label>
          Password
          <Input
            {...attrs}
            bind:value={$formData.password}
            placeholder="Write your password"
            disabled={isLoading}
            class="w-full mt-1"
            type="password"
          />
        </Label>
      </Control>
      <FieldErrors class="text-red-500" />
    </Field>
    <div class="flex justify-end w-full mt-2.5">
      <Button type="submit" disabled={isLoading}>
        {#if isLoading}
          <LoaderCircle class="w-5 animate-spin" />
        {:else}
          <SendHorizontal class="w-5" />
        {/if}
        Sign in
      </Button>
    </div>
  </form>
  <AuthToggleBtn text="I don't have an account" href="/auth/signup" />
</FullScreenForm>
<Toaster />
