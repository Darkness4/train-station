import { redirect } from '@sveltejs/kit';
import type { OAuth2Tokens } from 'arctic';
import { getOidcClient } from '$lib/server/oauth';
import { setSessionTokenCookie } from '$lib/server/session';
import type { RequestHandler } from './$types';

export const GET: RequestHandler = async (event) => {
	const code = event.url.searchParams.get('code');
	const state = event.url.searchParams.get('state');
	const storedState = event.cookies.get('state');
	const storedCodeVerifier = event.cookies.get('code_verifier');

	const oidcClient = await getOidcClient()

	if (!code || !state || storedState !== state || !storedCodeVerifier) {
		return new Response('Please restart the process.', {
			status: 400,
		});
	}

	let tokens: OAuth2Tokens;
	try {
		tokens = await oidcClient.validateAuthorizationCode(code, storedCodeVerifier);
	} catch (e) {
		return new Response('Please restart the process.', {
			statusText: e instanceof Error ? e.message : 'An error occurred',
			status: 400,
		});
	}

	const accessToken = tokens.accessToken();
	const expiresAt = tokens.accessTokenExpiresAt();
	const idToken = tokens.idToken();
	const decodedIdToken = await oidcClient.verifyIdToken(idToken);

	setSessionTokenCookie(
		event,
		accessToken,
		tokens.refreshToken(),
		expiresAt,
		(decodedIdToken.name ?? decodedIdToken.username) as string,
	);
	return redirect(302, '/');
};
