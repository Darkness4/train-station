<script lang="ts" context="module">
	import 'material-design-icons/iconfont/material-icons.css';

	import type { LoadInput, LoadOutput } from '@sveltejs/kit';

	import { initializeFirebase } from '$lib/init-firebase';
	import { getAuth, onAuthStateChanged, signOut } from 'firebase/auth';

	initializeFirebase();

	const auth = getAuth();

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
	const unsubscribe = onAuthStateChanged(auth, (user) => (userOrNull = user));

	async function logOut() {
		if (userOrNull !== null) {
			await signOut(auth);
			userOrNull = null;
			return goto('/');
		}
	}

	onDestroy(() => {
		unsubscribe();
	});
</script>

{#if userOrNull}
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
