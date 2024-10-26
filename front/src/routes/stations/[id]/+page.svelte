<script lang="ts">
	import DetailStation from '$components/detail-station.component.svelte';

	let { data } = $props();
</script>

<svelte:head>
	<title>Station {data.id}</title>
</svelte:head>

{#if data.station}
	<DetailStation station={data.station} />
{/if}
