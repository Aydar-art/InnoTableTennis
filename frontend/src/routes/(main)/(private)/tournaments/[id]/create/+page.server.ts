import type { PageServerLoad } from './$types';

import * as db from '$lib/server/requests';
import type { Tournament } from '$lib/types/types';

export const load: PageServerLoad = async ({ params, depends }) => {
	depends('tournament:update');

	const id = +params.id;

	let tournament: Tournament = {} as Tournament;
	let error: string | undefined;

	try {
		tournament = await db.getTournament(id);
	} catch (e) {
		if (typeof e === 'string') {
			error = e;
		} else if (e instanceof Error) {
			error = e.message;
		}
	}

	return {
		title: `${tournament.title} Constructor`,
		tournament,
		error,
	};
};
