<script context="module" lang="ts">
	import { stationStore } from '$stores/station.store';
	import type { LoadInput, LoadOutput } from '@sveltejs/kit';

	let id: string;

	export async function load({ page }: LoadInput): Promise<LoadOutput> {
		id = page.params.id;
		try {
			await stationStore.load(page.params.id);
		} catch (e) {
			return {
				error: e
			};
		}

		return {};
	}
</script>

<svelte:head>
	<title>{id}</title>
</svelte:head>

<p>{$stationStore?.recordid ?? 'Object is null'}</p>
