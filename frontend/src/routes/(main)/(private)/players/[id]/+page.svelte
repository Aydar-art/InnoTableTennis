<script lang="ts">
	import profileImage from '$lib/assets/profileImage.png';
	import InfoBlocks from '$lib/components/profile/InfoBlocks.svelte';
	import ProfileMatchesComponent from '$lib/components/profile/ProfileMatchesComponent.svelte';
	import RatingGraph from '$lib/components/graph/RatingGraph.svelte';
	import type { ProfileData, ProfileMatch, RatingHistoryItem } from '$lib/types/types.profile.js';
	import { handleError } from '$lib/client/handleError.js';

	export let data;

	$: if (data.error) {
		handleError(data.error);
	}

	let profileStats = data.profileStats;

	let profileData: ProfileData = data.profileData;
	let graphInfo: RatingHistoryItem[] = profileData.graph;
	let dataMatches: ProfileMatch[] = profileData.matches;
</script>

<svelte:head>
	<meta
		name="description"
		content="Explore your table tennis statistics at InnoTableTennis Club. View the number of tournaments played, win/loss records, rating history, graphs, and a complete list of all your played games. Track your progress and achievements in the club."
	/>
	<meta
		name="keywords"
		content="profile statistics, table tennis, InnoTableTennis Club, rating history, win/loss records, played games, progress tracking"
	/>
</svelte:head>

<div class="container">
	<div class="banner">
		<div class="row">
			<div class="profile-info">
				<div class="name">
					{profileData?.name || ''}
				</div>
			</div>
			<img class="image-container" src={profileImage} alt="Problem with downloading" />
		</div>
	</div>
	<div class="column">
		<InfoBlocks {profileStats} />
		<RatingGraph Data={graphInfo} />
		<ProfileMatchesComponent {dataMatches} />
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
		margin-top: 14rem;
	}

	.banner {
		position: absolute;
		box-sizing: border-box;
		top: 0;
		left: 0;
		width: 100vw;
		z-index: -1;
		height: 15rem;
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
		padding: 0 10%;
		justify-content: center;
	}
	.image-container {
		object-fit: cover;
		height: 100%;
		width: 30%;
	}
	.name {
		font-size: var(--fontsize-x-large);
		color: var(--main-color);
	}

	@media (max-width: 840px) {
	}

	@media (max-width: 480px) {
		.container {
			top: 0;
		}
		.banner {
			height: 10rem;
		}
		.column {
			margin-top: 10rem;
		}
	}
</style>
