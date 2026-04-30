<script lang="ts">
	import { SvelteURLSearchParams } from 'svelte/reactivity';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';
	import { page } from '$app/state';
	import Pager from '$components/pager.component.svelte';
	import PaginatedStations from '$components/paginated-stations.component.svelte';
	import Search from '$components/search.component.svelte';
	import type { Station } from '$gen/ts/trainstation/v1alpha1/station_pb';

	let { data } = $props();

	let searchQuery = $state('');

	function search(
		event: SubmitEvent & { currentTarget: EventTarget & HTMLFormElement },
		search: string,
	) {
		event.preventDefault();
		return goto(resolve(`/stations?s=${search}`));
	}
	function gotoPage(newPage: bigint) {
		const params = new SvelteURLSearchParams(page.url.searchParams);
		params.set('page', newPage.toString());
		return goto(resolve(`/stations?${params.toString()}`));
	}
	function onClick(station: Station) {
		return goto(
			resolve('/stations/[id]', {
				id: station.id,
			}),
		);
	}
	async function onFavorite(station: Station) {
		await fetch(`/stations/${station.id}/favorite`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				stationId: station.id,
				value: !station.isFavorite,
			}),
		});
	}
</script>

<svelte:head> <title>Stations list</title> </svelte:head>

<section>
	<div class="flex justify-center">
		<Search
			bind:value={searchQuery}
			onConfirm={(event) => search(event, searchQuery)}
		/>
	</div>

	<Pager
		page={data.stations.page}
		pageCount={data.stations.pageCount}
		startPage={1n}
		goto={gotoPage}
	/>

	<PaginatedStations stations={data.stations} {onClick} {onFavorite} />

	<Pager
		page={data.stations.page}
		pageCount={data.stations.pageCount}
		startPage={1n}
		goto={gotoPage}
	/>
</section>
