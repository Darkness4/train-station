<script lang="ts">
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import Pager from '$components/pager.component.svelte';
	import PaginatedStations from '$components/paginated-stations.component.svelte';
	import Search from '$components/search.component.svelte';
	import type { Station } from '$gen/ts/trainstation/v1alpha1/station';

	let { data } = $props();

	let searchQuery = $state('');

	function search(search: string) {
		return goto(`${$page.url.pathname}?s=${search}`);
	}
	function gotoPage(newPage: number) {
		const params = new URLSearchParams($page.url.searchParams);
		params.set('page', newPage.toString());
		return goto(`${$page.url.pathname}?${params.toString()}`);
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

<section>
	<div class="flex justify-center">
		<Search bind:value={searchQuery} onConfirm={() => search(searchQuery)} />
	</div>

	<Pager
		page={data.stations.page}
		pageCount={data.stations.pageCount}
		startPage={1}
		goto={gotoPage}
	/>

	<PaginatedStations stations={data.stations} {onClick} {onFavorite} />

	<Pager
		page={data.stations.page}
		pageCount={data.stations.pageCount}
		startPage={1}
		goto={gotoPage}
	/>
</section>
