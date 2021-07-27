<script lang="ts" context="module">
	import type { LoadInput, LoadOutput } from '@sveltejs/kit';

	let initialSearchQuery: string;
	let initialPageNumber: number;

	export async function load({ page }: LoadInput): Promise<LoadOutput> {
		initialPageNumber = 1;
		const queryPage = page.query.get('page');
		if (queryPage !== null) {
			initialPageNumber = parseInt(queryPage);
		}
		initialSearchQuery = page.query.get('s') ?? '';
		return {};
	}
</script>

<script lang="ts">
	import { getAuth } from 'firebase/auth';

	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import Pager from '$components/pager.component.svelte';
	import PaginatedStations from '$components/paginated-stations.component.svelte';
	import Search from '$components/search.component.svelte';
	import StationRepository from '$lib/api/train-station';
	import type { Station } from '$lib/entities/station';
	import { initialState, paginatedStationsStore } from '$stores/paginated-stations.store';

	const auth = getAuth();

	let searchQuery: string = initialSearchQuery;
	let pageNumber: number = initialPageNumber;

	$: loadData(searchQuery, pageNumber);

	async function loadData(s: string, page: number) {
		try {
			const user = auth.currentUser;
			if (user !== null) {
				const token = await user.getIdToken();
				await paginatedStationsStore.load(token, {
					s: s,
					page: page
				});
			}
		} catch (e) {
			console.error(e);
		}
	}

	function search(newPage: number) {
		return goto(`${$page.path}?s=${searchQuery}&page=${newPage}`);
	}

	function onClick(station: Station) {
		return goto(`/stations/${station.recordid}`);
	}

	async function onFavorite(station: Station) {
		try {
			const user = auth.currentUser;
			if (user !== null) {
				const token = await user.getIdToken();
				station.is_favorite = !station.is_favorite;
				await StationRepository.updateById(station.recordid, station, token);
				await loadData(searchQuery, pageNumber);
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
