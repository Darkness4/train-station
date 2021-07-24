<script lang="ts" context="module">
	import { goto } from '$app/navigation';
	import { initializeFirebase } from '$lib/init-firebase';
	import type { LoadOutput, LoadInput } from '@sveltejs/kit';
	import firebase from 'firebase/app';
	import { onDestroy, onMount } from 'svelte';
	import '../app.scss';

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
	let userOrNull: firebase.User | null = null;
	const unsubscribe = firebase.auth().onAuthStateChanged((user) => (userOrNull = user));

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
