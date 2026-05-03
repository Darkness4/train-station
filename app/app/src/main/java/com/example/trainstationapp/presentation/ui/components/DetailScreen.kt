package com.example.trainstationapp.presentation.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.trainstationapp.R
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.presentation.ui.theme.TrainStationAppTheme
import com.example.trainstationapp.presentation.ui.theme.Typography
import com.example.trainstationapp.presentation.viewmodels.DetailViewModel
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle.Json
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position
import kotlin.collections.listOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val station by viewModel.station.collectAsState()
    val scaffoldState =
        rememberBottomSheetScaffoldState(
            bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded),
        )
    val cameraState = rememberCameraState()

    LaunchedEffect(station) {
        station?.let {
            cameraState.animateTo(
                finalPosition = cameraState.position.copy(
                    target = Position(
                        latitude = it.yWgs84,
                        longitude = it.xWgs84,
                    ),
                    zoom = 13.0,
                ),
            )
        }
    }

    val error by viewModel.error.collectAsState()
    LaunchedEffect(error) {
        if (!error.isNullOrEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
    station?.let { station ->
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = { DescriptionCard(station = station) },
        ) {
            MaplibreMap(
                modifier = modifier.fillMaxSize(),
                cameraState = cameraState,
                baseStyle = Json {
                    put("version", 8)
                    putJsonObject("metadata") {}
                    putJsonObject("sources") {
                        putJsonObject("osm-tiles") {
                            put("type", "raster")
                            putJsonArray("tiles") {
                                add("https://tile.openstreetmap.org/{z}/{x}/{y}.png")
                            }
                            put("tileSize", 256)
                            put("attribution", "© OpenStreetMap")
                        }
                    }
                    putJsonArray("layers") {
                        addJsonObject {
                            put("id", "osm-layer")
                            put("type", "raster")
                            put("source", "osm-tiles")
                            put("minzoom", 0)
                            put("maxzoom", 19)
                        }
                    }
                },
            ) {
                val markerIcon = painterResource(org.maplibre.android.R.drawable.maplibre_marker_icon_default)

                val source = rememberGeoJsonSource(
                    data = GeoJsonData.Features(
                        FeatureCollection(
                            listOf(
                                Feature(
                                    geometry = Point(
                                        Position(
                                            latitude = station.yWgs84,
                                            longitude = station.xWgs84,
                                        ),
                                    ),
                                    properties = buildJsonObject {},
                                ),
                            ),
                        ),
                    ),
                )

                SymbolLayer(
                    id = "station-marker",
                    source = source,
                    iconImage = image(markerIcon),
                    iconAllowOverlap = const(true),
                )
            }
        }
    }
}

@Composable
fun DescriptionCard(station: Station, modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier.padding(16.dp)) {
        val (
            libelle,
            departementLabel,
            departement,
            communeLabel,
            commune,
            latitudeLabel,
            latitude,
            longitudeLabel,
            longitude,
        ) = createRefs()
        Text(
            station.libelle,
            style = Typography.headlineMedium,
            modifier =
            Modifier.constrainAs(libelle) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
        )
        Text(
            stringResource(R.string.departement_label),
            modifier =
            Modifier.constrainAs(departementLabel) {
                top.linkTo(libelle.bottom, margin = 16.dp)
                start.linkTo(parent.start)
            },
        )
        Text(
            station.departemen,
            modifier =
            Modifier.constrainAs(departement) {
                top.linkTo(departementLabel.top)
                bottom.linkTo(departementLabel.bottom)
                start.linkTo(departementLabel.end, margin = 16.dp)
            },
        )
        Text(
            stringResource(R.string.commune_label),
            modifier =
            Modifier.constrainAs(communeLabel) {
                top.linkTo(departementLabel.bottom, margin = 16.dp)
                start.linkTo(parent.start)
            },
        )
        Text(
            station.commune,
            modifier =
            Modifier.constrainAs(commune) {
                top.linkTo(communeLabel.top)
                bottom.linkTo(communeLabel.bottom)
                start.linkTo(departement.start)
            },
        )
        Text(
            stringResource(R.string.latitude_label),
            modifier =
            Modifier.constrainAs(latitudeLabel) {
                top.linkTo(communeLabel.bottom, margin = 16.dp)
                start.linkTo(parent.start)
            },
        )
        Text(
            station.yWgs84.toString(),
            modifier =
            Modifier.constrainAs(latitude) {
                top.linkTo(latitudeLabel.top)
                bottom.linkTo(latitudeLabel.bottom)
                start.linkTo(departement.start)
            },
        )
        Text(
            stringResource(R.string.longitude_label),
            modifier =
            Modifier.constrainAs(longitudeLabel) {
                top.linkTo(latitudeLabel.bottom, margin = 16.dp)
                start.linkTo(parent.start)
            },
        )
        Text(
            station.xWgs84.toString(),
            modifier =
            Modifier.constrainAs(longitude) {
                top.linkTo(longitudeLabel.top)
                bottom.linkTo(longitudeLabel.bottom)
                start.linkTo(departement.start)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DescriptionCardPreview() {
    TrainStationAppTheme {
        DescriptionCard(
            station =
            Station(
                libelle = "libelle",
                departemen = "departement",
                commune = "commune",
                yWgs84 = 0.0,
                xWgs84 = 0.0,
            ),
        )
    }
}
