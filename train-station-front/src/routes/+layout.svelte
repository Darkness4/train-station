<script lang="ts">
	import '../app.scss';

	import { page } from '$app/stores';
	import { signOut } from '@auth/sveltekit/client';
</script>

{#if $page.data.session}
	<nav class="container-fluid">
		<ul>
			<li><a href="/"><strong>Train Stations</strong></a></li>
		</ul>
		<ul>
			<li>
				<span
					><small>Signed in as</small>
					<strong>{$page.data.session.user?.name ?? 'User'}</strong></span
				>
			</li>
			<li>
				{#if $page.data.session.user?.image}
					<img src={$page.data.session.user.image} alt="avatar" class="avatar" />
				{/if}
			</li>
			<li><a href="/">Home</a></li>
			<!-- svelte-ignore a11y-invalid-attribute -->
			<li><a on:click={() => signOut()} href="#" role="button">Log out</a></li>
		</ul>
	</nav>

	<div class="container">
		<slot />
	</div>
{:else}
	<slot />
{/if}
