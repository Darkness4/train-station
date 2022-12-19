<script lang="ts">
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import Pager from '$components/pager.component.svelte';
	import PaginatedStations from '$components/paginated-stations.component.svelte';
	import Search from '$components/search.component.svelte';
	import type { Station } from '$gen/ts/trainstation/v1alpha1/station';
	import type { PageData } from './$types';

	export let data: PageData;

	let stations = data.stations;

	let searchQuery: string = '';

	function search(newPage: number) {
		return goto(`${$page.url.pathname}?s=${searchQuery}&page=${newPage}`);
	}
	function onClick(station: Station) {
		return goto(`/stations/${station.id}`);
	}
	async function onFavorite(station: Station) {
		await fetch(`/stations/${station.id}/favorite`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				stationId: station.id,
				value: !station.isFavorite
			})
		});
	}
</script>

<svelte:head>
	<title>Stations list</title>
</svelte:head>

<section class="section">
	<Search bind:value={searchQuery} onConfirm={() => search(data.stations.page)} />

	<Pager page={stations.page} pageCount={stations.pageCount} startPage={1} goto={search} />

	<PaginatedStations {stations} {onClick} {onFavorite} />

	<Pager page={stations.page} pageCount={stations.pageCount} startPage={1} goto={search} />
</section>
