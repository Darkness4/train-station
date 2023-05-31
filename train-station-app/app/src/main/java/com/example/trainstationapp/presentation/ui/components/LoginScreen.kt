package com.example.trainstationapp.presentation.ui.components

import android.content.Intent
import android.net.Uri
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.trainstationapp.R
import com.example.trainstationapp.data.github.GithubLogin
import com.example.trainstationapp.presentation.viewmodels.LoginViewModel
import java.util.UUID

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val context = LocalContext.current
    val isOnline by viewModel.isOnline.collectAsState()

    LaunchedEffect(isOnline) {
        if (isOnline) {
            // Move to stations
            navController.navigate(Route.Stations.route) {
                popUpTo(Route.Login.route) { inclusive = true }
            }
        }
    }

    val error by viewModel.error.collectAsState()
    LaunchedEffect(error) {
        if (!error.isNullOrEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    fun login() {
        val uri =
            Uri.parse(GithubLogin.BASE_URL)
                .buildUpon()
                .appendPath("login")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("client_id", context.getString(R.string.github_client_id))
                .appendQueryParameter("scope", "read:user,user:email")
                .appendQueryParameter("state", UUID.randomUUID().toString())
                .appendQueryParameter("redirect_uri", "https://train.mnguyen.fr/oauth2")
                .build()

        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.intent.apply { addCategory(Intent.CATEGORY_BROWSABLE) }
        customTabsIntent.launchUrl(context, uri)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ElevatedButton(onClick = { login() }) { Text(stringResource(R.string.sign_in_with_github)) }
    }
}
