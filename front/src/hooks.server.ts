import { TokenBucket } from '$lib/server/rate-limit';
import { sequence } from '@sveltejs/kit/hooks';
import { decode } from 'jsonwebtoken';

import type { Handle } from '@sveltejs/kit';

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
	const token = event.cookies.get('session') ?? null;
	if (token === null) {
		event.locals.session = null;
		return resolve(event);
	}

	const decoded = decode(token, { json: true });
	if (!decoded || !decoded?.exp) {
		event.locals.session = null;
		return resolve(event);
	}

	if (decoded.exp * 1000 < Date.now()) {
		event.locals.session = null;
		return resolve(event);
	}

	event.locals.session = {
		token: token,
		expiresAt: new Date(decoded.exp * 1000),
		user: {
			name: decoded.name,
			image: decoded.picture
		}
	};

	return resolve(event);
};

export const handle = sequence(rateLimitHandle, authHandle);
