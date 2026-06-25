<script lang="ts">
	import { base } from '$app/paths';
	import signup from '$lib/assets/signup.jpg';
	import NewRightArrow from '$lib/components/icons/NewRightArrow.svelte';
</script>

<svelte:head>
	<title>Sign up</title>
	<meta
		name="description"
		content="Join InnoTableTennis Club and get added to the players' list. Obtain your password via the Telegram bot after adding your Telegram alias. Log in with your password to access exclusive services and features. Already have an account? Log in and enjoy the thrill of table tennis at Innopolis University."
	/>
	<meta
		name="keywords"
		content="sign up, join club, table tennis, Innopolis, Inno, University, InnoTableTennis, Innopolis University, players' list, exclusive services, Telegram bot"
	/>
</svelte:head>

<button
	class="back-btn"
	on:click={() => {
		window.history.back();
	}}
>
	Back
	<div class="arrow">
		<NewRightArrow />
	</div>
</button>

<div class="container">
	<div class="row">
		<div class="wrapper-left">
			<div class="container">
				<img class="backgroundImage" src={signup} alt="Problem with downloading" />
			</div>
		</div>
		<div class="container">
			<div class="wrapper-right">
				<div class="column">
					<h2>Sign Up</h2>
					<div class="index">01</div>
					<p>
						Contact the <a
							class="link"
							href="https://t.me/+pSAqcDY6eqM5Mjg6"
							target="_blank"
							rel="noreferrer">club</a
						>
						leaders to add yourself to the players' list. Check the
						<a class="link" href="{base}/">matches section</a> to confirm if you're already added.
					</p>
					<div class="index">02</div>
					<p>
						Use the <a
							class="link"
							href="https://t.me/InnoTableTennisBot"
							target="_blank"
							rel="noreferrer">telegram bot</a
						> to get your password once your telegram alias is added.
					</p>
					<div class="index">03</div>
					<p>
						<a class="link" href="{base}/login">Log in</a> to the website using your password to access
						the services.
					</p>
					<a class="sp-link" href="{base}/login">Already have an account</a>
				</div>
			</div>
		</div>
	</div>
</div>

<style>
	h2 {
		font-size: var(--fontsize-x-large);
		margin-bottom: 0.5em;
		text-align: end;
		color: inherit;
	}
	p {
		text-align: end;
		color: inherit;
		font-size: var(--fontsize-large);
		color: var(--content-color);
		margin-bottom: 0.5em;
		padding-right: 0.5em;
	}

	.row {
		display: flex;
		flex-direction: row;
		width: 100%;
		height: 100%;
	}

	.link {
		color: var(--secondary-color);
	}

	.sp-link {
		padding-top: 1.5em;
		color: var(--secondary-color);
		text-align: center;
		width: 100%;
		padding-bottom: 1rem;
	}

	.index {
		text-align: end;
		color: var(--secondary-bg-color);
		font-size: 6em;
		margin-bottom: -0.5em;
	}

	.container {
		color: var(--content-color);
		display: flex;
		flex-direction: column;
		width: 100%;
		max-height: 100vh;
		align-content: center;
		justify-content: center;
	}
	.wrapper-right {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
		padding: 4em 3em 3em 7em;
		width: 100%;
		height: 100vh;
		overflow-y: scroll;
	}
	.wrapper-right::-webkit-scrollbar {
		display: none;
	}
	.wrapper-right {
		-ms-overflow-style: none;
		scrollbar-width: none;
	}

	.wrapper-left {
		display: flex;
		width: 50vw;
		align-items: start;
	}

	.column {
		display: flex;
		flex-direction: column;
		width: 100%;
		height: 100%;
		align-content: center;
		justify-content: center;
	}
	.backgroundImage {
		height: 100vh;
		object-fit: cover;
	}
	.back-btn {
		position: absolute;
		right: 5%;
		top: 3%;
		color: var(--secondary-color);
		font-size: 1.5em;
		background-color: var(--main-color);
		border: 0;
		text-decoration: underline;
		display: flex;
		flex-direction: row;
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
	@media (max-width: 840px) {
		.row {
			font-size: small;
		}
		p {
			font-size: small;
		}
		.wrapper-right {
			width: 100vw;
		}
		.wrapper-left {
			display: none;
		}
	}

	@media (max-width: 480px) {
		.wrapper-left {
			display: none;
		}
		.arrow {
			display: flex;
			justify-content: center;
			align-items: center;
			width: 1rem;
			height: 1rem;
		}
		.back-btn {
			left: 75%;
			top: 0.5%;
		}
		.container {
			height: 100vh;
		}
	}
</style>
