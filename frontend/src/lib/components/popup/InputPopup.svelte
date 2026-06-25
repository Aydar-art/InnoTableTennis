<script lang="ts">
	import Button from '$lib/components/base/Button.svelte';
	import { fade } from 'svelte/transition';
	import { inputOverlayText, outputOverlayText } from '$lib/client/stores/stores';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import { convertDateToString } from '$lib/utils';
	export let popupText: string;
	export let firstPlayer: string;
	export let secondPlayer: string;
	export let date: string = convertDateToString(new Date());

	const buttonPressed = (e: Event) => {
		e.preventDefault();
		let data = new FormData(e.target as HTMLFormElement);
		$outputOverlayText = {
			firstScore: Number(data.get('firstScore')),
			secondScore: Number(data.get('secondScore')),
			date: String(data.get('matchDate')),
		};
		$inputOverlayText = null;
	};
</script>

<form class="fullscreen-overlay" transition:fade on:submit|preventDefault={buttonPressed}>
	<button class="overlay-background" type="button" />
	<div class="overlay-content">
		<div class="overlay-head">
			<div class="circle">!</div>
		</div>
		<div class="popup-container">
			<p class="overlay-text">{popupText}</p>
			<div class="input-score">
				<div class="player-block">
					<p>{firstPlayer}</p>
					<div class="input">
						<InputTemplate
							type={'number'}
							name={'firstScore'}
							defaultNumValue={0}
							placeholder={'First Score'}
						/>
					</div>
				</div>
				<div class="player-block">
					<p>{secondPlayer}</p>
					<div class="input">
						<InputTemplate
							type={'number'}
							name={'secondScore'}
							defaultNumValue={0}
							placeholder={'Second Score'}
						/>
					</div>
				</div>
			</div>
			<div class="input-date">
				<!-- <div> -->
				<InputTemplate type="date" name="matchDate" defaultValue={date} placeholder="Match Date" />
				<!-- </div> -->
			</div>
			<div class="overlay-button-block">
				<div class="overlay-button">
					<Button type={'submit'}>Submit</Button>
				</div>
				<div class="overlay-button">
					<Button dark={true} type={'submit'}>Cancel</Button>
				</div>
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
		width: 25rem;
		text-align: center;
		border-radius: 30px;
		opacity: 1;
		top: 50%;
		transform: translateY(-50%);
	}
	.overlay-head {
		display: flex;
		height: 2.5rem;
		padding: 0.5rem 0;
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
	.popup-container {
		padding: 0 10%;
	}
	.overlay-text {
		font-size: var(--fontsize-large);
		margin: 1rem auto;
	}
	.overlay-button-block {
		display: flex;
		width: 100%;
		justify-content: space-evenly;
		margin: 1rem 0;
		padding: 1rem 0 1.5rem;
	}
	.overlay-button {
		/* margin: 0.5rem 0 1rem; */
		width: 40%;
	}
	.player-block p {
		width: 100%;
		height: 2rem;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.input-date {
		display: flex;
		justify-content: space-between;
		margin: 0.5rem 0;
	}

	.input-score {
		margin-top: 1.5rem;
		width: 100%;
		display: flex;
		gap: 1.5rem;
		justify-content: space-between;
	}
	.input {
		margin: 0.5rem 0;
	}
	@media (max-width: 800px) {
		.overlay-content {
			width: 60%;
		}
	}
	@media (max-width: 480px) {
		.overlay-content {
			padding: 0;
			width: 70%;
		}
		.overlay-text {
			font-size: var(--fontsize-large);
			margin-top: 2rem;
		}
		/* .overlay-button {
			width: 60%;
		} */
		.input-score {
			margin-top: 0;
			/* display: block; */
			/* margin: 2rem auto; */
			flex-direction: column;
		}
		.player-block {
			text-align: center;
		}
		.input {
			margin: 0 auto;
		}

		.input-date {
			margin-top: 1.5rem;
		}
	}
</style>
