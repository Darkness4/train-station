<script lang="ts">
	import type { Station } from '$gen/ts/trainstation/v1alpha1/station';

	export let station: Station;
	export let onClick: () => unknown;
	export let onFavorite: () => unknown;

	function onClickFavorite() {
		onFavorite();
		station.isFavorite = !station.isFavorite;
	}
</script>

<article class="m-2">
	<hgroup>
		<h1>{station.libelle}</h1>
		<h2 class="text-ellipsis overflow-hidden whitespace-nowrap max-md:max-w-xs">{station.id}</h2>
	</hgroup>
	<div>
		<button on:click={onClick}>
			<span class="align-middle">Details</span>
		</button>
		<button on:click={onClickFavorite}>
			<span
				class="material-symbols-outlined align-middle"
				style={station.isFavorite
					? "font-variation-settings: 'FILL' 1, 'wght' 400, 'GRAD' 0, 'opsz' 48;"
					: ''}
				aria-hidden="true">star</span
			>
			<span class="align-middle">Favorite</span>
		</button>
	</div>
</article>
