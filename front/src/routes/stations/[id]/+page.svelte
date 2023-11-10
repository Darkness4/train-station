<script lang="ts">
	import DetailStation from '$components/detail-station.component.svelte';
	import type { PageData } from './$types';

	export let data: PageData;
</script>

<svelte:head>
	<title>Station {data.id}</title>
</svelte:head>

{#if data.station}
	<DetailStation station={data.station} />
{/if}
