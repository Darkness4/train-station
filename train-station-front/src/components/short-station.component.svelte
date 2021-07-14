<script lang="ts">
	import type { Station } from '$lib/entities/station';
	import { goto } from '$app/navigation';
	import 'material-design-icons/iconfont/material-icons.css';
	import StationRepository from '$lib/api/train-station';

	export let station: Station;

	function onClick() {
		goto(`/stations/${station.recordid}`);
	}

	function onFavorite() {
		station.is_favorite = !station.is_favorite;
		StationRepository.updateById(station.recordid, station);
	}
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
