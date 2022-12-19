<script lang="ts">
	import { page } from '$app/stores';
	import AuthCard from '$components/auth/auth-card.component.svelte';
	import { signIn } from '$lib/auth/client';
</script>

<svelte:head>
	<title>Train Station Login</title>
</svelte:head>

{#if !$page.data.session}
	<main class="container">
		<AuthCard onLoginGithub={() => signIn('github')} />
	</main>
{/if}
