package com.example.trainstationapp.presentation.ui.components

import android.content.Intent
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.example.trainstationapp.R
import com.example.trainstationapp.presentation.ui.navigation.Navigator
import com.example.trainstationapp.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val error by viewModel.error.collectAsState()
    LaunchedEffect(error) {
        if (!error.isNullOrEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    fun login() {
        val url = viewModel.provideAuthorizationUrl()
        println("auth url $url")
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.intent.apply { addCategory(Intent.CATEGORY_BROWSABLE) }
        customTabsIntent.launchUrl(context, url.toUri())
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ElevatedButton(onClick = { login() }) { Text(stringResource(R.string.sign_in_with_github)) }
    }
}
