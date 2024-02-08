import { env } from '$env/dynamic/private';
import { authClient } from '$lib/server/api';
import type { Provider } from '@auth/core/providers';
import GitHub from '@auth/core/providers/github';
import type { Profile } from '@auth/core/types';
import { SvelteKitAuth } from '@auth/sveltekit';

export const { handle, signIn, signOut } = SvelteKitAuth({
	providers: [
		GitHub({
			clientId: env.GITHUB_ID ?? 'bed15795fa3da2de6e6a',
			clientSecret: env.GITHUB_SECRET ?? '6d1801da84cf5f3c747f95dcb936f3f8'
		}) as Provider<Profile>
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
		redirect({ url, baseUrl }) {
			// Allows relative callback URLs
			if (url.startsWith('/')) return `${baseUrl}${url}`;
			// Allows callback URLs on the same origin
			else if (new URL(url).origin === baseUrl) return url;
			return baseUrl;
		},
		session: async ({ session, token }) => {
			if (token.stationToken) {
				(session as Session).token = token.stationToken as string;
			}

			return session;
		}
	}
});
