<script lang="ts" context="module">
	import type { LoadInput, LoadOutput } from '@sveltejs/kit';

	let initialSearchQuery: string;
	let initialPageNumber: number;

	export async function load({ page }: LoadInput): Promise<LoadOutput> {
		initialPageNumber = 1;
		if (page.query.get('page') != null) {
			initialPageNumber = parseInt(page.query.get('page')!!);
		}

		initialSearchQuery = page.query.get('s') ?? '';

		return {};
	}
</script>

<script lang="ts">
	import { page } from '$app/stores';
	import { goto } from '$app/navigation';
	import PaginatedStations from '$components/paginated-stations.component.svelte';
	import Search from '$components/search.component.svelte';
	import { initialState, paginatedStationsStore } from '$stores/paginated-stations.store';
	import Pager from '$components/pager.component.svelte';

	let searchQuery: string = initialSearchQuery;
	let pageNumber: number = initialPageNumber;

	$: loadData(searchQuery, pageNumber);

	async function loadData(s: string, page: number) {
		try {
			await paginatedStationsStore.load({
				s: s,
				page: page
			});
		} catch (e) {
			console.error(e);
		}
	}

	function search(newPage: number) {
		goto(`${$page.path}?s=${searchQuery}&page=${newPage}`);
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

	<PaginatedStations stations={$paginatedStationsStore} />

	<Pager
		bind:page={pageNumber}
		pageCount={$paginatedStationsStore.pageCount}
		startPage={initialState.page}
		goto={search}
	/>
</section>
