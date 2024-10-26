import { redirect } from '@sveltejs/kit';

export async function load({ url, locals }) {
	if (!locals?.session && url.pathname !== '/' && !url.pathname.startsWith('/oauth2')) {
		redirect(302, '/');
	}
	return {
		session: locals?.session
	};
}
