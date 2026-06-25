<script lang="ts">
	import { createEventDispatcher } from 'svelte';
	import Button from '$lib/components/base/Button.svelte';
	import InputTemplate from '$lib/components/base/inputs/InputTemplate.svelte';
	import BackArrowButton from '$lib/components/base/BackArrowButton.svelte';
	import type { Tournament } from '$lib/types/types';
	import { alertPopup } from '$lib/client/popup/popup.handler';
	import type { Group, TournamentStage } from '$lib/types/types.tournaments';

	export let stage: TournamentStage;
	export let tournament: Tournament;

	const dispatch = createEventDispatcher();

	let numberGroups = tournament.state.firstStage?.length ?? 0;
	let groups: Group[] = [];

	const makeGroups = function () {
		groups = [];
		for (let i = 0; i < numberGroups; i++) {
			groups[i] = {
				type: 'Group',
				tournamentTitle: tournament.title,
				players: [],
				matches: [],
				id: i,
			};
		}
		for (let i = 0; i < tournament.state.participants.length; i++) {
			groups[i % numberGroups].players = [
				...groups[i % numberGroups].players,
				tournament.state.participants[i],
			];
		}
	};

	const nextStage = async function () {
		if (
			!tournament.state ||
			!tournament.state.firstStage?.length ||
			tournament.state.firstStage.length == numberGroups ||
			(tournament.state &&
				tournament.state.firstStage?.length != numberGroups &&
				(await alertPopup(
					'Are you sure? Changing the number of groups can cause the loss of previous results.',
				)))
		) {
			makeGroups();
			if (numberGroups != tournament.state.firstStage?.length) {
				tournament.state = {
					participants: tournament.state.participants,
					firstStage: groups,
					secondStage: null,
				};
			}

			dispatch('update', { state: tournament.state });
			stage = 'groups';
		}
	};
	function back() {
		stage = 'addParticipants';
	}
</script>

<BackArrowButton action={back} />

<div class="center">
	<div class="content">
		<h1>Choose the number of groups in the tournament</h1>
		<form on:submit|preventDefault={nextStage}>
			<div class="input">
				<InputTemplate
					type="number"
					min="1"
					max="100"
					name="groupNumber"
					placeholder=""
					defaultNumValue={tournament.state.firstStage?.length
						? tournament.state.firstStage?.length
						: numberGroups}
					bind:numberVal={numberGroups}
					textAlignCenter={true}
				/>
			</div>
			<div class="button">
				<Button type="submit"
					>{numberGroups === tournament.state.firstStage?.length ? 'Continue' : 'Confirm'}</Button
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
		width: 30rem;
		height: 16rem;
		text-align: center;
	}
	.button {
		margin: 2rem auto auto;
		width: 9rem;
		height: 2.75rem;
	}
	.input {
		margin: 3rem auto auto;
		max-width: 5rem;
	}

	h1 {
		font-size: var(--fontsize-x-large);
		font-weight: var(--fontweight-1);
		letter-spacing: 0.125rem;
	}
</style>
