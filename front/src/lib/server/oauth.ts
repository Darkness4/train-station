import { env } from '$env/dynamic/private';
import { GitHub } from 'arctic';

export const github = new GitHub(
	env.GITHUB_ID ?? 'bed15795fa3da2de6e6a',
	env.GITHUB_SECRET ?? '6d1801da84cf5f3c747f95dcb936f3f8',
	env.GITHUB_REDIRECT_URI ?? 'http://localhost:5173/auth/github/callback'
);
