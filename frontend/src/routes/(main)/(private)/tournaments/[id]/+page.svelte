<script lang="ts">
	import profileImage from '$lib/assets/tournament.png';
	import TournamentInfoPanel from '$lib/components/tournament/TournamentInfoPanel.svelte';
	import ParticipantsTable from '$lib/components/tables/ParticipantsTable.svelte';
	import TournamentSortFilterParticipantsForm from '$lib/components/forms/TournamentSortFilterParticipantsForm.svelte';
	import TournamentFilterMatchForm from '$lib/components/forms/TournamentFilterMatchForm.svelte';
	import TournamentGroup from '$lib/components/tournament/tournamentConstructor/TournamentGroup.svelte';
	import TournamentMatchesTable from '$lib/components/tables/TournamentMatchesTable.svelte';
	import { handleError } from '$lib/client/handleError.js';
	let handleInsert: () => void;

	export let data;

	$: if (data.error) {
		handleError(data.error);
	}

	$: tournament = data.tournament;
	$: deltas = data.deltas;
</script>

<svelte:head>
	<meta
		name="description"
		content="Explore the statistics, list of participants, groups, and tournament net and matches of the Tournament Name at InnoTableTennis Club. Stay updated with exciting table tennis games and see who emerges as the winner."
	/>
	<meta
		name="keywords"
		content="tournament name, InnoTableTennis Club, tournament statistics, list of participants, groups, tournament net, table tennis matches"
	/>
</svelte:head>

<div class="container">
	<div class="banner">
		<div class="row">
			<img class="image-container" src={profileImage} alt="Problem with downloading" />
			<div class="profile-info">
				<div class="name">
					{tournament.title}
				</div>
				<div class="date">
					{tournament.startDateString} - {tournament.endDateString}
				</div>
			</div>
		</div>
	</div>
	<div class="column">
		{#if tournament.finished}
			{#if tournament.state}
				<TournamentInfoPanel
					numberOfGames={tournament.matches.length}
					places={tournament.state.participants}
					numberOfParticipants={tournament.state === null
						? 0
						: tournament.state.participants.length}
				/>
			{:else}
				<TournamentInfoPanel numberOfGames={tournament.matches.length} numberOfParticipants={0} />
			{/if}
		{:else}
			<TournamentInfoPanel
				numberOfGames={tournament.matches.length}
				numberOfParticipants={tournament.state === null ? 0 : tournament.state.participants.length}
			/>
		{/if}
	</div>
	<div class="main">
		{#if tournament.state}
			<!-- {#if tournament.finished} -->
			<h1>List of all the participants</h1>
			<div class="form-list-layout">
				<div class="form">
					<TournamentSortFilterParticipantsForm on:update={() => handleInsert()} />
				</div>
				<div class="participants-list">
					<ParticipantsTable
						participants={tournament.state.participants}
						{deltas}
						bind:handleInsert
					/>
				</div>
			</div>
			<!-- {/if} -->
			{#if tournament.state.firstStage}
				<h1>First stage - Groups</h1>
				{#each tournament.state.firstStage as group}
					<TournamentGroup finalInfo={group}>Group {group.id + 1}</TournamentGroup>
				{/each}
			{/if}
			{#if tournament.state.secondStage}
				<h1>Second stage - 2 finals</h1>
				{#each tournament.state.secondStage as final, i}
					{#if final.type === 'Group'}
						<TournamentGroup finalInfo={final}>Final {i + 1}</TournamentGroup>
					{:else}
						{'single elim tourn'}
					{/if}
				{/each}
			{/if}
		{/if}
		<h1>Matches</h1>
		<div class="form-list-layout">
			<div class="form">
				<TournamentFilterMatchForm on:update={() => handleInsert()} />
			</div>
			<div class="participants-list">
				<TournamentMatchesTable matches={tournament.matches} bind:handleInsert />
			</div>
		</div>
	</div>
</div>

<style>
	.container {
		position: absolute;
		right: 0;
		color: var(--content-color);
		display: flex;
		flex-direction: column;
		width: 100vw;
		min-height: 100%;
		align-content: center;
	}
	.column {
		display: flex;
		width: 100vw;
		flex-direction: column;
		justify-content: center;
		margin-top: 17rem;
	}

	.banner {
		position: absolute;
		box-sizing: border-box;
		top: 0;
		left: 0;
		width: 100vw;
		z-index: -1;
		height: 20rem;
		background: var(--secondary-color);
	}
	.row {
		height: 100%;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: center;
	}
	.profile-info {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
		width: 70%;
		height: 100%;
		padding: 0 5%;
		justify-content: center;
	}
	.image-container {
		object-fit: cover;
		height: 100%;
		width: 30%;
	}
	.name {
		font-size: var(--fontsize-x-large);
		font-weight: var(--fontweight-1);
		color: var(--main-color);
		letter-spacing: 0.3125rem;
	}
	.date {
		font-size: var(--fontsize-large);
		color: var(--main-color);
		letter-spacing: 0.09375rem;
	}

	.main {
		padding: 0 4rem;
	}
	.form-list-layout {
		margin-bottom: 2rem;
		display: grid;
		grid-auto-flow: column;
		align-items: center;
		grid-template-columns: 1fr 2fr;
	}
	.form {
		max-width: 350px;
		margin-right: 2rem;
	}
	.participants-list {
		max-width: 900px;
	}

	h1 {
		margin-top: 3rem;
		font-size: var(--fontsize-x-large);
		font-weight: var(--fontweight-1);
		letter-spacing: 0.125rem;
	}

	@media (max-width: 1100px) {
		.form {
			max-width: 500px;
			margin: 0 auto;
		}
		.form-list-layout {
			display: block;
			margin: 0;
			margin-bottom: 2rem;
		}
		.participants-list {
			margin: 0 auto;
		}
	}

	@media (max-width: 480px) {
		.main {
			padding: 0 2rem;
		}
		.container {
			top: 0;
		}
		.banner {
			height: 10rem;
		}
		.column {
			margin-top: 10rem;
		}
		.name {
			font-size: var(--fontsize-large);
			letter-spacing: 0.09375rem;
		}
		.date {
			font-size: var(--fontsize-medium1);
			letter-spacing: 0.09375rem;
		}
	}
</style>
