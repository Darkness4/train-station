import { error } from '@sveltejs/kit';
import clone from 'just-clone';
import { getStationClient } from '$lib/server/api';
import type { PageServerLoad } from './$types';

export const load = (async ({ url, locals }) => {
	const page = BigInt(url.searchParams.get('page') ?? '1');
	const searchQuery = url.searchParams.get('s') ?? '';
	if (!locals.session?.accessToken) {
		error(401, {
			message: 'Unauthorized',
		});
	}

	// TODO: handle error
	const response = await getStationClient().getManyStations(
		{
			limit: 12n,
			page: page,
			query: searchQuery,
		},
		{
			headers: {
				Authorization: `Bearer ${locals.session.accessToken}`,
			},
		},
	);
	if (!response.stations) {
		throw new Error('no data');
	}
	return {
		stations: clone(response.stations),
	};
}) satisfies PageServerLoad;
