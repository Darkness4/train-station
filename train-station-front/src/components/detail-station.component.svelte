<script lang="ts">
	import type { Station } from '$lib/entities/station';
	import 'material-design-icons/iconfont/material-icons.css';

	export let station: Station;
</script>

<section class="section">
	<div class="content">
		<iframe
			title="Google Maps"
			width="600"
			height="450"
			style="border:0"
			loading="lazy"
			allowfullscreen
			src="https://www.google.com/maps/embed/v1/view?key=AIzaSyB7n9cGoEeEzl7LryVhC2HR0ukP2sqgWwo&zoom=18&maptype=satellite
				&center={station.fields?.y_wgs84 ?? 0},{station.fields?.x_wgs84 ?? 0}"
		/>

		<h1 class="title">
			{station.libelle}
		</h1>

		<ul>
			<li>Département: {station.fields?.departemen ?? 'Inconnu'}</li>
			<li>Commune: {station.fields?.commune ?? 'Inconnue'}</li>
			<li>Latitude: {station.fields?.y_wgs84 ?? 'Inconnue'}</li>
			<li>Longitude: {station.fields?.x_wgs84 ?? 'Inconnue'}</li>
		</ul>
	</div>
</section>
