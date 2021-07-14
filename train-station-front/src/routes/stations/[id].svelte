<script context="module" lang="ts">
	import DetailStation from '$components/detail-station.component.svelte';
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
	<title>Station {id}</title>
</svelte:head>

<DetailStation station={$stationStore} />
