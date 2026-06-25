import * as db from '$lib/server/requests';
import type { Match } from '$lib/types/types';
import { fail } from '@sveltejs/kit';

export const prerender = false;

export async function load({ url }) {
	const searchParams = url.searchParams;

	const descending = searchParams.get('descending');
	const name = searchParams.get('name');
	const score = searchParams.get('score');
	const minDateString = searchParams.get('minDateString');
	const maxDateString = searchParams.get('maxDateString');
	const currentPageNumber = searchParams.get('currentPageNumber');
	const currentPageSize = searchParams.get('currentPageSize');

	let data: {
		matches: Match[];
		totalPages: number;
	};
	let error: string | undefined;

	try {
		data = await db.getMatches(
			descending !== null ? descending == 'true' : null,
			name,
			score,
			minDateString,
			maxDateString,
			currentPageNumber ? Number(currentPageNumber) : null,
			currentPageSize ? Number(currentPageSize) : null,
		);
	} catch (e) {
		data = { matches: [], totalPages: 0 };

		if (typeof e === 'string') {
			error = e;
		} else if (e instanceof Error) {
			error = e.message; // works, `e` narrowed to Error
		}
	}

	return {
		...data,
		error,
		title: 'Matches',
	};
}

export const actions = {
	createMatch: async ({ request }) => {
		const data = await request.formData();

		try {
			await db.createMatch(
				String(data.get('firstPlayerName') || ''),
				String(data.get('secondPlayerName') || ''),
				Number(data.get('firstPlayerScore')),
				Number(data.get('secondPlayerScore')),
				String(data.get('tournamentTitle') || ''),
				String(data.get('localDateString') || ''),
			);
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
	editMatch: async ({ request }) => {
		const data = await request.formData();

		try {
			await db.editMatch(
				String(data.get('matchId') || ''),
				String(data.get('firstPlayerName') || ''),
				String(data.get('secondPlayerName') || ''),
				Number(data.get('firstPlayerScore')),
				Number(data.get('secondPlayerScore')),
				String(data.get('tournamentTitle') || ''),
				String(data.get('localDateString') || ''),
			);
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
	deleteMatch: async ({ request }) => {
		const data = await request.formData();

		try {
			await db.deleteMatch(String(data.get('matchId') || ''));
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
};
