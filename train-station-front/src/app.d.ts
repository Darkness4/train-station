// See https://kit.svelte.dev/docs/types#app
// for information about these interfaces
// and what to do when importing types
declare namespace App {
	// interface Error {}
	// interface Locals {}
	// interface PageData {}
	// interface Platform {}
}

interface Session {
	encodedToken: string;
	user?: {
		name?: string | null;
		email?: string | null;
		image?: string | null;
		id?: string | null;
	};
	expires: ISODateString;
}
