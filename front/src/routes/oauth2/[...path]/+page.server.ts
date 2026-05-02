import { redirect } from '@sveltejs/kit';

export const trailingSlash = 'ignore';

export async function load({ url }) {
	redirect(308, `com.example.trainstationapp://oauth2/?${url.searchParams.toString()}`);
}
