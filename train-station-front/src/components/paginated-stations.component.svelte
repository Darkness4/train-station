<script lang="ts">
	import ShortStation from '$components/short-station.component.svelte';
	import type { Paginated } from '$lib/entities/paginated';
	import type { Station } from '$lib/entities/station';

	export let stations: Paginated<Station>;
</script>

<section id="stations" class="section">
	{#if stations.count == 0}
		<h1 class="title">No data.</h1>
	{:else}
		{#each stations.data as station}
			<ShortStation {station} />
		{/each}
	{/if}
</section>
