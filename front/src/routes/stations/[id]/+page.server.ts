import { stationClient } from '$lib/server/api';
import { fail } from '@sveltejs/kit';
import clone from 'just-clone';
import type { PageServerLoad } from './$types';

export const load = (async ({ params, locals }) => {
	if (!locals.session?.token) {
		return fail(401);
	}
	// TODO: handle error
	const { response } = await stationClient.getOneStation({
		id: params.id,
		token: locals.session?.token
	});
	if (!response.station) {
		throw new Error('no data');
	}
	return {
		id: params.id,
		station: clone(response.station)
	};
}) satisfies PageServerLoad;
