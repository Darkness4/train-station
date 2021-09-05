<script lang="ts" context="module">
	import { initializeFirebase } from '$lib/init-firebase';

	initializeFirebase();
</script>

<script lang="ts">
	import 'material-design-icons/iconfont/material-icons.css';
	import '../app.scss';

	import type { Auth, Unsubscribe } from 'firebase/auth';
	import { getAuth, onAuthStateChanged, signOut } from 'firebase/auth';
	import { onDestroy, onMount } from 'svelte';

	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import { authStore } from '$stores/auth.store';

	let unsubscribe: Unsubscribe;
	let auth: Auth;

	onMount(() => {
		auth = getAuth();

		unsubscribe = onAuthStateChanged(auth, (user) => {
			authStore.set({ user });

			if ($page.path !== '/' && !auth?.currentUser) {
				goto('/');
			}
		});
	});

	async function logOut() {
		if ($authStore.user) {
			await signOut(auth!);
			return goto('/');
		}
	}

	onDestroy(() => {
		if (unsubscribe !== undefined) {
			unsubscribe();
		}
	});
</script>

{#if $authStore.user}
	<nav class="navbar">
		<div class="container">
			<div id="navMenu" class="navbar-menu">
				<div class="navbar-start">
					<div class="navbar-item">
						<div class="buttons">
							<button on:click={() => goto('/')} class="button">Home</button>
						</div>
					</div>
				</div>
				<div class="navbar-end">
					<div class="navbar-item">
						<div class="buttons">
							<button on:click={logOut} class="button is-danger">Log out</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</nav>

	<div class="container">
		<slot />
	</div>
{:else}
	<slot />
{/if}
