import * as db from '$lib/server/requests';
import { json } from '@sveltejs/kit';

export async function POST({ request }) {
	const {
		firstPlayerName,
		secondPlayerName,
		firstPlayerScore,
		secondPlayerScore,
		tournamentTitle,
		localDateString,
	} = await request.json();

	const data = await db.createMatch(
		firstPlayerName,
		secondPlayerName,
		firstPlayerScore,
		secondPlayerScore,
		tournamentTitle,
		localDateString,
	);

	return json(data, { status: 201 });
}
