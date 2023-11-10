<script lang="ts">
	import type { Station } from '$gen/ts/trainstation/v1alpha1/station';

	export let station: Station;
</script>

<section>
	<iframe
		title="Google Maps"
		width="600"
		height="450"
		style="border:0"
		loading="lazy"
		allowfullscreen
		src="https://www.google.com/maps/embed/v1/view?key=AIzaSyB7n9cGoEeEzl7LryVhC2HR0ukP2sqgWwo&zoom=18&maptype=satellite
				&center={station.yWgs84},{station.xWgs84}"
	/>

	<article>
		<h1>
			{station.libelle}
		</h1>

		<ul>
			<li>Département: {station.departemen}</li>
			<li>Commune: {station.commune}</li>
			<li>Latitude: {station.yWgs84}</li>
			<li>Longitude: {station.xWgs84}</li>
		</ul>
	</article>
</section>
