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

	if (!code || !state || storedState !== state || !storedCodeVerifier) {
		return new Response(
			`Please restart the process. State, code or code verifier not found. code: ${code}, state: ${state}, storedState: ${storedState}, storedCodeVerifier: ${storedCodeVerifier}`,
			{
				status: 400
			}
		);
	}

	const oidcClient = await getOidcClient();

	let tokens: OAuth2Tokens;
	try {
		tokens = await oidcClient.validateAuthorizationCode(code, storedCodeVerifier);
	} catch (e) {
		return new Response('Please restart the process. Code or code verifier was invalid.', {
			statusText: e instanceof Error ? e.message : 'An error occurred',
			status: 400
		});
	}

	const decodedIdToken = await oidcClient.verifyIdToken(tokens.idToken());

	setSessionTokenCookie(
		event,
		tokens.accessToken(),
		tokens.refreshToken(),
		tokens.accessTokenExpiresAt(),
		new Date(Date.now() + 30 * 24 * 60 * 60 * 1000),
		(decodedIdToken.name ?? decodedIdToken.username) as string
	);
	return redirect(302, '/');
};
