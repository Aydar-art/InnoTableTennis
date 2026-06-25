import type { Player, Tournament } from '$lib/types/types';

export function countDeltas(tournament: Tournament) {
	const deltas: number[] = [];
	if (tournament.state) {
		for (let i = 0; i < tournament.state.participants.length; i++) {
			deltas[i] = 0;
			const currentPlayer: Player | undefined = tournament.state.participants.at(i);
			for (let j = 0; j < tournament.matches.length; j++) {
				const currentPlayerName = currentPlayer?.name;
				if (tournament.matches[j].firstPlayerName === currentPlayerName) {
					deltas[i] += tournament.matches[j].firstPlayerRatingDelta;
				} else if (tournament.matches[j].secondPlayerName === currentPlayerName) {
					deltas[i] += tournament.matches[j].secondPlayerRatingDelta;
				}
			}
			deltas[i] = Math.floor(deltas[i] * 100) / 100;
		}
	}
	return deltas;
}
