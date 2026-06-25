<script lang="ts">
	import Button from '$lib/components/base/Button.svelte';
	import type { Tournament } from '$lib/types/types';
	import type { TournamentStage } from '$lib/types/types.tournaments';

	export let stage: TournamentStage;
	export let tournament: Tournament;

	const nextStage = function () {
		stage = 'addParticipants';
	};
</script>

<div class="center">
	<div class="content">
		<h1>{tournament.title}</h1>
		<p>{tournament.startDateString} - {tournament.endDateString}</p>
		<div class="button">
			{#if tournament.state != null}
				<Button on:click={() => nextStage()}>Continue</Button>
			{:else}
				<Button on:click={() => nextStage()}>Create tournament</Button>
			{/if}
		</div>
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
		height: 11rem;
		text-align: center;
	}
	.button {
		margin: auto;
		margin-top: 3rem;
		width: 18rem;
		height: 2.75rem;
	}

	h1 {
		font-size: var(--fontsize-x-large);
		font-weight: var(--fontweight-1);
		letter-spacing: 0.3125rem;
	}
	p {
		margin-top: 1rem;
		font-size: var(--fontsize-large);
		font-weight: 500;
		letter-spacing: 0.09375rem;
	}
</style>
