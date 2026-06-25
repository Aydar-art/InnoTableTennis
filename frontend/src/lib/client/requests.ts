// import { handleGetErrors, handleModifyErrors } from '$lib/errorHandler';

import type { Player, Match, Tournament } from '$lib/types/types';
import type { TournamentState } from '$lib/types/types.tournaments';

import { dev } from '$app/environment';
import { PUBLIC_DEV_SERVER_URL, PUBLIC_PROD_SERVER_URL } from '$env/static/public';

/**
 * This file contains functions for making API requests.
 */

const token = '';

const SERVER_URL = dev ? PUBLIC_DEV_SERVER_URL : PUBLIC_PROD_SERVER_URL;
const SERVER_API: string = SERVER_URL + '/api';

export async function getAllPlayers(): Promise<Player[]> {
	const response = await fetch(SERVER_API + '/players');

	//TODO: Handle errors

	const players = await response.json();

	return players;
}

export async function getAllTournaments(): Promise<Tournament[]> {
	const response = await fetch(SERVER_API + '/tournaments');

	//TODO: Handle errors

	const tournaments = await response.json();

	return tournaments;
}

/**
 * Creates a new match.
 * @param firstPlayerName - The name of the first player.
 * @param secondPlayerName - The name of the second player.
 * @param firstPlayerScore - The score of the first player.
 * @param secondPlayerScore - The score of the second player.
 * @param tournamentTitle - The title of the tournament.
 * @param localDateString - The local date string.
 * @returns The created match data.
 */
export async function createMatch(
	firstPlayerName: string,
	secondPlayerName: string,
	firstPlayerScore: number,
	secondPlayerScore: number,
	tournamentTitle: string,
	localDateString: string | null = null,
): Promise<Match> {
	const response: Response = await fetch(SERVER_API + '/matches', {
		method: 'POST',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: 'Bearer ' + token,
		},
		body: JSON.stringify({
			firstPlayerName,
			secondPlayerName,
			firstPlayerScore,
			secondPlayerScore,
			localDateString,
			tournamentTitle,
		}),
	});

	// await handleModifyErrors(response, token);
	//TODO: handle errors

	const data = await response.json();

	return data;
}

export async function editMatch(
	id: string,
	firstPlayerName: string,
	secondPlayerName: string,
	firstPlayerScore: number,
	secondPlayerScore: number,
	tournamentTitle: string,
	localDateString: string | null = null,
): Promise<void> {
	const response: Response = await fetch(SERVER_API + '/matches/' + id, {
		method: 'PUT',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: 'Bearer ' + token,
		},
		body: JSON.stringify({
			firstPlayerScore,
			secondPlayerScore,
			tournamentTitle,
			firstPlayerName,
			secondPlayerName,
			localDateString,
		}),
	});
	// await handleModifyErrors(response, token);
	//TODO: handle errors
}

/**
 * Updates state of the tournament.
 * @param tournamentID - The ID of the tournament.
 */
export async function updateTournament(
	tournamentID: number,
	state: TournamentState | null,
): Promise<void> {
	const response: Response = await fetch(SERVER_API + '/tournaments/' + tournamentID + '/state', {
		method: 'PATCH',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify({
			state: JSON.stringify(state),
		}),
	});

	// await handleModifyErrors(response, token);
	//TODO: handle errors
}
