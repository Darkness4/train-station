<script context="module" lang="ts">
	import type { LoadInput, LoadOutput } from '@sveltejs/kit';
	import firebase from 'firebase';

	let id: string;

	export async function load({ page }: LoadInput): Promise<LoadOutput> {
		id = page.params.id;
		try {
			const user = firebase.auth().currentUser;
			if (user !== null) {
				const token = await user.getIdToken();
				await stationStore.load(page.params.id, token);
			}
		} catch (e) {
			return { error: e };
		}
		return {};
	}
</script>

<script lang="ts">
	import DetailStation from '$components/detail-station.component.svelte';

	import { stationStore } from '$stores/station.store';
	import type { Station } from '$lib/entities/station';

	import { onDestroy } from 'svelte';

	let station: Station | null;
	const unsubscribe = stationStore.subscribe((it) => (station = it));

	onDestroy(unsubscribe);
</script>

<svelte:head>
	<title>Station {id}</title>
</svelte:head>

{#if station}
	<DetailStation {station} />
{/if}
