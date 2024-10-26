import { stationClient } from '$lib/server/api';
import { error } from '@sveltejs/kit';
import clone from 'just-clone';
import type { PageServerLoad } from './$types';

export const load = (async ({ url, locals }) => {
	const page = parseInt(url.searchParams.get('page') ?? '1');
	const searchQuery = url.searchParams.get('s') ?? '';
	if (!locals.session?.token) {
		error(401, {
			message: 'Unauthorized'
		});
	}

	// TODO: handle error
	const { response } = await stationClient.getManyStations({
		limit: 12,
		page: page,
		query: searchQuery,
		token: locals.session?.token
	});
	if (!response.stations) {
		throw new Error('no data');
	}
	return {
		stations: clone(response.stations)
	};
}) satisfies PageServerLoad;
