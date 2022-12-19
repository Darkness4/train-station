import SvelteKitAuth from '@auth/sveltekit';
import GitHub from '@auth/core/providers/github';
import { GITHUB_ID, GITHUB_SECRET, AUTH_SECRET } from '$env/static/private';
import jwt from 'jsonwebtoken';

export const handle = SvelteKitAuth({
	providers: [GitHub({ clientId: GITHUB_ID, clientSecret: GITHUB_SECRET })],
	session: {
		strategy: 'jwt'
	},
	callbacks: {
		session: async ({ session, token }) => {
			(session as Session).encodedToken = jwt.sign(token, AUTH_SECRET, { algorithm: 'HS256' });
			return session;
		}
	}
});
