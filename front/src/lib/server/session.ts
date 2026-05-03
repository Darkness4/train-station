import type { RequestEvent } from '@sveltejs/kit';

export function setSessionTokenCookie(
	event: RequestEvent,
	accessToken: string,
	refreshToken: string,
	expiresAt: Date,
	refreshTokenExpiresAt: Date,
	username: string
): void {
	event.cookies.set('train.access_token', accessToken, {
		httpOnly: true,
		path: '/',
		secure: import.meta.env.PROD,
		expires: expiresAt,
		sameSite: 'strict'
	});

	event.cookies.set('train.refresh_token', refreshToken, {
		httpOnly: true,
		path: '/',
		secure: import.meta.env.PROD,
		expires: refreshTokenExpiresAt,
		sameSite: 'strict'
	});

	event.cookies.set('train.username', username, {
		httpOnly: true,
		path: '/',
		secure: import.meta.env.PROD,
		expires: refreshTokenExpiresAt,
		sameSite: 'strict'
	});
}

export function deleteSessionTokenCookie(event: RequestEvent): void {
	event.cookies.set('train.access_token', '', {
		httpOnly: true,
		path: '/',
		secure: import.meta.env.PROD,
		maxAge: 0,
		sameSite: 'strict'
	});

	event.cookies.set('train.refresh_token', '', {
		httpOnly: true,
		path: '/',
		secure: import.meta.env.PROD,
		maxAge: 0,
		sameSite: 'strict'
	});

	event.cookies.set('train.username', '', {
		httpOnly: true,
		path: '/',
		secure: import.meta.env.PROD,
		maxAge: 0,
		sameSite: 'strict'
	});
}
