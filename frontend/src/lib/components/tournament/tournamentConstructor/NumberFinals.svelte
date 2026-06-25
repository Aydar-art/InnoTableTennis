<script lang="ts">
	import Button from '$lib/components/base/Button.svelte';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import BackArrowButton from '$lib/components/base/BackArrowButton.svelte';
	import type { Tournament } from '$lib/types/types';
	import { alertPopup } from '$lib/client/popup/popup.handler';
	import type { TournamentStage } from '$lib/types/types.tournaments';

	export let stage: TournamentStage;
	export let tournament: Tournament;

	export let numberFinals = tournament.state.secondStage?.length ?? 1;

	const nextStage = async function () {
		if (
			!tournament.state ||
			!tournament.state.secondStage?.length ||
			tournament.state.secondStage.length == numberFinals ||
			(tournament.state &&
				tournament.state.secondStage?.length != numberFinals &&
				(await alertPopup(
					'Are you sure? Changing the number of finals can cause the loss of previous results.',
				)))
		) {
			stage = 'finalsDistribution';
		}
	};
	function back() {
		stage = 'continue';
	}
</script>

<BackArrowButton action={back} />
<div class="center">
	<div class="content">
		<h1>How many finals do you want?</h1>
		<form
			on:submit|preventDefault={async () => {
				await nextStage();
			}}
		>
			<div class="input">
				<InputTemplate
					type="number"
					min="1"
					max="100"
					name="groupNumber"
					placeholder=""
					bind:numberVal={numberFinals}
					textAlignCenter={true}
				/>
			</div>
			<div class="button">
				<Button type="submit"
					>{numberFinals === tournament.state.secondStage?.length ? 'Continue' : 'Confirm'}</Button
				>
			</div>
		</form>
	</div>
</div>

<style>
	.center {
		display: flex;
		height: 90vh;
	}
	.content {
		margin: auto;
		width: 35rem;
		height: 11rem;
		text-align: center;
	}
	.button {
		margin: 2rem auto auto;
		width: 9rem;
	}
	.input {
		margin: 1.5rem auto auto;
		max-width: 4rem;
	}

	h1 {
		font-size: var(--fontsize-large);
		font-weight: var(--fontweight-1);
		letter-spacing: 0.09375rem;
	}
</style>
