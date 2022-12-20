import { stationClient } from '$lib/server/api';
import type { PageServerLoad } from './$types';
import clone from 'just-clone';
import { redirect } from '@sveltejs/kit';

export const load = (async ({ params, locals }) => {
	const session = (await locals.getSession()) as Session | null;
	if (!session || !session.token) {
		throw redirect(302, '/');
	}

	// TODO: handle error
	const { response } = await stationClient.getOneStation({
		id: params.id,
		token: session.token
	});
	if (!response.station) {
		throw new Error('no data');
	}
	return {
		id: params.id,
		station: clone(response.station),
		session: session
	};
}) satisfies PageServerLoad;
