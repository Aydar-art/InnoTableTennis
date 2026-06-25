import { base } from '$app/paths';
import * as db from '$lib/server/requests';
import { getRoles } from '$lib/server/token';
import type { Player } from '$lib/types/types';
import { fail, redirect } from '@sveltejs/kit';

export const prerender = false;

export async function load({ cookies }) {
	const token = cookies.get('userToken') ?? '';
	const userRoles = getRoles(token);
	const isLeader = userRoles.includes('LEADER');
	if (!isLeader) {
		throw redirect(308, `${base}/`);
	}

	let leaders: Player[] = [];
	let error: string | undefined;

	try {
		leaders = await db.getLeaders();
	} catch (e) {
		if (typeof e === 'string') {
			error = e;
		} else if (e instanceof Error) {
			error = e.message;
		}
	}

	return {
		leaders,
		error,
		title: 'Admin Panel',
	};
}

export const actions = {
	promoteLeader: async ({ request }) => {
		const data = await request.formData();

		try {
			await db.promoteLeader(String(data.get('telegramAlias') || ''));
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
	demoteLeader: async ({ request }) => {
		const data = await request.formData();

		try {
			await db.demoteLeader(String(data.get('id') || ''));
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
	broadcastMessage: async ({ request }) => {
		const data = await request.formData();

		const message = data.get('message');

		try {
			if (message) await db.broadcastMessage(String(message));
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
	},
};
