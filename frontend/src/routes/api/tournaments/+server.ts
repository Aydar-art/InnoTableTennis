import * as db from '$lib/server/requests';

import { json } from '@sveltejs/kit';

export async function GET() {
	const { tournaments } = await db.getTournaments(
		'date',
		null,
		null,
		null,
		null,
		null,
		null,
		1,
		1000000,
	);
	return json(tournaments);
}
