import { stationClient } from '$lib/server/api';
import type { PageServerLoad } from './$types';
import clone from 'just-clone';
import { redirect } from '@sveltejs/kit';

export const load = (async ({ url, locals }) => {
	const page = parseInt(url.searchParams.get('page') ?? '1');
	const searchQuery = url.searchParams.get('s') ?? '';
	const session = (await locals.getSession()) as Session | null;
	if (!session || !session.token) {
		throw redirect(302, '/');
	}

	// TODO: handle error
	const { response } = await stationClient.getManyStations({
		limit: 10,
		page: page,
		query: searchQuery,
		token: session.token
	});
	if (!response.stations) {
		throw new Error('no data');
	}
	return {
		stations: clone(response.stations),
		session: session
	};
}) satisfies PageServerLoad;
