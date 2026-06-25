import * as db from '$lib/server/requests';

import { json } from '@sveltejs/kit';

export async function GET() {
	const { players } = await db.getPlayers('name', null, null, null, null, null, 1, 1000000);
	return json(players);
}
