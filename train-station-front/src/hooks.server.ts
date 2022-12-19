import GitHub from '@auth/core/providers/github';
import { env } from '$env/dynamic/private';
import jwt from 'jsonwebtoken';
import SvelteKitAuth from '$lib/auth';

export const handle = SvelteKitAuth({
	providers: [
		GitHub({
			clientId: env.GITHUB_ID ?? 'bed15795fa3da2de6e6a',
			clientSecret: env.GITHUB_SECRET ?? '6d1801da84cf5f3c747f95dcb936f3f8'
		})
	],
	session: {
		strategy: 'jwt'
	},
	callbacks: {
		session: async ({ session, token }) => {
			(session as Session).encodedToken = jwt.sign(
				token,
				env.AUTH_SECRET ?? '7db5cdd9d2f3ed72662bd8a9ea331844898e9151cee8818ec3cf127d2a08e89d',
				{ algorithm: 'HS256' }
			);
			return session;
		}
	}
});
