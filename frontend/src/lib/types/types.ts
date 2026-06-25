import type { TournamentState } from '$lib/types/types.tournaments';

export interface Error {
	message: string;
	// status: number;
}

export interface DecodedJWT {
	exp: number;
	iat: number;
	roles: ('LEADER' | 'ADMIN' | 'USER')[];
	sub: string;
}

export interface Player {
	id: number;
	name: string;
	numberOfGames: number;
	numberOfLosses: number;
	numberOfWins: number;
	rating: number;
	telegramAlias: string;
	winRate: number;
}

export interface Match {
	firstPlayerScore: number;
	secondPlayerScore: number;
	firstPlayerName: string;
	secondPlayerName: string;
	localDateString: string;
	id: number;
	firstPlayerRatingDelta: number;
	secondPlayerRatingDelta: number;
	firstPlayerRatingBefore: number;
	secondPlayerRatingBefore: number;
	tournamentTitle: string;
}

export interface Tournament {
	coefficient: number;
	endDateString: string;
	finished: boolean;
	id: number;
	numberOfPlayers: number;
	startDateString: string;
	title: string;
	matches: Match[];
	state: TournamentState;
}
