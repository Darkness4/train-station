<script lang="ts">
	import 'firebaseui/dist/firebaseui.css';

	import firebase from 'firebase';
import type { auth } from 'firebaseui';
import { onDestroy, onMount } from 'svelte';
	import { goto } from '$app/navigation';

	const unsubcribe = firebase.auth().onAuthStateChanged((user) => {
		if (user) {
			goto('/stations');
			unsubcribe();
		}
	});

	let ui: auth.AuthUI;
	onMount(async () => {
		const firebaseui = await import('firebaseui');
		ui = new firebaseui.auth.AuthUI(firebase.auth());
		ui.start('#firebaseui-auth-container', {
			signInSuccessUrl: '/stations',
			signInOptions: [
				firebase.auth.GoogleAuthProvider.PROVIDER_ID,
				firebase.auth.EmailAuthProvider.PROVIDER_ID
			]
		});
	});
	onDestroy(async () => {
		await ui?.delete();
	});
</script>

<svelte:head>
	<title>Train Station Login</title>
</svelte:head>

<div class="hero is-large">
	<div class="hero-body">
		<div id="firebaseui-auth-container" />
	</div>
</div>
