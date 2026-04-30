<script lang="ts">
	import ShortStation from '$components/short-station.component.svelte';
	import type {
		PaginatedStation,
		Station,
	} from '$gen/ts/trainstation/v1alpha1/station_pb';

	interface Props {
		stations: PaginatedStation;
		onClick: (station: Station) => unknown;
		onFavorite: (station: Station) => unknown;
	}
	let { stations, onClick, onFavorite }: Props = $props();
</script>

<section id="stations" class="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
	{#if stations.count === 0n}
		<h1>No data.</h1>
	{:else}
		{#each stations.data as station (station.id)}
			<ShortStation {station} {onClick} {onFavorite} />
		{/each}
	{/if}
</section>
