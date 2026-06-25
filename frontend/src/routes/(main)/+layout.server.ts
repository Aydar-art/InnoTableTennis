import * as db from '$lib/server/requests';

import { userToken } from '$lib/server/stores';
import { getExpirationDate, getRoles, getUsername } from '$lib/server/token';
import type { ProfileData } from '$lib/types/types.profile';
import type { LayoutServerLoad } from './$types';

export const prerender = false;

export const load: LayoutServerLoad = async ({ cookies }) => {
	const token = cookies.get('userToken') ?? '';

	const isTokenExpired = getExpirationDate(token) < new Date();

	if (isTokenExpired) {
		token == '';
		cookies.delete('userToken');
	}

	userToken.set(token);

	const userRoles = getRoles(token);
	const username = getUsername(token);

	const isAuthorized = userRoles.includes('USER');
	const isLeader = userRoles.includes('LEADER') && isAuthorized;
	const isAdmin = userRoles.includes('ADMIN') && isAuthorized;

	let playerInfo: ProfileData | undefined;
	let error: string | undefined;

	if (isAuthorized && !isAdmin) {
		try {
			playerInfo = await db.getProfileData(username);
		} catch (e) {
			if (typeof e === 'string') {
				error = e;
			} else if (e instanceof Error) {
				error = e.message;
			}
		}
	}
	return {
		isAuthorized,
		isLeader,
		isAdmin,
		username,
		playerInfo,
		error,
	};
};
