import { createTournament } from '$lib/server/db';
import { redirect } from '@sveltejs/kit';

/** @type {import('./$types').Actions} */
export const actions = {
  default: async (event) => {
    const formData = await event.request.formData();
    createTournament({
      title: formData.get('title') as string,
      slug: formData.get('slug') as string,
      date: new Date().toISOString().split('T')[0],
      participantsCount: formData.get('participantsCount') ? parseInt(formData.get('participantsCount') as string) : 0,
      gamesPlayedCount: 0,
      customText: 'In progress',
    });
    throw redirect(303, '/profile/active');
  },
};
