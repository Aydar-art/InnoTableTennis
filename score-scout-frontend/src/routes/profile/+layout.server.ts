import type { LayoutServerLoad } from './$types';
import { verifyToken } from '$lib/server/auth';
import { redirect } from '@sveltejs/kit';

export const load = (async ({ cookies }) => {
  const token = cookies.get('token');
  if (!token) throw redirect(300, '/auth/signin');
  const email = verifyToken(token);
  if (!email) throw redirect(300, '/auth/signin');
  return {
    user: email,
  };
}) satisfies LayoutServerLoad;
