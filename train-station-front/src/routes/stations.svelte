<script lang="ts" context="module">
	import type { LoadInput, LoadOutput } from '@sveltejs/kit';

	let initialSearchQuery: string;
	let initialPageNumber: number;
	export function load({ url }: LoadInput): Promise<LoadOutput> {
		initialPageNumber = 1;
		const queryPage = url.searchParams.get('page');
		if (queryPage !== null) {
			initialPageNumber = parseInt(queryPage);
		}
		initialSearchQuery = url.searchParams.get('s') ?? '';
		return Promise.resolve({});
	}
</script>

<script lang="ts">
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import Pager from '$components/pager.component.svelte';
	import PaginatedStations from '$components/paginated-stations.component.svelte';
	import Search from '$components/search.component.svelte';
	import type { Station } from '$lib/entities/station';
	import { authStore } from '$stores/auth.store';
	import { initialState, paginatedStationsStore } from '$stores/paginated-stations.store';

	let searchQuery: string = initialSearchQuery;
	let pageNumber: number = initialPageNumber;
	$: loadData(searchQuery, pageNumber);
	async function loadData(s: string, page: number) {
		try {
			const token = $authStore.token;
			if (token) {
				await paginatedStationsStore.load(token, { s: s, page: page });
			}
		} catch (e) {
			console.error(e);
		}
	}
	function search(newPage: number) {
		return goto(`${$page.url.pathname}?s=${searchQuery}&page=${newPage}`);
	}
	function onClick(station: Station) {
		return goto(`/stations/${station.recordid}`);
	}
	async function onFavorite(station: Station) {
		try {
			const token = $authStore.token;
			if (token) {
				await paginatedStationsStore.makeFavorite(station.recordid, !station.is_favorite, token);
			}
		} catch (e) {
			console.error(e);
		}
	}
</script>

<svelte:head>
	<title>Stations list</title>
</svelte:head>

<section class="section">
	<Search bind:value={searchQuery} onConfirm={() => search(pageNumber)} />

	<Pager
		bind:page={pageNumber}
		pageCount={$paginatedStationsStore.pageCount}
		startPage={initialState.page}
		goto={search}
	/>

	<PaginatedStations stations={$paginatedStationsStore} {onClick} {onFavorite} />

	<Pager
		bind:page={pageNumber}
		pageCount={$paginatedStationsStore.pageCount}
		startPage={initialState.page}
		goto={search}
	/>
</section>
