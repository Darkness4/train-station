<script lang="ts">
	import { fade } from 'svelte/transition';

	export let signUp: boolean;
	export let email: string;
	export let password: string;
	let confirmPassword: string;
	let error: Error | null;
	export let onLogin: (email: string, password: string) => Promise<unknown>;
	export let onLoginWithGoogle: () => Promise<unknown>;
	export let onCreateAccount: (email: string, password: string) => Promise<unknown>;
	export let showCreateAccount: () => void;
	export let showSignIn: () => void;
	export let showForgotPassword: () => void;
	function validateConfirmPassword() {
		if (password != confirmPassword) {
			throw new Error('Passwords are not identical.');
		}
	}
	async function _onSubmit() {
		try {
			if (!signUp) {
				await onLogin(email, password);
			} else {
				validateConfirmPassword();
				await onCreateAccount(email, password);
			}
		} catch (e) {
			if (e instanceof Error) {
				error = e;
			} else {
				console.error(e);
			}
		}
	}
</script>

<form on:submit|preventDefault={_onSubmit}>
	<div class="field">
		<label for="email" class="label">Email</label>
		<div class="control has-icons-left">
			<input
				bind:value={email}
				id="email"
				name="email"
				type="email"
				placeholder="e.g. johndoe@gmail.com"
				class="input"
				required
			/>
			<span class="icon is-small is-left"> <i class="material-icons">email</i></span>
		</div>
	</div>
	<div class="field">
		<label for="password" class="label">Password</label>
		<div class="control has-icons-left">
			<input
				bind:value={password}
				id="password"
				name="password"
				type="password"
				placeholder="********"
				class="input"
				required
			/>
			<span class="icon is-small is-left"> <i class="material-icons">lock</i></span>
		</div>
	</div>
	{#if !signUp}
		{#if error}
			<div transition:fade class="notification is-danger is-light">
				<button type="button" on:click={() => (error = null)} class="delete" />{error.message}
			</div>
		{/if}
		<div in:fade class="field">
			<div class="control">
				<button
					on:click={showForgotPassword}
					type="button"
					class="button is-link is-small is-inverted">Forgot password ?</button
				>
			</div>
		</div>
		<div class="field is-grouped">
			<div class="control">
				<button type="submit" class="button is-primary">Login</button>
			</div>
			<div class="control">
				<button on:click={showCreateAccount} type="button" class="button is-light"
					>Create an account</button
				>
			</div>
		</div>
	{:else}
		<div in:fade class="field">
			<label for="confirm-password" class="label">Confirm Password</label>
			<div class="control has-icons-left">
				<input
					bind:value={confirmPassword}
					id="confirm-password"
					name="confirm-password"
					type="password"
					placeholder="*******"
					class="input"
					required
				/>
				<span class="icon is-small is-left">
					<i class="material-icons">lock</i>
				</span>
			</div>
		</div>
		{#if error}
			<div transition:fade class="notification is-danger is-light">
				<button type="button" on:click={() => (error = null)} class="delete" />{error.message}
			</div>
		{/if}
		<div class="field is-grouped">
			<div class="control">
				<button type="submit" class="button is-primary">Create an account</button>
			</div>
			<div class="control">
				<button on:click={showSignIn} type="button" class="button is-link"
					>Already have an account ?</button
				>
			</div>
		</div>
	{/if}
	<hr />
	<div class="field">
		<div class="control">
			<button on:click={onLoginWithGoogle} type="button" class="button is-primary"
				>Login with Google</button
			>
		</div>
	</div>
</form>
