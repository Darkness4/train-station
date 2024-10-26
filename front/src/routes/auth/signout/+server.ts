import { deleteSessionTokenCookie } from '$lib/server/session';
import { redirect } from '@sveltejs/kit';
import type { RequestEvent } from './$types';

export function GET(event: RequestEvent): Response {
	deleteSessionTokenCookie(event);
	return redirect(302, '/');
}
