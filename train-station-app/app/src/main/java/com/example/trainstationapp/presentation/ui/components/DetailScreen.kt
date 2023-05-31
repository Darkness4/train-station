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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.trainstationapp.R
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.presentation.ui.theme.TrainStationAppTheme
import com.example.trainstationapp.presentation.ui.theme.Typography
import com.example.trainstationapp.presentation.viewmodels.DetailViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private const val DEFAULT_ZOOM_LEVEL = 15.0f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val context = LocalContext.current
    val station by viewModel.station.collectAsState()
    val scaffoldState =
        rememberBottomSheetScaffoldState(
            bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded),
        )
    val cameraPositionState = rememberCameraPositionState {}

    LaunchedEffect(station) {
        station?.let {
            val position = LatLng(it.yWgs84, it.xWgs84)
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(
                    position,
                    DEFAULT_ZOOM_LEVEL,
                )
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

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        station?.let {
            val position = LatLng(it.yWgs84, it.xWgs84)
            Marker(
                state = MarkerState(position = position),
                title = it.libelle,
            )
        }
    }

    station?.let {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = { DescriptionCard(station = it) }
        ) {}
    }
}

@Composable
fun DescriptionCard(
    modifier: Modifier = Modifier,
    station: Station,
) {
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
                }
        )
        Text(
            stringResource(R.string.departement_label),
            modifier =
                Modifier.constrainAs(departementLabel) {
                    top.linkTo(libelle.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            station.departemen,
            modifier =
                Modifier.constrainAs(departement) {
                    top.linkTo(departementLabel.top)
                    bottom.linkTo(departementLabel.bottom)
                    start.linkTo(departementLabel.end, margin = 16.dp)
                }
        )
        Text(
            stringResource(R.string.commune_label),
            modifier =
                Modifier.constrainAs(communeLabel) {
                    top.linkTo(departementLabel.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            station.commune,
            modifier =
                Modifier.constrainAs(commune) {
                    top.linkTo(communeLabel.top)
                    bottom.linkTo(communeLabel.bottom)
                    start.linkTo(departement.start)
                }
        )
        Text(
            stringResource(R.string.latitude_label),
            modifier =
                Modifier.constrainAs(latitudeLabel) {
                    top.linkTo(communeLabel.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            station.yWgs84.toString(),
            modifier =
                Modifier.constrainAs(latitude) {
                    top.linkTo(latitudeLabel.top)
                    bottom.linkTo(latitudeLabel.bottom)
                    start.linkTo(departement.start)
                }
        )
        Text(
            stringResource(R.string.longitude_label),
            modifier =
                Modifier.constrainAs(longitudeLabel) {
                    top.linkTo(latitudeLabel.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            station.xWgs84.toString(),
            modifier =
                Modifier.constrainAs(longitude) {
                    top.linkTo(longitudeLabel.top)
                    bottom.linkTo(longitudeLabel.bottom)
                    start.linkTo(departement.start)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DescriptionCardPreview() {
    TrainStationAppTheme {
        DescriptionCard(
            station =
                Station(
                    libelle = "libelle",
                    departemen = "departement",
                    commune = "commune",
                    yWgs84 = 0.0,
                    xWgs84 = 0.0,
                )
        )
    }
}
