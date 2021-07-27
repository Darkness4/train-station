<script lang="ts" context="module">
	import type { LoadInput, LoadOutput } from '@sveltejs/kit';
	import { getAuth } from 'firebase/auth';

	export async function load({ page }: LoadInput): Promise<LoadOutput> {
		if (getAuth().currentUser !== null) {
			return {
				redirect: '/stations',
				status: 302
			};
		}
		return {};
	}
</script>

<script lang="ts">
	import { goto } from '$app/navigation';
	import AuthCard from '$components/auth/auth-card.component.svelte';
	import { loginFormStore } from '$stores/login-form.store';
	import { onAuthStateChanged } from 'firebase/auth';

	const auth = getAuth();

	const unsubcribe = onAuthStateChanged(auth, (user) => {
		if (user) {
			goto('/stations');
			unsubcribe();
		}
	});
</script>

<svelte:head>
	<title>Train Station Login</title>
</svelte:head>

<div class="hero is-fullheight is-primary">
	<div class="hero-body">
		<AuthCard
			email={$loginFormStore.email}
			password={$loginFormStore.password}
			onCreateAccount={loginFormStore.createAccount}
			onLogin={loginFormStore.signIn}
			onLoginWithGoogle={async () => loginFormStore.signInWithGoogle()}
			onForgotPassword={loginFormStore.forgotPassword}
		/>
	</div>
</div>
