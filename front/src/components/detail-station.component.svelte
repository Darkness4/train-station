<script lang="ts">
	import maplibre from 'maplibre-gl';
	import type { Station } from '$gen/ts/trainstation/v1alpha1/station_pb';

	interface Props {
		station: Station;
	}

	let { station }: Props = $props();

	let mapComponent: HTMLDivElement;
	let map: maplibre.Map;
	let marker: maplibre.Marker;

	$effect(() => {
		if (mapComponent && !map) {
			map = new maplibre.Map({
				container: mapComponent,
				center: [station.xWgs84, station.yWgs84],
				zoom: 13,
				maxZoom: 18,
				style: {
					version: 8,
					sources: {
						'osm-tiles': {
							type: 'raster',
							tiles: ['https://tile.openstreetmap.org/{z}/{x}/{y}.png'],
							tileSize: 256,
							attribution: '© OpenStreetMap'
						}
					},
					layers: [
						{
							id: 'osm-layer',
							type: 'raster',
							source: 'osm-tiles',
							minzoom: 0,
							maxzoom: 19
						}
					]
				}
			});
			map.addControl(new maplibre.NavigationControl(), 'top-right');
			marker = new maplibre.Marker().setLngLat([station.xWgs84, station.yWgs84]).addTo(map);
		}

		if (map) {
			map.setCenter([station.xWgs84, station.yWgs84]);
			marker.setLngLat([station.xWgs84, station.yWgs84]);
		}

		return () => {
			map?.remove();
		};
	});
</script>

<section>
	<div style="height: 450px; width: 600px;" bind:this={mapComponent}></div>

	<article>
		<h1>{station.libelle}</h1>

		<ul>
			<li>Département: {station.departemen}</li>
			<li>Commune: {station.commune}</li>
			<li>Latitude: {station.yWgs84}</li>
			<li>Longitude: {station.xWgs84}</li>
		</ul>
	</article>
</section>

<style>
	@import 'maplibre-gl/dist/maplibre-gl.css';

	/* Cancel PicoCSS */
	:global .maplibregl-map [type='button'] {
		margin-bottom: unset;
	}

	:global .maplibregl-map button,
	:global .maplibregl-map [type='submit'],
	:global .maplibregl-map [type='reset'],
	:global .maplibregl-map [type='button'],
	:global .maplibregl-map [type='file']::file-selector-button,
	:global .maplibregl-map [role='button'] {
		padding: unset;
		border: unset;
		border-radius: unset;
		outline: unset;
		background-color: unset;
		box-shadow: unset;
		color: unset;
		font-weight: unset;
		font-size: unset;
		line-height: unset;
		text-align: unset;
		text-decoration: unset;
		cursor: unset;
		user-select: unset;
	}
</style>
