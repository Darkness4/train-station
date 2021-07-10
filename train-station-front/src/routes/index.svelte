<script>
	import PaginatedStations from '$components/paginated-stations.component.svelte';

	import { paginatedStationsStore } from '$stores/paginated-stations.store';
	import { onMount } from 'svelte';

	onMount(async () => {
		await paginatedStationsStore.refresh();
	});
</script>

<PaginatedStations stations={$paginatedStationsStore} />
