import { base } from '$app/paths';
import * as db from '$lib/server/requests';
import { fail, redirect } from '@sveltejs/kit';

export const prerender = false;

export function load() {
	return {
		title: 'Login',
	};
}

export const actions = {
	default: async ({ request, cookies }) => {
		const data = await request.formData();

		try {
			const token = await db.authenticate(
				String(data.get('username') || ''),
				String(data.get('password') || ''),
			);
			cookies.set('userToken', token, { path: '/' });
		} catch (error) {
			return fail(422, {
				error: (<Error>error).message,
			});
		}
		throw redirect(308, `${base}/`);
	},
};
