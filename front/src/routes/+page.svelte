<script lang="ts">
	import { page } from '$app/stores';
	import AuthCard from '$components/auth/auth-card.component.svelte';
	import loginBg from '$lib/assets/login-bg.jpg?format=webp';
	import { signIn } from '@auth/sveltekit/client';
</script>

<svelte:head>
	<title>Train Station Login</title>
</svelte:head>

{#if !$page.data.session}
	<div class="h-screen w-screen bg-cover bg-center" style="background-image: url('{loginBg}');">
		<div class="h-screen w-screen backdrop-blur flex items-center justify-center">
			<AuthCard onLoginGithub={() => signIn('github')} />
		</div>
	</div>
{/if}
