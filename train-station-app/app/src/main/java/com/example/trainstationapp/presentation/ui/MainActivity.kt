package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.trainstationapp.R
import com.example.trainstationapp.data.datastore.Session
import com.example.trainstationapp.data.datastore.oAuth
import com.example.trainstationapp.data.github.GithubApi
import com.example.trainstationapp.data.github.GithubLogin
import com.example.trainstationapp.presentation.ui.components.NavigationHost
import com.example.trainstationapp.presentation.ui.theme.TrainStationAppTheme
import com.example.trainstationapp.presentation.viewmodels.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var assisted: DetailViewModel.AssistedFactory
    @Inject lateinit var githubLogin: GithubLogin
    @Inject lateinit var oauthDataStore: DataStore<Session.OAuth>
    @Inject lateinit var githubApi: GithubApi

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle OAuth redirect
        intent.data?.getQueryParameter("code")?.let { code -> loginOAuth(code) }

        setContent {
            val navController = rememberNavController()

            var canPop by remember { mutableStateOf(false) }

            DisposableEffect(navController) {
                val listener =
                    NavController.OnDestinationChangedListener { controller, _, _ ->
                        canPop = controller.previousBackStackEntry != null
                    }
                navController.addOnDestinationChangedListener(listener)
                onDispose { navController.removeOnDestinationChangedListener(listener) }
            }

            TrainStationAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            Surface(
                                shadowElevation = 16.dp,
                            ) {
                                TopAppBar(
                                    title = { Text(stringResource(R.string.title_activity_main)) },
                                    navigationIcon = {
                                        if (canPop) {
                                            IconButton(onClick = { navController.popBackStack() }) {
                                                Icon(
                                                    imageVector = Icons.Filled.ArrowBack,
                                                    contentDescription = "Back"
                                                )
                                            }
                                        }
                                    },
                                )
                            }
                        }
                    ) { contentPadding ->
                        NavigationHost(
                            navController = navController,
                            detailViewModelFactory = assisted,
                            modifier = Modifier.padding(contentPadding)
                        )
                    }
                }
            }
        }
    }

    private fun loginOAuth(code: String) {
        lifecycleScope.launch {
            val token =
                githubLogin.accessToken(
                    getString(R.string.github_client_id),
                    getString(R.string.github_client_secret),
                    code,
                )

            // Test oauth
            val user = githubApi.user("${token.tokenType} ${token.accessToken}")
            println(user)

            // Store oauth
            oauthDataStore.updateData {
                oAuth {
                    accessToken = token.accessToken
                    tokenType = token.tokenType
                    userId = user.id
                }
            }
        }
    }
}
