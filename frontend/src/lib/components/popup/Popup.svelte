<script lang="ts">
	import Button from '$lib/components/base/Button.svelte';
	import { fade } from 'svelte/transition';
	import { overlayText } from '$lib/client/stores/stores';
	export let popupText: string;
	const buttonPressed = (e: Event) => {
		e.preventDefault();
		$overlayText = null;
	};
</script>

<form class="fullscreen-overlay" transition:fade on:submit={buttonPressed}>
	<button class="overlay-background" type="submit" />
	<div class="overlay-content">
		<div class="overlay-head">
			<div class="circle">!</div>
		</div>
		<p class="overlay-text">{popupText}</p>
		<div class="overlay-button-block">
			<div class="overlay-button-yes">
				<Button type={'submit'}>Yes</Button>
			</div>
			<div class="overlay-button-no">
				<Button dark={true} type={'submit'}>No</Button>
			</div>
		</div>
	</div>
</form>

<style>
	.fullscreen-overlay {
		position: fixed;
		visibility: visible;
		top: 0;
		left: 0;
		height: 100%;
		width: 100%;
		z-index: 10;
		align-items: center;
	}
	.overlay-background {
		position: fixed;
		height: 100%;
		width: 100%;
		background-color: var(--content-color);
		z-index: 0;
		border: none;
		padding: 0;
		outline: none;
		opacity: 0.8;
	}
	.overlay-content {
		margin: 0 auto;
		position: relative;
		background: var(--main-color);
		z-index: 1;
		height: 15rem;
		width: 25rem;
		text-align: center;
		border-radius: 30px;
		opacity: 1;
		top: 50%;
		transform: translateY(-50%);
	}
	.overlay-head {
		display: flex;
		height: 30%;
		background-color: var(--secondary-color);
		border-top-left-radius: 30px;
		border-top-right-radius: 30px;
		align-items: center;
		justify-content: center;
	}
	.circle {
		display: flex;
		height: 2rem;
		width: 2rem;
		border-radius: 100%;
		border: 3px solid var(--main-color);
		align-items: center;
		justify-content: center;
		color: var(--main-color);
		font-size: var(--fontsize-large);
		font-weight: var(--fontweight-1);
	}
	.overlay-text {
		margin: 2.5rem 1rem;
		font-size: var(--fontsize-medium1);
		font-weight: var(--fontweight-2);
	}
	.overlay-button-block {
		position: absolute;
		display: flex;
		bottom: 10%;
		height: 15%;
		width: 100%;
		justify-content: center;
	}
	.overlay-button-yes {
		width: 20%;
		margin-right: 1rem;
	}
	.overlay-button-no {
		margin-left: 1rem;
		width: 20%;
	}
	@media (max-width: 800px) {
		.overlay-content {
			height: 45%;
			width: 60%;
		}
	}
	@media (max-width: 480px) {
		.overlay-button-block {
			display: block;
			margin-bottom: 15%;
		}
		.overlay-button-yes {
			width: 60%;
			margin: 0 auto;
		}
		.overlay-button-no {
			width: 60%;
			margin: 7% auto;
		}
	}
</style>
