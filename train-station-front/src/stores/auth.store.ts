import type { User } from 'firebase/auth';
import { writable } from 'svelte/store';

export type AuthState = {
	user?: User | null;
	token?: string | null;
};

export const initialState: AuthState = {
	user: undefined,
	token: undefined
};
function createAuthStore() {
	const { subscribe, set } = writable<AuthState>(initialState);

	return {
		subscribe,
		set: async (user: User | null) => {
			const token = (await user?.getIdToken()) ?? null;
			set({
				user,
				token
			});
		}
	};
}
export const authStore = createAuthStore();
