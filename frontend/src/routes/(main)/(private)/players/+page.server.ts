import * as db from '$lib/server/requests';
import type { Player } from '$lib/types/types.js';
import { fail } from '@sveltejs/kit';

export const prerender = false;

export async function load({ url }) {
	const searchParams = url.searchParams;

	const name = searchParams.get('name');
	const telegramAlias = searchParams.get('telegramAlias');
	const minRating = searchParams.get('minRating');
	const maxRating = searchParams.get('maxRating');
	const descending = searchParams.get('descending');
	const sortBy = searchParams.get('sortBy');
	const currentPageNumber = searchParams.get('currentPageNumber');
	const currentPageSize = searchParams.get('currentPageSize');

	let data: {
		players: Player[];
		totalPages: number;
	};
	let error: string | undefined;

	try {
		data = await db.getPlayers(
			sortBy as 'name' | 'rating',
			descending !== null ? descending == 'true' : null,
			name,
			telegramAlias,
			minRating ? Number(minRating) : null,
			maxRating ? Number(maxRating) : null,
			currentPageNumber ? Number(currentPageNumber) : null,
			currentPageSize ? Number(currentPageSize) : null,
		);
	} catch (e) {
		data = { players: [], totalPages: 0 };

		if (typeof e === 'string') {
			error = e;
		} else if (e instanceof Error) {
			error = e.message; // works, `e` narrowed to Error
		}
	}

	return {
		...data,
		error,
		title: 'Players',
	};
}

export const actions = {
	createPlayer: async ({ request }) => {
		const data = await request.formData();
		try {
			await db.createPlayer(
				String(data.get('name') || ''),
				String(data.get('telegramAlias') || ''),
				Number(data.get('rating') || '0'),
			);
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
	editPlayer: async ({ request }) => {
		const data = await request.formData();

		try {
			await db.editPlayer(
				String(data.get('playerId') || ''),
				String(data.get('name') || ''),
				String(data.get('telegramAlias') || ''),
				Number(data.get('rating') || '0'),
			);
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
	deletePlayer: async ({ request }) => {
		const data = await request.formData();

		try {
			await db.deletePlayer(String(data.get('playerId') || ''));
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
};
