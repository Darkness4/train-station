import type { User } from 'firebase/auth';
import { writable } from 'svelte/store';

export type AuthState = {
	user: User | null;
};

export const initialState: AuthState = {
	user: null
};
function createAuthStore() {
	const { subscribe, set } = writable<AuthState>(initialState);

	return {
		subscribe,
		set
	};
}
export const authStore = createAuthStore();
