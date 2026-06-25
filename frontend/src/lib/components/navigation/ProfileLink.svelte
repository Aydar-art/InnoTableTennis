<script lang="ts">
	import Button from '$lib/components/base/Button.svelte';
	import ProfileIcon from '$lib/components/icons/ProfileIcon.svelte';

	import { isAdmin, playerInfo, username } from '$lib/client/stores/stores';
	import { base } from '$app/paths';
	import { enhance } from '$app/forms';

	let isMenuVisible = false;

	const toggleProfileMenu = () => {
		isMenuVisible = isMenuVisible ? false : true;
	};

	export let isAuthorized = false;

	let linkToProfile = `${base}/players/` + $username;
</script>

<div class="profile-container">
	{#if !isAuthorized}
		<div class="sign-in">
			<a id="nav-link-matches" href="{base}/login">Sign in</a>
		</div>
		<div class="sign-up">
			<a href="{base}/signup">
				<Button>Sign Up</Button>
			</a>
		</div>
	{:else}
		<div class="profile-button-container">
			<button class="profile-button" on:click={toggleProfileMenu}>
				<div class="profile-icon">
					<ProfileIcon />
				</div>
			</button>
			{#if isMenuVisible}
				<div class="profile-menu-container">
					<div class="profile-menu-wrapper">
						<div class="upper-subcontainer">
							<div class="name-tag">
								<div class="name">
									{#if $isAdmin}
										{$username}
									{:else}
										{$playerInfo?.name}
									{/if}
								</div>
								<div class="tag">@{$username}</div>
							</div>
							<div class="space-for-icon" />
						</div>
						<div class="lower-subcontainer">
							{#if !$isAdmin}
								<a href={linkToProfile}>
									<button class="open-profile-button" on:click={toggleProfileMenu}>Profile</button>
								</a>
							{/if}
							<form method="POST" action="/logout" class="log-out-form" use:enhance>
								<button class="log-out-button">Log Out</button>
							</form>
						</div>
					</div>
				</div>
				<button class="full-screen-button" on:click={toggleProfileMenu} />
			{/if}
		</div>
	{/if}
</div>

<style>
	.profile-container {
		display: flex;
		gap: 1.5em;
		height: 3rem;
		align-items: center;
	}
	.sign-in {
		height: 100%;
		width: 7rem;
	}
	.sign-up {
		width: 100%;
	}
	.profile-container a {
		text-decoration: none;
		display: flex;
		height: 100%;
		transition: 0.1s;
		align-items: center;
		justify-content: center;
		color: var(--content-color);
	}
	.profile-button {
		z-index: 3;
		background: var(--main-color);
		border: 0;
		cursor: pointer;
	}
	.profile-icon {
		width: 3rem;
		height: 3rem;
	}
	.profile-menu-container {
		z-index: 2;
		top: 0;
		right: 0;
		display: flex;
		flex-direction: row;
		position: relative;
		transform: translate(-94%, -5%);
		min-height: 8rem;
		height: fit-content;
		width: fit-content;
		min-width: 20rem;
		border: 1px solid black;
		border-radius: 10px;
		background: var(--main-color);
	}
	.profile-menu-wrapper {
		display: flex;
		box-sizing: border-box;
		width: fit-content;
		min-width: 20rem;
		min-height: 8rem;
		height: fit-content;
		flex-direction: column;
		padding: 0.5rem;
	}
	.upper-subcontainer {
		display: flex;
		flex-direction: row;
		min-height: 4rem;
		height: fit-content;
		align-items: center;
	}
	.lower-subcontainer {
		display: flex;
		flex-direction: row;
		width: 100%;
		height: 50%;
	}
	.name-tag {
		display: flex;
		flex-direction: column;
		align-items: flex-end;
		min-width: 70%;
		width: fit-content;
	}
	.space-for-icon {
		width: 30%;
		height: 100%;
	}
	.profile-button-container {
		position: relative;
		display: flex;
		flex-direction: row;
		height: 3rem;
		width: 3rem;
	}
	.name {
		font-size: var(--fontsize-large);
		text-align: end;
	}
	.tag {
		margin: 0.5rem;
	}
	.open-profile-button {
		border-radius: 10px;
		outline: none;
		border: 0;
		padding: 0.1rem 0rem;
		background-color: var(--secondary-color);
		color: white;
		font-weight: var(--fontweight-2);
		transition: 0.1s;
		width: 100%;
		height: 2rem;
		margin: 0.25rem;
	}

	.open-profile-button:hover {
		box-shadow: 0px 4px 4px 2px var(--not-chosen-font-color);
		cursor: pointer;
	}

	.log-out-form {
		width: 100%;
		height: 2rem;
		margin: 0.25rem;
	}

	.log-out-button {
		border-radius: 10px;
		outline: none;
		border: 2px solid var(--content-color);
		background: var(--main-color);
		color: var(--content-color);
		font-weight: var(--fontweight-2);
		padding: 0.1rem 0rem;
		transition: 0.1s;
		width: 100%;
		height: 100%;
	}
	.lower-subcontainer a {
		width: 100%;
		height: 2rem;
		margin: 0.25rem;
	}

	.log-out-button:hover {
		box-shadow: 0px 4px 4px 2px var(--not-chosen-font-color);
		cursor: pointer;
	}
	.full-screen-button {
		z-index: 1;
		background: none;
		border: none;
		position: fixed;
		right: 0;
		top: 0;
		height: 100vh;
		width: 100vw;
	}
	@media (min-width: 480px) {
		.profile-container .sign-in a:hover {
			border-bottom: 2px solid var(--content-color);
		}
	}
	@media (max-width: 800px) and (min-width: 480px) {
		.profile-container {
			gap: 0.5rem;
		}
		.sign-in {
			margin-top: 0.078125rem;
		}
		.profile-menu-container {
			transform: translate(-99%, -4%);
		}
	}
	@media (max-width: 480px) {
		.profile-container {
			display: block;
			height: 6rem;
		}
		.sign-in {
			height: 3rem;
		}
		.sign-up {
			height: 3rem;
		}
	}
</style>
