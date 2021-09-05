import {
	createUserWithEmailAndPassword,
	getAuth,
	GoogleAuthProvider,
	sendPasswordResetEmail,
	signInWithEmailAndPassword,
	signInWithPopup
} from 'firebase/auth';
import { writable } from 'svelte/store';

export type LoginFormState = {
	email: string;
	password: string;
};

export const initialState: LoginFormState = {
	email: '',
	password: ''
};
function createLoginFormStore() {
	const { subscribe } = writable<LoginFormState>(initialState);

	return {
		subscribe,
		createAccount: (email: string, password: string) =>
			createUserWithEmailAndPassword(getAuth(), email, password),
		signIn: (email: string, password: string) =>
			signInWithEmailAndPassword(getAuth(), email, password),
		signInWithGoogle: () => {
			const provider = new GoogleAuthProvider();
			return signInWithPopup(getAuth(), provider);
		},
		forgotPassword: (email: string) => sendPasswordResetEmail(getAuth(), email)
	};
}
export const loginFormStore = createLoginFormStore();
