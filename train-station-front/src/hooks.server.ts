import GitHub from '@auth/core/providers/github';
import { env } from '$env/dynamic/private';
import SvelteKitAuth from '$lib/auth';
import { authClient } from '$lib/server/api';

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
		jwt: async ({ token, account }) => {
			if (account && account.access_token) {
				const { response } = await authClient.getJWT({
					account: {
						accessToken: account.access_token,
						provider: account.provider,
						providerAccountId: account.providerAccountId,
						type: account.type
					}
				});
				token.stationToken = response.token;
			}
			return token;
		},
		session: async ({ session, token }) => {
			if (token.stationToken) {
				(session as Session).token = token.stationToken as string;
			}

			return session;
		}
	}
});
