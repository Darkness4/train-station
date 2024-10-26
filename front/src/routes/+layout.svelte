<script lang="ts">
	import background from '$lib/assets/background.svg';
	import '../app.scss';

	import { page } from '$app/stores';

	let { children } = $props();
</script>

{#if $page.data.session}
	<nav class="px-2">
		<ul>
			<li><a href="/"><strong>Train Stations</strong></a></li>
		</ul>
		<ul>
			<li class="max-md:hidden">
				<span
					><small>Signed in as</small>
					<strong>{$page.data.session.user?.name ?? 'User'}</strong></span
				>
			</li>
			<li>
				{#if $page.data.session.user?.image}
					<img src={$page.data.session.user.image} alt="avatar" class="h-8 w-8" />
				{/if}
			</li>
			<!-- svelte-ignore a11y_invalid_attribute -->
			<li>
				<a href="/auth/signout" role="button">Log out</a>
			</li>
		</ul>
	</nav>
{/if}

<main class="relative">
	<div
		class="absolute inset-0 z-0 h-full w-full bg-cover bg-center"
		style="background-image: url('{background}');"
	></div>
	<div class="relative z-10">
		{@render children?.()}
	</div>
</main>
