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

const auth = getAuth();

export const initialState: LoginFormState = {
	email: '',
	password: ''
};
function createLoginFormStore() {
	const { subscribe } = writable<LoginFormState>(initialState);

	return {
		subscribe,
		createAccount: (email: string, password: string) =>
			createUserWithEmailAndPassword(auth, email, password),
		signIn: (email: string, password: string) => signInWithEmailAndPassword(auth, email, password),
		signInWithGoogle: () => {
			const provider = new GoogleAuthProvider();

			return signInWithPopup(auth, provider);
		},
		forgotPassword: (email: string) => sendPasswordResetEmail(auth, email)
	};
}
export const loginFormStore = createLoginFormStore();
