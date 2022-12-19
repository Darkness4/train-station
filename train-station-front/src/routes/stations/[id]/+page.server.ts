import client from '$lib/server/api';
import type { PageServerLoad } from './$types';
import { cloneDeep } from 'lodash';
import { redirect } from '@sveltejs/kit';

export const load = (async ({ params, locals }) => {
	const session = (await locals.getSession()) as Session | null;
	if (!session) {
		throw redirect(302, '/');
	}

	// TODO: handle error
	const { response } = await client.getOneStation({ id: params.id, token: session.encodedToken });
	if (!response.station) {
		throw new Error('no data');
	}
	return {
		id: params.id,
		station: cloneDeep(response.station),
		session: session
	};
}) satisfies PageServerLoad;
