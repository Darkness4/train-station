import { stationClient } from '$lib/server/api';
import { error } from '@sveltejs/kit';
import type { RequestHandler } from './$types';

export const POST = (async ({ request, locals }) => {
	if (!locals.session?.token) {
		error(401, {
			message: 'Unauthorized'
		});
	}

	const data = await request.json();
	if (!data.stationId) throw new Error('id not defined');
	const id = data.stationId as string;
	if (data.value === undefined) throw new Error('value not defined');
	const value = data.value;

	// TODO: handle error
	await stationClient.setFavoriteOneStation({
		id: id,
		value: value,
		token: locals.session?.token
	});

	return new Response();
}) satisfies RequestHandler;
