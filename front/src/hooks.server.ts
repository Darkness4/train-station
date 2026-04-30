import type { Handle } from '@sveltejs/kit';
import { sequence } from '@sveltejs/kit/hooks';
import { TokenBucket } from '$lib/server/rate-limit';

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
			status: 429,
		});
	}
	return resolve(event);
};

const authHandle: Handle = async ({ event, resolve }) => {
	const accessToken = event.cookies.get('train.access_token') ?? null;
	if (accessToken === null) {
		event.locals.session = null;
		return resolve(event);
	}

	const refreshToken = event.cookies.get('train.refresh_token') ?? null;
	if (refreshToken === null) {
		event.locals.session = null;
		return resolve(event);
	}

	const username = event.cookies.get('train.username') ?? null;
	if (username === null) {
		event.locals.session = null;
		return resolve(event);
	}

	event.locals.session = {
		accessToken: accessToken,
		refreshToken: refreshToken,
		username: username,
	};

	return resolve(event);
};

export const handle = sequence(rateLimitHandle, authHandle);
