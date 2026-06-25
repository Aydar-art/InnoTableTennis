<script lang="ts">
	import ProfileMatchesTable from '$lib/components/tables/ProfileMatchesTable.svelte';
	import type { ProfileMatch } from '$lib/types/types.profile';

	export let dataMatches: ProfileMatch[];
</script>

<div class="matches-comp-wrapper">
	<div class="form-list-layout">
		<h1>All games played</h1>
		<div class="matches-list">
			<ProfileMatchesTable {dataMatches} />
		</div>
	</div>
</div>

<style>
	h1 {
		font-size: var(--fontsize-x-large);
		font-weight: 700;
	}
	.matches-comp-wrapper {
		box-sizing: border-box;
		padding: 0% 3% 3% 3%;
		height: 100%;
	}
	.matches-list {
		margin: 0 auto;
		max-width: 900px;
	}

	@media (max-width: 1100px) {
		.form-list-layout {
			display: block;
		}
		.matches-list {
			margin: 0 auto;
		}
	}
</style>
