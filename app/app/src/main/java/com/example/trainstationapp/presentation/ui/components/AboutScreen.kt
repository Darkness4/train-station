package com.example.trainstationapp.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trainstationapp.R
import com.example.trainstationapp.presentation.ui.theme.TrainStationAppTheme
import com.example.trainstationapp.presentation.ui.theme.Typography

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            stringResource(R.string.app_name),
            style = Typography.titleLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            stringResource(R.string.about),
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            stringResource(R.string.about_api_url, stringResource(R.string.api_url)),
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            buildAnnotatedString {
                append(stringResource(R.string.data_description_1))
                append("\n")
                append(stringResource(R.string.data_description_2))
            },
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            ElevatedButton(
                onClick = { uriHandler.openUri(context.getString(R.string.project_url)) }
            ) {
                Text(stringResource(R.string.source_code))
            }
            ElevatedButton(
                onClick = {
                    uriHandler.openUri(
                        "https://github.com/Darkness4/train-station/blob/main/protos/trainstationapis/trainstation/v1alpha1/station.proto"
                    )
                }
            ) {
                Text(stringResource(R.string.api_specs))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AboutScreenPreview() {
    TrainStationAppTheme { AboutScreen() }
}
