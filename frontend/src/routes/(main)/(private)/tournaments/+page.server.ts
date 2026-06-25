import * as db from '$lib/server/requests';
import type { Tournament } from '$lib/types/types.js';
import { fail } from '@sveltejs/kit';

export const prerender = false;

export async function load({ url }) {
	const searchParams = url.searchParams;

	const title = searchParams.get('title');
	const minParticipants = searchParams.get('minParticipants');
	const maxParticipants = searchParams.get('maxParticipants');
	const startDateString = searchParams.get('startDateString');
	const endDateString = searchParams.get('endDateString');
	const descending = searchParams.get('descending');
	const sortBy = searchParams.get('sortBy');
	const currentPageNumber = searchParams.get('currentPageNumber');
	const currentPageSize = searchParams.get('currentPageSize');

	let data: { tournaments: Tournament[]; totalPages: number } = { tournaments: [], totalPages: 0 };
	let error: string | undefined;

	try {
		data = await db.getTournaments(
			sortBy as 'date' | 'kf' | 'players',
			descending !== null ? descending == 'true' : null,
			title,
			minParticipants ? Number(minParticipants) : null,
			maxParticipants ? Number(maxParticipants) : null,
			startDateString,
			endDateString,
			currentPageNumber ? Number(currentPageNumber) : null,
			currentPageSize ? Number(currentPageSize) : null,
		);
	} catch (e) {
		if (typeof e === 'string') {
			error = e;
		} else if (e instanceof Error) {
			error = e.message; // works, `e` narrowed to Error
		}
	}

	return {
		...data,
		error,
		title: 'Tournaments',
	};
}

export const actions = {
	createTournament: async ({ request }) => {
		const data = await request.formData();

		try {
			await db.createTournament(
				String(data.get('title') || ''),
				String(data.get('startDateString') || ''),
				String(data.get('endDateString') || ''),
			);
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
	editTournament: async ({ request }) => {
		const data = await request.formData();

		try {
			await db.editTournament(
				String(data.get('tournamentId') || ''),
				String(data.get('title') || ''),
				String(data.get('startDateString') || ''),
				String(data.get('endDateString') || ''),
			);
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
	deleteTournament: async ({ request }) => {
		const data = await request.formData();

		try {
			await db.deleteTournament(String(data.get('tournamentId') || ''));
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
	finishTournament: async ({ request }) => {
		const data = await request.formData();

		try {
			await db.finishTournament(String(data.get('tournamentId') || ''));
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
};
