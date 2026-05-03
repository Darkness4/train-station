import { Code, ConnectError } from '@connectrpc/connect';
import { error } from '@sveltejs/kit';
import { getStationClient } from '$lib/server/api';
import type { RequestHandler } from './$types';

export const POST = (async ({ request, locals }) => {
	if (!locals.session?.accessToken) {
		error(401, {
			message: 'Unauthorized'
		});
	}

	const data = await request.json();
	if (!data.stationId) throw new Error('id not defined');
	const id = data.stationId as string;
	if (data.value === undefined) throw new Error('value not defined');
	const value = data.value;

	try {
		await getStationClient().setFavoriteOneStation(
			{
				id: id,
				value: value
			},
			{
				headers: {
					Authorization: `Bearer ${locals.session.accessToken}`
				}
			}
		);
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

	return new Response();
}) satisfies RequestHandler;
