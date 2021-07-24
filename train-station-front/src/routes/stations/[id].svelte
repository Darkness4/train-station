<script context="module" lang="ts">
	import type { LoadInput, LoadOutput } from '@sveltejs/kit';
	import firebase from 'firebase';

	import DetailStation from '$components/detail-station.component.svelte';
	import { stationStore } from '$stores/station.store';

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

<svelte:head>
	<title>Station {id}</title>
</svelte:head>

<DetailStation station={$stationStore} />
