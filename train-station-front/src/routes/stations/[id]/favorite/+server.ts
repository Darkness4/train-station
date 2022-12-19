import client from '$lib/server/api';
import { redirect } from '@sveltejs/kit';
import type { RequestHandler } from './$types';

export const POST = (async ({ request, locals }) => {
	const session = (await locals.getSession()) as Session | null;
	if (!session) {
		throw redirect(302, '/');
	}

	const data = await request.json();
	if (!data.stationId) throw new Error('id not defined');
	const id = data.stationId as string;
	if (data.value === undefined) throw new Error('value not defined');
	const value = data.value;

	// TODO: handle error
	await client.setFavoriteOneStation({
		id: id,
		token: session.encodedToken,
		value: value
	});

	return new Response();
}) satisfies RequestHandler;
