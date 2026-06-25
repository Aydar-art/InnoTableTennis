import { objectToURLSearchParams } from '$lib/utils';
import { handleGetErrors, handleModifyErrors } from '$lib/errorHandler';

import { userToken } from '$lib/server/stores';

import type { Player, Match, Tournament } from '$lib/types/types';
import type { TournamentState } from '$lib/types/types.tournaments';

import type { Stats, ProfileData } from '$lib/types/types.profile';

import { dev } from '$app/environment';

import { DEV_SERVER_URL, PROD_SERVER_URL } from '$env/static/private';

/**
 * This file contains functions for making API requests.
 */

let token = '';

userToken.subscribe((value: string) => {
	token = value;
});

const serverPath = dev ? DEV_SERVER_URL : PROD_SERVER_URL;
const serverAPI: string = serverPath + '/api';
const serverAUTH: string = serverPath + '/auth';

/**
 * Retrieves matches from the API.
 * @param sortBy - The sorting type either by name or by rating.
 * @param descending - The sorting order.
 * @param name - The string that player has in their name in match.
 * @param score - The score of the match.
 * @param startDateString - The start date of showed matches.
 * @param endDateString - The end date of showed matches.
 * @param pageNumber - The page number.
 * @param pageSize - The page size.
 * @returns The matches data and total number of pages.
 */
export async function getMatches(
	descending: boolean | null,
	name: string | null,
	score: string | null,
	minDateString: string | null,
	maxDateString: string | null,
	pageNumber: number | null,
	pageSize: number | null,
): Promise<{
	matches: Match[];
	totalPages: number;
}> {
	const searchParams = objectToURLSearchParams({
		score,
		descending: descending !== null ? String(descending) : null,
		hasPlayerWith: name,
		startDate: minDateString,
		endDate: maxDateString,
		page: pageNumber ? String(pageNumber) : null,
		size: pageSize ? String(pageSize) : null,
	});

	const url: string = serverAPI + '/matches' + (searchParams && '?' + searchParams);

	const response: Response = await fetch(url, {
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: `Bearer ${token}`,
		},
	});

	await handleGetErrors(response, token);

	const totalPages: number = parseInt(response.headers.get('X-Total-Pages') ?? '999', 10);
	const data = await response.json();
	return { matches: data, totalPages };
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
	if (localDateString !== null) {
		localDateString = new Date(localDateString).toLocaleDateString('ru');
	} else {
		localDateString = new Date().toLocaleDateString('ru');
	}

	const response: Response = await fetch(serverAPI + '/matches', {
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

	await handleModifyErrors(response, token);

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
	if (localDateString !== null) {
		localDateString = new Date(localDateString).toLocaleDateString('ru');
	} else {
		localDateString = new Date().toLocaleDateString('ru');
	}

	const response: Response = await fetch(serverAPI + '/matches/' + id, {
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
	await handleModifyErrors(response, token);
}

/**
 * Deletes a match.
 * @param matchID - The ID of the match.
 */
export async function deleteMatch(matchID: string): Promise<void> {
	const response: Response = await fetch(serverAPI + '/matches/' + matchID, {
		method: 'DELETE',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: 'Bearer ' + token,
		},
	});

	await handleModifyErrors(response, token);
}

/**
 * Retrieves players from the API.
 * @param sortBy - The sorting type either by name or by rating.
 * @param descending - The sorting order.
 * @param name - The string that players has in their name.
 * @param alias - The string that players has in their alias.
 * @param minRating - The minimum rating of showed players.
 * @param maxRating - The maximum rating of showed players.
 * @param pageNumber - The page number.
 * @param pageSize - The page size.
 * @returns The players data and total number of pages.
 */
export async function getPlayers(
	sortBy: 'name' | 'rating' = 'rating',
	descending: boolean | null,
	name: string | null,
	alias: string | null,
	minRating: number | null,
	maxRating: number | null,
	pageNumber: number | null,
	pageSize: number | null,
): Promise<{
	players: Player[];
	totalPages: number;
}> {
	const searchParams = objectToURLSearchParams({
		sortBy,
		descending: descending !== null ? String(descending) : null,
		nameHas: name,
		aliasHas: alias,
		minRating: minRating ? String(minRating) : null,
		maxRating: maxRating ? String(maxRating) : null,
		page: pageNumber ? String(pageNumber) : null,
		size: pageSize ? String(pageSize) : null,
	});

	const url: string = serverAPI + '/players' + (searchParams && '?' + searchParams);

	const response: Response = await fetch(url, {
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: `Bearer ${token}`,
		},
	});

	await handleGetErrors(response, token);

	const totalPages: number = parseInt(response.headers.get('X-Total-Pages') ?? '100', 10);

	const data = await response.json();

	return { players: data, totalPages };
}

/**
 * Creates a new player.
 * @param name - The name of the player.
 * @param telegramAlias - The Telegram alias of the player.
 * @param rating - The rating of the player.
 * @returns The created player data.
 */
export async function createPlayer(
	name: string,
	telegramAlias: string | null = null,
	rating: number | null = null,
): Promise<Player> {
	name = name.trim();

	if (telegramAlias !== null) {
		telegramAlias = telegramAlias.trim();
		if (telegramAlias === '') telegramAlias = null;
	}

	if (telegramAlias !== null) {
		if (telegramAlias[0] == '@') telegramAlias = telegramAlias.slice(1);
		if (!/^[A-Za-z0-9_]*$/.test(telegramAlias)) {
			throw new Error('telegram alias must consist only of latin alphabet letters and digits! ');
		}
	}

	if (!/^[A-Za-z ().]*$/.test(name)) {
		throw new Error('name must consist only of latin alphabet letters!');
	}

	if (name.split(' ').length < 2) {
		throw new Error('Name must consist of at least two parts!');
	}

	const response: Response = await fetch(serverAPI + '/players', {
		method: 'POST',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: 'Bearer ' + token,
		},
		body: JSON.stringify({
			name,
			telegramAlias,
			rating,
		}),
	});

	await handleModifyErrors(response, token);

	const data = await response.json();
	return data;
}

/**
 * Creates a new player.
 * @param name - The name of the player.
 * @param telegramAlias - The Telegram alias of the player.
 * @param rating - The rating of the player.
 */

export async function editPlayer(
	id: string,
	name: string,
	telegramAlias: string | null = null,
	rating: number | null = null,
): Promise<void> {
	name = name.trim();

	if (telegramAlias !== null) {
		telegramAlias = telegramAlias.trim();
		if (telegramAlias === '') telegramAlias = null;
	}

	if (telegramAlias !== null) {
		if (telegramAlias[0] == '@') telegramAlias = telegramAlias.slice(1);
		if (!/^[A-Za-z0-9_]*$/.test(telegramAlias)) {
			throw new Error('telegram alias must consist only of latin alphabet letters and digits! ');
		}
	}

	if (!/^[A-Za-z ().]*$/.test(name)) {
		throw new Error('name must consist only of latin alphabet letters!');
	}

	if (name.split(' ').length < 2) {
		throw new Error('Name must consist of at least two parts!');
	}

	const response: Response = await fetch(serverAPI + '/players/' + id, {
		method: 'PUT',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: 'Bearer ' + token,
		},
		body: JSON.stringify({
			name,
			telegramAlias,
			rating,
		}),
	});

	await handleModifyErrors(response, token);
}

/**
 * Deletes a player.
 * @param playerID - The ID of the player.
 */
export async function deletePlayer(playerID: string): Promise<void> {
	const response: Response = await fetch(serverAPI + '/players/' + playerID, {
		method: 'DELETE',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: `Bearer ${token}`,
		},
	});

	await handleModifyErrors(response, token);
}

/**
 * Authenticates a user.
 * @param username - The username.
 * @param password - The password.
 * @returns The user token.
 */
export async function authenticate(username: string, password: string): Promise<string> {
	let telegramAlias: string = username.trim();

	if (telegramAlias !== null) {
		if (telegramAlias[0] == '@') telegramAlias = telegramAlias.slice(1);
		if (!/^[A-Za-z0-9_]*$/.test(telegramAlias)) {
			throw new Error('telegram alias must consist only of latin alphabet letters and digits! ');
		}
	}

	const response: Response = await fetch(serverAUTH + '/authenticate', {
		method: 'POST',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
		},
		body: JSON.stringify({
			username: telegramAlias,
			password,
		}),
	});

	if (!response.ok) {
		const errorResponse = await response.json();
		throw new Error(errorResponse.message);
	}

	const data = await response.json();

	return data.token;
}

/**
 * Retrieves tournaments from the API.
 * @param sortBy - The sorting type either by name or by rating.
 * @param descending - The sorting order.
 * @param title - The string that tournaments has in their title.
 * @param minParticipants - The minimum number of players in tournament.
 * @param maxParticipants - The maximum number of players in tournament.
 * @param startDateString - The start date of showed tournaments.
 * @param endDateString - The end date of showed tournaments.
 * @param pageNumber - The page number.
 * @param pageSize - The page size.
 * @returns The tournaments data and total number of pages.
 */
export async function getTournaments(
	sortBy: 'date' | 'kf' | 'players' = 'date',
	descending: boolean | null,
	title: string | null,
	minParticipants: number | null,
	maxParticipants: number | null,
	startDateString: string | null,
	endDateString: string | null,
	pageNumber: number | null,
	pageSize: number | null,
): Promise<{
	tournaments: Tournament[];
	totalPages: number;
}> {
	const searchParams = objectToURLSearchParams({
		sortBy,
		descending: descending !== null ? String(descending) : null,
		titleHas: title,
		minEndDate: startDateString,
		maxEndDate: endDateString,
		minPlayers: minParticipants ? String(minParticipants) : null,
		maxParticipants: maxParticipants ? String(maxParticipants) : null,
		page: pageNumber ? String(pageNumber) : null,
		size: pageSize ? String(pageSize) : null,
	});

	const url: string = serverAPI + '/tournaments' + (searchParams && '?' + searchParams);

	const response: Response = await fetch(url, {
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: `Bearer ${token}`,
		},
	});

	await handleGetErrors(response, token);

	const totalPages: number = parseInt(response.headers.get('X-Total-Pages') ?? '100', 10);

	const data = await response.json();

	return { tournaments: data, totalPages };
}

/**
 * Retrieves tournament from the API.
 * @param id - The id of the tournament that you want to get.
 * @returns The tournament data.
 */
export async function getTournament(id: number): Promise<Tournament> {
	const url: string = serverAPI + '/tournaments/' + id;

	const response: Response = await fetch(url, {
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: `Bearer ${token}`,
		},
	});

	await handleGetErrors(response, token);

	const json = await response.json();
	const data = json.data;

	if (data.state) {
		try {
			data.state = JSON.parse(JSON.parse(data.state)) as TournamentState;
		} catch {
			data.state = JSON.parse(data.state) as TournamentState;
		}
	}
	data.state = { participants: [], ...data.state };

	return data;
}

/**
 * Updates state of the tournament.
 * @param tournamentID - The ID of the tournament.
 */
export async function updateTournament(
	tournamentID: number,
	state: TournamentState | null,
): Promise<void> {
	const response: Response = await fetch(serverAPI + '/tournaments/' + tournamentID + '/state', {
		method: 'PATCH',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: `Bearer ${token}`,
		},
		body: JSON.stringify({
			state: JSON.stringify(state),
		}),
	});

	await handleModifyErrors(response, token);
}

/**
 * Creates a new tournament.
 * @param title - The title of the tournament.
 * @param startDateString - The start date string.
 * @param endDateString - The end date string.
 * @returns The created tournament data.
 */
export async function createTournament(
	title: string,
	startDateString: string,
	endDateString: string,
): Promise<Tournament> {
	startDateString = new Date(startDateString).toLocaleDateString('ru');
	endDateString = new Date(endDateString).toLocaleDateString('ru');

	const response: Response = await fetch(serverAPI + '/tournaments', {
		method: 'POST',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: 'Bearer ' + token,
		},
		body: JSON.stringify({
			title,
			startDateString,
			endDateString,
		}),
	});

	await handleModifyErrors(response, token);

	const data = await response.json();
	return data;
}

export async function editTournament(
	id: string,
	title: string,
	startDateString: string,
	endDateString: string,
): Promise<void> {
	startDateString = new Date(startDateString).toLocaleDateString('ru');
	endDateString = new Date(endDateString).toLocaleDateString('ru');

	const response: Response = await fetch(serverAPI + '/tournaments/' + id, {
		method: 'PUT',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: 'Bearer ' + token,
		},
		body: JSON.stringify({
			title,
			startDateString,
			endDateString,
		}),
	});
	await handleModifyErrors(response, token);
}

/**
 * Deletes a tournament.
 * @param tournamentID - The ID of the tournament.
 */
export async function deleteTournament(tournamentID: string): Promise<void> {
	const response: Response = await fetch(serverAPI + '/tournaments/' + tournamentID, {
		method: 'DELETE',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: `Bearer ${token}`,
		},
	});

	await handleModifyErrors(response, token);
}

/**
 * Finishes a tournament.
 * @param tournamentID - The ID of the tournament.
 */
export async function finishTournament(tournamentID: string): Promise<void> {
	const response: Response = await fetch(serverAPI + '/tournaments/' + tournamentID, {
		method: 'PATCH',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: `Bearer ${token}`,
		},
	});

	await handleModifyErrors(response, token);
}

export async function getStatistics(playerID: number | null = null): Promise<Stats> {
	const url: string = serverAPI + '/players/' + playerID + '/stats';

	const response: Response = await fetch(url, {
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: `Bearer ${token}`,
		},
	});

	await handleGetErrors(response, token);

	const data = await response.json();

	return data;
}

/**
 * Retrieves leaders list from the API.
 * @returns The leaders data.
 */
export async function getLeaders(): Promise<Player[]> {
	const url: string = serverAPI + '/leaders';

	const response: Response = await fetch(url, {
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: `Bearer ${token}`,
		},
	});

	await handleGetErrors(response, token);

	const leaders = await response.json();

	return leaders;
}

/**
 * Promotes player to leaders through their telegram alias.
 * @param telegramAlias - The Telegram alias of the player.
 * @returns Data of the promoted leader.
 */
export async function promoteLeader(telegramAlias: string): Promise<Player> {
	if (telegramAlias[0] == '@') telegramAlias = telegramAlias.slice(1);

	const response: Response = await fetch(serverAPI + '/leaders', {
		method: 'POST',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: 'Bearer ' + token,
		},
		body: JSON.stringify({
			telegramAlias,
		}),
	});

	await handleModifyErrors(response, token);

	const data = await response.json();

	return data;
}

/**
 * Demote leader to .
 * @param leaderId - The ID of the leader.
 */
export async function demoteLeader(leaderId: string): Promise<void> {
	const response: Response = await fetch(serverAPI + '/leaders/' + leaderId, {
		method: 'DELETE',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: `Bearer ${token}`,
		},
	});

	await handleModifyErrors(response, token);
}

/**
 * Broadcasts message to all other players via telegram bot.
 * @param message - message to broadcast.
 */
export async function broadcastMessage(message: string): Promise<void> {
	const response: Response = await fetch(serverAPI + '/telegram', {
		method: 'POST',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
			Authorization: 'Bearer ' + token,
		},
		body: JSON.stringify({
			message,
		}),
	});

	await handleModifyErrors(response, token);
}

export async function getProfileData(playerID: number | string): Promise<ProfileData> {
	const url: string = serverAPI + '/players/' + playerID;

	const response: Response = await fetch(url, {
		headers: {
			Authorization: `Bearer ${token}`,
		},
	});

	await handleGetErrors(response, token);
	const resp = await response.json();
	const id = +resp.message.split(' ').at(-1);
	const data = { ...resp.data, id };

	return data;
}
