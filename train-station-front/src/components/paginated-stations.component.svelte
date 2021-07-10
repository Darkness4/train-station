<script lang="ts">
	import ShortStation from '$components/short-station.component.svelte';
	import type { Paginated } from '$lib/entities/paginated';
	import type { Station } from '$lib/entities/station';

	export let stations: Paginated<Station>;
</script>

{#each stations.data as station}
	<ShortStation {station} />
{/each}
