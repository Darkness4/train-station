import SvelteKitAuth from '@auth/sveltekit';
import GitHub from '@auth/core/providers/github';
import { env } from '$env/dynamic/private';
import jwt from 'jsonwebtoken';

export const handle = SvelteKitAuth({
	providers: [GitHub({ clientId: env.GITHUB_ID, clientSecret: env.GITHUB_SECRET })],
	session: {
		strategy: 'jwt'
	},
	callbacks: {
		session: async ({ session, token }) => {
			(session as Session).encodedToken = jwt.sign(token, env.AUTH_SECRET, { algorithm: 'HS256' });
			return session;
		}
	}
});
