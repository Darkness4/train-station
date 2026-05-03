<script lang="ts">
	import './layout.scss';
	import './layout.css';
	import { resolve } from '$app/paths';
	import { page } from '$app/state';
	import background from '$lib/assets/background.svg';
	import favicon from '$lib/assets/favicon.svg';

	let { children } = $props();
</script>

<svelte:head><link rel="icon" href={favicon} /></svelte:head>

{#if page.data.session}
	<nav class="px-2">
		<ul>
			<li>
				<a href={resolve('/')}><strong>Train Stations</strong></a>
			</li>
		</ul>
		<ul>
			<li class="max-md:hidden">
				<span
					><small>Signed in as</small>
					<strong>{page.data.session.username ?? 'User'}</strong></span
				>
			</li>
			<li><a href={resolve('/auth/signout')} role="button">Log out</a></li>
		</ul>
	</nav>
{/if}

<main class="relative">
	<div
		class="absolute inset-0 z-0 h-full w-full bg-cover bg-center"
		style="background-image: url('{background}');"
	></div>
	<div class="relative z-10">{@render children?.()}</div>
</main>
