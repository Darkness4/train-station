import { github } from '$lib/server/oauth';
import { createSession, setSessionTokenCookie } from '$lib/server/session';

import type { OAuth2Tokens } from 'arctic';
import type { RequestEvent } from './$types';

export async function GET(event: RequestEvent): Promise<Response> {
	const storedState = event.cookies.get('github_oauth_state') ?? null;
	const code = event.url.searchParams.get('code');
	const state = event.url.searchParams.get('state');

	if (storedState === null || code === null || state === null) {
		return new Response('Please restart the process.', {
			status: 400
		});
	}
	if (storedState !== state) {
		return new Response('Please restart the process.', {
			status: 400
		});
	}

	let tokens: OAuth2Tokens;
	try {
		tokens = await github.validateAuthorizationCode(code);
	} catch (e) {
		return new Response('Please restart the process.', {
			statusText: e instanceof Error ? e.message : 'An error occurred',
			status: 400
		});
	}

	const githubAccessToken = tokens.accessToken();

	const userRequest = new Request('https://api.github.com/user');
	userRequest.headers.set('Authorization', `Bearer ${githubAccessToken}`);
	const userResponse = await fetch(userRequest);
	const userResult = (await userResponse.json()) as {
		id: number;
	};

	const session = await createSession(githubAccessToken, userResult.id.toString());
	setSessionTokenCookie(event, session);
	return new Response(null, {
		status: 302,
		headers: {
			Location: '/'
		}
	});
}
