import type { Handle } from '@sveltejs/kit';
import { sequence } from '@sveltejs/kit/hooks';
import { getOidcClient } from '$lib/server/oauth';
import { TokenBucket } from '$lib/server/rate-limit';
import { deleteSessionTokenCookie, setSessionTokenCookie } from '$lib/server/session';

const bucket = new TokenBucket<string>(100, 1);

const rateLimitHandle: Handle = async ({ event, resolve }) => {
	// Note: Assumes X-Forwarded-For will always be defined.
	const clientIP = event.request.headers.get('X-Forwarded-For');
	if (clientIP === null) {
		return resolve(event);
	}
	let cost: number;
	if (event.request.method === 'GET' || event.request.method === 'OPTIONS') {
		cost = 1;
	} else {
		cost = 3;
	}
	if (!bucket.consume(clientIP, cost)) {
		return new Response('Too many requests', {
			status: 429
		});
	}
	return resolve(event);
};

const authHandle: Handle = async ({ event, resolve }) => {
	if (event.locals.session) {
		return resolve(event);
	}

	const accessToken = event.cookies.get('train.access_token');
	const refreshToken = event.cookies.get('train.refresh_token');
	const username = event.cookies.get('train.username');

	if (!accessToken || !refreshToken || !username) {
		event.locals.session = null;
	} else {
		event.locals.session = {
			accessToken,
			refreshToken,
			username
		};
	}

	return resolve(event);
};

const refreshHandle: Handle = async ({ event, resolve }) => {
	const accessToken = event.cookies.get('train.access_token');
	const refreshToken = event.cookies.get('train.refresh_token');

	if (!accessToken && refreshToken) {
		try {
			const oidcClient = await getOidcClient();
			const tokens = await oidcClient.refreshAccessToken(refreshToken);
			const decodedIdToken = await oidcClient.verifyIdToken(tokens.idToken());
			const username = (decodedIdToken.name ?? decodedIdToken.username) as string;

			setSessionTokenCookie(
				event,
				tokens.accessToken(),
				tokens.refreshToken(),
				tokens.accessTokenExpiresAt(),
				new Date(Date.now() + 30 * 24 * 60 * 60 * 1000),
				username
			);

			event.locals.session = {
				accessToken: tokens.accessToken(),
				refreshToken: tokens.refreshToken(),
				username: username
			};
		} catch (e) {
			console.error('failed to refresh token', e);
			deleteSessionTokenCookie(event);
		}
	}

	return await resolve(event);
};

export const handle = sequence(rateLimitHandle, refreshHandle, authHandle);
