import { Code, ConnectError } from '@connectrpc/connect';
import { error, fail } from '@sveltejs/kit';
import clone from 'just-clone';
import { getStationClient } from '$lib/server/api';
import type { PageServerLoad } from './$types';

export const load = (async ({ params, locals }) => {
	if (!locals.session?.accessToken) {
		return fail(401);
	}
	try {
		const response = await getStationClient().getOneStation(
			{
				id: params.id
			},
			{
				headers: {
					Authorization: `Bearer ${locals.session.accessToken}`
				}
			}
		);
		if (!response.station) {
			throw new Error('no data');
		}
		return {
			id: params.id,
			station: clone(response.station)
		};
	} catch (e) {
		if (e instanceof ConnectError) {
			if (e.code === Code.Unauthenticated) {
				console.error('Failed to get stations', e);
				error(401, {
					message: 'Unauthorized'
				});
			}
		}
	}
}) satisfies PageServerLoad;
