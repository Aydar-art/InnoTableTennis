import type { PageServerLoad } from './$types';

import * as db from '$lib/server/requests';
import { countDeltas } from '$lib/server/tournament.utils';
import type { Tournament } from '$lib/types/types';

export const load: PageServerLoad = async ({ params }) => {
	const id = +params.id;

	let tournament: Tournament = {} as Tournament;
	let deltas: number[] = [];
	let error: string | undefined;

	try {
		tournament = await db.getTournament(id);
		deltas = countDeltas(tournament);
	} catch (e) {
		if (typeof e === 'string') {
			error = e;
		} else if (e instanceof Error) {
			error = e.message;
		}
	}

	return {
		title: tournament.title,
		tournament,
		deltas,
		error,
	};
};
