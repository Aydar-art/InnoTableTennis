import { redirect } from '@sveltejs/kit';
import { base } from '$app/paths';

export const actions = {
	default: async ({ cookies }) => {
		cookies.delete('userToken');
		throw redirect(308, `${base}/`);
	},
};
