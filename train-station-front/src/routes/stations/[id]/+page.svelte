<script lang="ts">
	import { page } from '$app/stores';
	import DetailStation from '$components/detail-station.component.svelte';
	import { authStore } from '$stores/auth.store';
	import { stationStore } from '$stores/station.store';

	$: id = $page.params.id;
	$: if ($authStore.token) {
		stationStore.load(id, $authStore.token).catch((e) => console.error(e));
	}
	$: station = $stationStore;
</script>

<svelte:head>
	<title>Station {id}</title>
</svelte:head>

{#if station}
	<DetailStation {station} />
{/if}
