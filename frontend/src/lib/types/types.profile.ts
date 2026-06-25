export interface Stats {
	matchesPlayed: number;
	tournamentsParticipated: number;
	matchesWon: number;
	matchesLost: number;
	winPercentage: number;
	pointsWon: number;
	pointsLost: number;
	averageMatchDuration: number;
	averageMatchPoints: number;
	winningStreak: number;
	losingStreak: number;
	highestMatchDelta: number;
	lowestMatchDelta: number;
	averageMatchDelta: number;
	highestTournamentsDelta: number;
	lowestTournamentsDelta: number;
	averageTournamentsDelta: number;
	ranking: number;
	rating: number;
}

export interface ProfileMatch {
	opponentName: string;
	opponentRatingBefore: number;
	delta: number;
	score: string;
	date: string;
	tournamentTitle: string;
}

export interface ProfileInfo {
	data: ProfileData;
}
export interface ProfileData {
	name: string;
	telegramAlias: string;
	statistics: Stats;
	matches: ProfileMatch[];
	graph: RatingHistoryItem[];
	id: number;
}

export interface RatingHistoryItem {
	date: string;
	rating: number;
	tournamentTitle: string;
	delta: number;
}
