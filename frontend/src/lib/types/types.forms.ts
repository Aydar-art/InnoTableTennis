export interface sortFilterPlayerFormStore {
	name: string;
	telegramAlias: string;
	minRating: number | null;
	maxRating: number | null;
	descending: boolean;
	sortBy: 'rating' | 'name';
}

export interface sortFilterTournamentFormStore {
	title: string;
	minParticipants: number | null;
	maxParticipants: number | null;
	startDateString: string;
	endDateString: string;
	descending: boolean;
	sortBy: 'date' | 'players' | 'coefficient';
}

export interface sortFilterMatchFormStore {
	name: string;
	score: string;
	minDateString: string;
	maxDateString: string;
	descending: boolean;
	sortBy: 'date';
}

export interface addPlayerFormStore {
	name: string;
	telegramAlias: string;
	initialRating: number;
}

export interface addTournamentFormStore {
	title: string;
	startDateString: string;
	endDateString: string;
}

export interface addMatchFormStore {
	firstPlayerName: string;
	secondPlayerName: string;
	tournamentTitle: string;
	firstPlayerScore: number;
	secondPlayerScore: number;
	localDateString: string;
}
