import * as db from '$lib/server/requests';

export async function PUT({ params, request }) {
	const {
		firstPlayerName,
		secondPlayerName,
		firstPlayerScore,
		secondPlayerScore,
		tournamentTitle,
		localDateString,
	} = await request.json();
	const id = params.id;

	await db.editMatch(
		id,
		firstPlayerName,
		secondPlayerName,
		firstPlayerScore,
		secondPlayerScore,
		tournamentTitle,
		localDateString,
	);
	return new Response(null, { status: 204 });
}
