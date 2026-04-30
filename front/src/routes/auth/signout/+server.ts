import { redirect } from '@sveltejs/kit';
import { getOidcClient } from '$lib/server/oauth';
import { deleteSessionTokenCookie } from '$lib/server/session';
import type { RequestHandler } from './$types';

export const GET: RequestHandler = async (event) => {
	const oauth = await getOidcClient();
	oauth.revokeToken(event.cookies.get('train.access_token') ?? '');
	deleteSessionTokenCookie(event);
	return redirect(302, '/');
};
