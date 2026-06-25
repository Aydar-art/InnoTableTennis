import type { PageServerLoad } from './$types';

import * as db from '$lib/server/requests';
import type { ProfileData, Stats } from '$lib/types/types.profile';

export const load: PageServerLoad = async ({ params }) => {
	let profileData: ProfileData = {} as ProfileData;
	let profileStats: Stats = {} as Stats;
	let error: string | undefined;

	try {
		profileData = await db.getProfileData(params.id);
		profileStats = await db.getStatistics(profileData.id);
	} catch (e) {
		if (typeof e === 'string') {
			error = e;
		} else if (e instanceof Error) {
			error = e.message; // works, `e` narrowed to Error
		}
	}

	return {
		id: params.id,
		title: profileData.name,
		profileData,
		profileStats,
		error,
	};
};
