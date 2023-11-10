import { redirect } from '@sveltejs/kit';
import type { LayoutServerLoad } from './$types';

export const load: LayoutServerLoad = async ({ url, locals }) => {
	const session = await locals.getSession();
	if (!session?.user && url.pathname !== '/' && !url.pathname.startsWith('/oauth2')) {
		throw redirect(302, '/');
	}
	return {
		session: session
	};
};
