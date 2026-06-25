<script lang="ts">
	import { handleError } from '$lib/client/handleError.js';
	import Login from '$lib/components/navigation/Login.svelte';

	export let form;

	$: if (form?.error) {
		handleError(form.error);
	}
</script>

<svelte:head>
	<title>Login</title>
	<meta
		name="description"
		content="Login to Your TableTennis account to access tournaments, players and profile. Sign in to your account securely and start exploring the site."
	/>
	<meta
		name="keywords"
		content="login, member login, account login, sign in, secure login, content"
	/>
</svelte:head>

<Login />
