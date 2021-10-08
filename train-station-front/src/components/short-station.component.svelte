<script lang="ts">
	import type { Station } from '$lib/entities/station';
	export let station: Station;
	export let onClick: () => unknown;
	export let onFavorite: () => unknown;
</script>

<div class="block">
	<div class="card">
		<div class="card-content content">
			<h2 class="title">{station.libelle}</h2>
			<p class="subtitle">{station.recordid}</p>
		</div>
		<div class="card-footer">
			<button class="button is-inverted is-link card-footer-item" on:click={onClick}>
				<span>Details</span>
			</button>
			<button class="button is-inverted is-primary card-footer-item" on:click={onFavorite}>
				<i class="material-icons" aria-hidden="true"
					>{station.is_favorite ? 'favorite' : 'favorite_border'}</i
				>
				<span>Favorite</span>
			</button>
		</div>
	</div>
</div>
