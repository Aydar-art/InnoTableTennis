<script lang="ts">
	import Button from '$lib/components/base/Button.svelte';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import signin from '$lib/assets/signin.jpg';
	import NewLeftArrow from '$lib/components/icons/NewLeftArrow.svelte';
	import { base } from '$app/paths';
	import { enhance } from '$app/forms';

	let username = '';
	let password = '';
</script>

<button
	class="back-btn"
	on:click={() => {
		window.history.back();
	}}
>
	<div class="arrow">
		<NewLeftArrow />
	</div>
	Back
</button>

<div class="container">
	<div class="row">
		<div class="container">
			<div class="wrapper-left">
				<div class="column">
					<form method="POST" use:enhance>
						<h2>Sign in</h2>
						<InputTemplate
							type="text"
							name="username"
							bind:stringVal={username}
							placeholder="@Telegram alias"
							required={true}
							isFirst={true}
						/>
						<InputTemplate
							type="password"
							name="password"
							bind:stringVal={password}
							placeholder="password"
							required={true}
							isFirst={false}
						/>
						<div class="button-container">
							<Button dark={true} type={'submit'}>Sign In</Button>
						</div>
						<a class="sp-link" href="{base}/signup">I don't have an account</a>
					</form>
				</div>
			</div>
		</div>
		<div class="wrapper-right">
			<div class="container">
				<img class="backgroundImage" src={signin} alt="" />
			</div>
		</div>
	</div>
</div>

<style>
	h2 {
		font-size: var(--fontsize-x-large);
		margin-bottom: 0.5em;
		text-align: end;
		align-self: start;
		color: inherit;
	}
	form {
		color: var(--content-color);
		width: 100%;
		height: 100%;
		margin: 0 auto;
		font-size: var(--fontsize-medium1);
		display: flex;
		flex-direction: column;
		gap: 1rem;
	}

	.row {
		display: flex;
		flex-direction: row;
		width: 100%;
		height: 100%;
	}
	.sp-link {
		padding-top: 1.5em;
		color: var(--secondary-color);
		text-align: center;
		padding-bottom: 1rem;
	}
	.container {
		color: var(--content-color);
		display: flex;
		flex-direction: column;
		width: 100%;
		height: 100vh;
		align-content: center;
		justify-content: center;
	}
	.wrapper-right {
		display: flex;
		width: 50vw;
		height: 100vh;
		align-items: start;
	}
	.wrapper-left {
		display: flex;
		flex-direction: column;
		padding: 4em;
		box-sizing: border-box;
		overflow-y: scroll;
		width: 50vw;
	}
	.wrapper-left::-webkit-scrollbar {
		display: none;
	}
	.wrapper-left {
		-ms-overflow-style: none;
		scrollbar-width: none;
	}
	.column {
		display: flex;
		flex-direction: column;
		width: 100%;
		height: 100%;
		align-content: center;
		justify-content: center;
	}
	.button-container {
		width: 10rem;
		align-self: flex-end;
	}
	.backgroundImage {
		height: 100vh;
		object-fit: cover;
	}
	.back-btn {
		position: absolute;
		left: 5%;
		top: 5%;
		width: 10rem;
		color: var(--secondary-color);
		font-size: 1.5em;
		background-color: var(--main-color);
		display: flex;
		flex-direction: row;
		border: 0;
		text-decoration: underline;
		justify-content: center;
		align-items: center;
	}
	.arrow {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 1rem;
		height: 1rem;
	}
	@media (max-width: 480px) {
		.container {
			height: 100vh;
		}
		.wrapper-right {
			display: none;
		}
		.wrapper-left {
			width: 100vw;
		}
		.back-btn {
			width: 20%;
			top: 1%;
			left: 8%;
		}
	}
	@media (max-width: 850px) {
		.wrapper-left {
			width: 100vw;
		}
		.wrapper-right {
			display: none;
		}
	}
</style>
