<script lang="ts">
	import ProfileLink from '$lib/components/navigation/ProfileLink.svelte';
	import { isAdmin, isLeader, playerInfo, username } from '$lib/client/stores/stores';
	import ToggleTheme from '$lib/components/ToggleTheme.svelte';
	import { base } from '$app/paths';
	import { enhance } from '$app/forms';

	export let isAuthorized = false;

	$: linkToProfile = `${base}/players/` + $playerInfo?.telegramAlias;
</script>

<nav>
	<input type="checkbox" />
	<div class="hamburger-lines">
		<div class="line line1" />
		<div class="line line2" />
		<div class="line line3" />
	</div>
	<div class="nav-container">
		<div class="toggle-theme-container">
			<ToggleTheme />
		</div>
		{#if isAuthorized}
			<div class="for-mobile">
				<div class="prof-info">
					<div class="user-name">
						{#if $isAdmin}
							{$username}
						{:else}
							{$playerInfo?.name}
						{/if}
					</div>
					<div class="user-alias">
						@{$username}
					</div>
				</div>
			</div>
		{/if}
		<ul class="nav-links">
			{#if $isLeader}
				<li><a href="{base}/admin">Admin panel</a></li>
			{/if}
			<li><a href="{base}/">Matches</a></li>
			<li><a href="{base}/tournaments">Tournaments</a></li>
			<li><a href="{base}/players">Players</a></li>
			{#if isAuthorized}
				<li class="for-mobile"><a href={linkToProfile}>Profile</a></li>
				<li>
					<form method="POST" action="/logout" use:enhance>
						<button class="log-out-button">Log Out</button>
					</form>
				</li>
			{/if}
		</ul>
		<div class="prof-link-container">
			<div class="for-pc">
				<ProfileLink {isAuthorized} />
			</div>
			{#if !isAuthorized}
				<div class="for-mobile">
					<ProfileLink {isAuthorized} />
				</div>
			{/if}
		</div>
	</div>
</nav>

<style>
	.for-mobile {
		display: none;
	}
	.log-out-button {
		display: none;
		border-radius: 10px;
		outline: none;
		box-sizing: border-box;
		border: 2px solid var(--content-color);
		padding: 0.1rem 0rem;
		background: var(--main-color);
		color: var(--content-color);
		font-weight: var(--fontweight-2);
		transition: 0.1s;
		width: 100%;
		height: 2rem;
		margin-top: 0.5rem;
	}

	.log-out-button:hover {
		box-shadow: 0px 4px 4px 2px var(--not-chosen-font-color);
		cursor: pointer;
	}
	input[type='checkbox'] {
		display: none;
		margin: 0;
	}
	nav {
		padding: 0 2rem;
		display: flex;
		justify-content: space-between;
		height: 100%;
		align-items: center;
		max-width: 1440px;
		margin: 0 auto;
	}
	.nav-container {
		display: flex;
		height: 100%;
		align-items: center;
	}
	.nav-links {
		height: 4em;
		justify-content: center;
		display: flex;
	}
	.nav-links li {
		white-space: nowrap;
		text-decoration: none;
		margin-left: 1.5em;
		margin-right: 1.5em;
		height: 100%;
		line-height: 4em;
	}

	.nav-links li a {
		text-decoration: none;
		display: flex;
		height: 100%;
		color: var(--content-color);
		transition: 0.1s;
		text-align: center;
		justify-content: center;
	}
	.nav-links li a:hover {
		border-bottom: 2px solid var(--content-color);
	}

	@media (min-width: 480px) and (max-width: 800px) {
		.nav-links li {
			text-decoration: none;
			margin-left: 0.7rem;
			margin-right: 0.7rem;
			height: 100%;
			line-height: 4em;
		}
		nav {
			padding: 0 1rem;
		}
	}

	@media (max-width: 480px) {
		.for-mobile {
			display: block;
		}
		.log-out-button {
			display: block;
		}
		.for-pc {
			display: none;
		}
		.hamburger-lines {
			height: 1.5rem;
			width: 2rem;
			right: 1rem;
			top: 1rem;
			position: fixed;
			z-index: 2;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			transition: all 0.5s ease-in-out;
		}
		.hamburger-lines {
			padding: 0.3rem;
			border-radius: 5px;
			background-color: var(--main-color);
			opacity: 0.8;
		}
		.hamburger-lines .line {
			display: block;
			height: 4px;
			width: 100%;
			border-radius: 10px;
			background: var(--content-color);
		}
		input[type='checkbox']:checked ~ .hamburger-lines {
			transform: rotate(-90deg);
		}
		input[type='checkbox'] {
			background-color: var(--main-color);
			position: fixed;
			display: block;
			border: none;
			z-index: 3;
			height: 1.5rem;
			width: 2rem;
			right: 1rem;
			top: 1rem;
			opacity: 0;
		}
		.nav-container {
			transform: translate(100%);
			display: flex;
			flex-direction: column;
			position: fixed;
			min-width: 160px;
			z-index: 1;
			top: 0;
			right: 0;
			margin: 0;
			background-color: var(--main-color);
			border-left: 1px solid var(--content-color);
			opacity: 0.9;
			transition: transform 0.5s ease-in-out;
			justify-content: center;
		}
		.nav-links {
			height: 100%;
			display: flex;
			flex-direction: column;
		}
		.toggle-theme-container {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			height: 100%;
		}
		.prof-link-container {
			height: 100%;
		}
		input[type='checkbox']:checked ~ .nav-container {
			transform: translateX(0);
		}
		.nav-links li a:hover {
			border-bottom: 2px solid var(--content-color);
		}
		.nav-links li {
			margin: 0.15em;
		}
		.user-name {
			text-align: center;
			font-size: var(--fontsize-large);
			max-width: 13rem;
			box-sizing: border-box;
			padding: 0.5rem;
		}
		.user-alias {
			text-align: center;
			box-sizing: border-box;
			padding: 0.5rem;
		}
		li.for-mobile {
			border-radius: 10px;
			background-color: var(--secondary-color);
			height: 2.5rem;
			display: flex;
			flex-direction: column;
			align-items: center;
		}
		li.for-mobile a {
			/* height: 1.5rem; */
			display: flex;
			flex-direction: column;
			align-items: center;
			color: white;
			border-bottom: none;
		}
		li.for-mobile a:hover {
			border-bottom: none;
		}
	}
</style>
