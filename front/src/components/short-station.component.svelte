<script lang="ts">
	import type { Station } from '$gen/ts/trainstation/v1alpha1/station';

	interface Props {
		station: Station;
		onClick: (station: Station) => unknown;
		onFavorite: (station: Station) => unknown;
	}

	let { station, onClick, onFavorite }: Props = $props();

	function onClickFavorite() {
		onFavorite(station);
		station = {
			...station,
			isFavorite: !station.isFavorite
		};
	}
</script>

<article class="m-2">
	<hgroup>
		<h1>{station.libelle}</h1>
		<h2 class="overflow-hidden text-ellipsis whitespace-nowrap max-md:max-w-xs">{station.id}</h2>
	</hgroup>
	<div>
		<button onclick={() => onClick(station)}>
			<span class="align-middle">Details</span>
		</button>
		<button onclick={onClickFavorite}>
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
