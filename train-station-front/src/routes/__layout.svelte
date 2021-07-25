<script lang="ts" context="module">
	import type { LoadInput, LoadOutput } from '@sveltejs/kit';

	import { initializeFirebase } from '$lib/init-firebase';
	import firebase from 'firebase';

	initializeFirebase();

	const auth = firebase.auth();

	export async function load({ page }: LoadInput): Promise<LoadOutput> {
		if (page.path !== '/' && auth.currentUser === null) {
			return {
				redirect: '/',
				status: 302
			};
		}
		return {};
	}
</script>

<script lang="ts">
	import '../app.scss';

	import { onDestroy } from 'svelte';

	import { goto } from '$app/navigation';

	let userOrNull: any | null = null;
	const unsubscribe = auth.onAuthStateChanged((user) => (userOrNull = user));

	async function logOut() {
		if (userOrNull !== null) {
			await auth.signOut();
			userOrNull = null;
			return goto('/');
		}
	}

	onDestroy(() => {
		unsubscribe();
	});
</script>

<nav class="navbar">
	<div class="container">
		<div id="navMenu" class="navbar-menu">
			<div class="navbar-end">
				<div class="navbar-item">
					<div class="buttons">
						{#if userOrNull}
							<button on:click={logOut} class="button is-danger">Log out</button>
						{/if}
					</div>
				</div>
			</div>
		</div>
	</div>
</nav>

<div class="container">
	<slot />
</div>
