<script lang="ts">
	import ShortStation from '$components/short-station.component.svelte';
	import type { Paginated } from '$lib/entities/paginated';
	import type { Station } from '$lib/entities/station';

	export let isLoading: boolean = false;
	export let stations: Paginated<Station>;
	export let onClick: (station: Station) => unknown;
	export let onFavorite: (station: Station) => unknown;
</script>

<section id="stations" class="section">
	{#if isLoading}
		<progress class="progress is-small is-primary" max="100">15%</progress>
	{/if}
	{#if stations.count == 0}
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
