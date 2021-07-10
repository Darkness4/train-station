<script lang="ts">
	import PaginatedStations from '$components/paginated-stations.component.svelte';

	import { paginatedStationsStore } from '$stores/paginated-stations.store';
	import { onMount } from 'svelte';

	onMount(async () => {
		await paginatedStationsStore.refresh();
	});
</script>

<svelte:head>
	<title>Train Station SNCF</title>
</svelte:head>

<PaginatedStations stations={$paginatedStationsStore} />
