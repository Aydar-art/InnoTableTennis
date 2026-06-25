import * as db from '$lib/server/requests';

export async function PATCH({ params, request }) {
	const { state } = await request.json();
	const id = +params.id;

	await db.updateTournament(id, state);
	return new Response(null, { status: 204 });
}
