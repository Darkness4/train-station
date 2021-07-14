<script lang="ts" context="module">
	import type { LoadInput, LoadOutput } from '@sveltejs/kit';

	export async function load({}: LoadInput): Promise<LoadOutput> {
		return {
			status: 302,
			redirect: '/stations'
		};
	}
</script>

<svelte:head>
	<title>Train Station SNCF</title>
</svelte:head>
