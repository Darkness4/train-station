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

<article>
	<div class="grid">
		<hgroup>
			<h1>{station.libelle}</h1>
			<h2>{station.id}</h2>
		</hgroup>
		<div>
			<button on:click={onClick}>
				<span>Details</span>
			</button>
			<button on:click={onClickFavorite}>
				<i class="material-icons" aria-hidden="true"
					>{station.isFavorite ? 'favorite' : 'favorite_border'}</i
				>
				<span>Favorite</span>
			</button>
		</div>
	</div>
</article>
