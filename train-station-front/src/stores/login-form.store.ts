import { app } from '$lib/init-firebase';
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
			createUserWithEmailAndPassword(getAuth(app), email, password),
		signIn: (email: string, password: string) =>
			signInWithEmailAndPassword(getAuth(app), email, password),
		signInWithGoogle: () => {
			const provider = new GoogleAuthProvider();
			return signInWithPopup(getAuth(app), provider);
		},
		forgotPassword: (email: string) => sendPasswordResetEmail(getAuth(app), email)
	};
}
export const loginFormStore = createLoginFormStore();
