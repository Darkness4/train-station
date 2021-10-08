<script lang="ts">
	import ForgotPassword from '$components/auth/forgot-password.component.svelte';
	import SignIn from '$components/auth/sign-in.component.svelte';
	export let email: string;
	export let password: string;
	export let onForgotPassword: (email: string) => Promise<unknown>;
	export let onLogin: (email: string, password: string) => Promise<unknown>;
	export let onCreateAccount: (email: string, password: string) => Promise<unknown>;
	export let onLoginWithGoogle: () => Promise<unknown>;
	let signUp = false;
	let forgotPassword = false;
	function showCreateAccount() {
		signUp = true;
		forgotPassword = false;
	}
	function showSignIn() {
		signUp = false;
		forgotPassword = false;
	}
	function showForgotPassword() {
		signUp = false;
		forgotPassword = true;
	}
</script>

<div class="card">
	<div class="card-content">
		<div class="content">
			{#if forgotPassword}
				<ForgotPassword {onForgotPassword} {email} />
			{:else}
				<SignIn
					{onCreateAccount}
					{onLogin}
					{onLoginWithGoogle}
					{signUp}
					{email}
					{password}
					{showCreateAccount}
					{showSignIn}
					{showForgotPassword}
				/>
			{/if}
		</div>
	</div>
</div>
