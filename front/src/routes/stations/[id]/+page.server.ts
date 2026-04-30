import { fail } from '@sveltejs/kit';
import clone from 'just-clone';
import { getStationClient } from '$lib/server/api';
import type { PageServerLoad } from './$types';

export const load = (async ({ params, locals }) => {
	if (!locals.session?.accessToken) {
		return fail(401);
	}
	// TODO: handle error
	const response = await getStationClient().getOneStation(
		{
			id: params.id,
		},
		{
			headers: {
				Authorization: `Bearer ${locals.session.accessToken}`,
			},
		},
	);
	if (!response.station) {
		throw new Error('no data');
	}
	return {
		id: params.id,
		station: clone(response.station),
	};
}) satisfies PageServerLoad;
