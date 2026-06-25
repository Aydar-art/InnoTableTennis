import { deleteTournament, getTournaments } from '$lib/server/db';
import type { PageServerLoad } from './$types';
import { redirect } from '@sveltejs/kit';

export const load = (() => {
  const tournaments = getTournaments('archived');
  return {
    tournaments,
  };
}) satisfies PageServerLoad;

/** @type {import('./$types').Actions} */
export const actions = {
  default: async (event) => {
    const formData = await event.request.formData();
    if (formData.has('slug')) {
      const slug = formData.get('slug') as string;
      deleteTournament(slug);
      throw redirect(303, '/profile/archived');
    }
  },
};
