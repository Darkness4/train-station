import { decode } from 'jsonwebtoken';
import { authClient } from './api';

import type { RequestEvent } from '@sveltejs/kit';

export function setSessionTokenCookie(event: RequestEvent, session: Session): void {
	event.cookies.set('session', session.token, {
		httpOnly: true,
		path: '/',
		secure: import.meta.env.PROD,
		sameSite: 'lax',
		expires: session.expiresAt
	});
}

export function deleteSessionTokenCookie(event: RequestEvent): void {
	event.cookies.set('session', '', {
		httpOnly: true,
		path: '/',
		secure: import.meta.env.PROD,
		sameSite: 'lax',
		maxAge: 0
	});
}

export async function createSession(accessToken: string, userId: string): Promise<Session> {
	const { response } = await authClient.getJWT({
		account: {
			accessToken: accessToken,
			provider: 'github',
			providerAccountId: userId,
			type: 'oauth'
		}
	});

	const payload = decode(response.token, { json: true }) as {
		exp: number;
		name: string;
		picture: string;
	} | null;
	return {
		token: response.token,
		expiresAt: payload?.exp ? new Date(payload.exp * 1000) : new Date(),
		user: {
			name: payload?.name ?? '',
			image: payload?.picture ?? ''
		}
	};
}

export interface Session {
	token: string;
	expiresAt: Date;
	user: {
		name: string;
		image: string;
	};
}
