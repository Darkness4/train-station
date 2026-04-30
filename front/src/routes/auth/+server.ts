import { generateCodeVerifier, generateState } from 'arctic';
import { getOidcClient } from '$lib/server/oauth';
import type { RequestHandler } from './$types';

export const GET: RequestHandler = async (event) => {
	const state = generateState();
	const codeVerifier = generateCodeVerifier();
	const oidcClient = await getOidcClient()
	const url = oidcClient.createAuthorizationURLWithPKCE(state, codeVerifier);

	// store state as cookie
	event.cookies.set('state', state, {
		secure: import.meta.env.PROD,
		path: '/',
		httpOnly: true,
		maxAge: 60 * 10, // 10 min
	});

	// store code verifier as cookie
	event.cookies.set('code_verifier', codeVerifier, {
		secure: import.meta.env.PROD,
		path: '/',
		httpOnly: true,
		maxAge: 60 * 10, // 10 min
	});

	return new Response(null, {
		status: 302,
		headers: {
			Location: url.toString(),
		},
	});
};
