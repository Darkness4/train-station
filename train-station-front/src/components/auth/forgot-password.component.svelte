<script lang="ts">
	import { fade } from 'svelte/transition';

	export let onForgotPassword: (email: string) => Promise<unknown>;
	export let email: string;
	let error: Error | null;
	async function _onForgotPassword() {
		try {
			await onForgotPassword(email);
		} catch (e) {
			if (e instanceof Error) {
				error = e;
			} else {
				console.error(e);
			}
		}
	}
</script>

<form action="">
	<div class="field">
		<label for="" class="label">Email</label>
		<div class="control has-icons-left">
			<input
				bind:value={email}
				type="email"
				placeholder="e.g. johndoe@gmail.com"
				class="input"
				required
			/>
			<span class="icon is-small is-left">
				<i class="fa fa-envelope" />
			</span>
		</div>
	</div>
	{#if error}
		<div transition:fade class="notification is-danger is-light">
			<button type="button" on:click={() => (error = null)} class="delete" />{error.message}
		</div>
	{/if}
	<div class="field">
		<button on:click={_onForgotPassword} class="button is-success">Send recovery e-mail</button>
	</div>
	<div class="is-divider" data-content="OR" />
</form>
