package com.example.trainstationapp.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trainstationapp.R
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.presentation.ui.theme.TrainStationAppTheme
import com.example.trainstationapp.presentation.ui.theme.Typography

@Composable
fun StationItem(
    modifier: Modifier = Modifier,
    station: Station = Station(),
    onFavorite: (Station) -> Unit = {},
    onClick: (Station) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().clickable { onClick(station) }.padding(16.dp),
    ) {
        Column(modifier = Modifier.weight(2f)) {
            Text(station.libelle, style = Typography.headlineMedium)
            Text(station.id, modifier = Modifier.padding(top = 16.dp))
        }
        IconButton(onClick = { onFavorite(station) }) {
            if (station.isFavorite) {
                Icon(
                    painterResource(R.drawable.ic_baseline_star_24),
                    contentDescription = stringResource(R.string.favorite_icon_description),
                )
            } else {
                Icon(
                    painterResource(R.drawable.ic_baseline_star_border_24),
                    contentDescription = stringResource(R.string.favorite_icon_description),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StationItemPreview() {
    TrainStationAppTheme { StationItem(station = Station(id = "id", libelle = "libelle")) }
}
