import { writable } from 'svelte/store';
import type {
	tournamentSortFilterMatchFormStore,
	tournamentSortFilterPlayerFormStore,
} from '$lib/types/types.tournaments';

const TOURNAMENT_SORT_FILTER_MATCH_FORM: tournamentSortFilterMatchFormStore = {
	name: '',
	score: '',
};

const TOURNAMENT_SORT_FILTER_PLAYER_FORM: tournamentSortFilterPlayerFormStore = {
	name: '',
	telegramAlias: '',
	minRating: null,
	maxRating: null,
	descending: false,
	sortBy: 'place',
};

export const TournamentFilterMatchFormStore = writable(TOURNAMENT_SORT_FILTER_MATCH_FORM);
export const TournamentSortFilterParticpantsFormStore = writable(
	TOURNAMENT_SORT_FILTER_PLAYER_FORM,
);
