<script lang="ts">
	import { goto } from '$app/navigation';
	import AuthCard from '$components/auth/auth-card.component.svelte';
	import { authStore } from '$stores/auth.store';
	import { loginFormStore } from '$stores/login-form.store';
	$: if ($authStore.user) {
		goto('/stations');
	}
</script>

<svelte:head>
	<title>Train Station Login</title>
</svelte:head>

{#if $authStore.user === null}
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
{/if}
