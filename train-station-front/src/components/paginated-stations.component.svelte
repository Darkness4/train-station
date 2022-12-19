<script lang="ts">
	import ShortStation from '$components/short-station.component.svelte';
	import type { PaginatedStation, Station } from '$gen/ts/trainstation/v1alpha1/station';

	export let stations: PaginatedStation;
	export let onClick: (station: Station) => unknown;
	export let onFavorite: (station: Station) => unknown;
</script>

<section id="stations" class="section">
	{#if stations.count === 0}
		<h1 class="title">No data.</h1>
	{:else}
		{#each stations.data as station}
			<ShortStation
				{station}
				onClick={() => onClick(station)}
				onFavorite={() => onFavorite(station)}
			/>
		{/each}
	{/if}
</section>
