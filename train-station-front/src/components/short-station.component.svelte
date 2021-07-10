<script lang="ts">
	import type { Station } from '$lib/entities/station';

	export let station: Station;
</script>

<div class="container max-w-sm bg-gray-50 shadow-lg rounded overflow-hidden my-2 mx-2">
	<div class="px-6 py-4">
		<h2 class="text-gray-800 text-3xl font-semibold mb-2">{station.libelle}</h2>
		<p class="text-gray-600 text-base">{station.recordid}</p>
	</div>
</div>
