package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.example.trainstationapp.R
import com.example.trainstationapp.presentation.ui.components.AboutScreen
import com.example.trainstationapp.presentation.ui.components.DetailScreen
import com.example.trainstationapp.presentation.ui.components.LoginScreen
import com.example.trainstationapp.presentation.ui.components.StationListScreen
import com.example.trainstationapp.presentation.ui.navigation.Navigator
import com.example.trainstationapp.presentation.ui.navigation.rememberNavigationState
import com.example.trainstationapp.presentation.ui.navigation.toEntries
import com.example.trainstationapp.presentation.ui.theme.TrainStationAppTheme
import com.example.trainstationapp.presentation.viewmodels.DetailViewModel
import com.example.trainstationapp.presentation.viewmodels.DetailViewModel.Companion.provideFactory
import com.example.trainstationapp.presentation.viewmodels.LoginViewModel
import com.example.trainstationapp.presentation.viewmodels.StationListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
sealed class Route {
    @Serializable
    object Login : NavKey

    @Serializable
    object Stations : NavKey

    @Serializable
    data class Detail(val id: String) : NavKey
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var assisted: DetailViewModel.AssistedFactory

    private val loginViewModel: LoginViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle OAuth redirect
        intent.data?.getQueryParameter("code")?.let { code -> loginOAuth(code) }

        setContent {
            val navigationState = rememberNavigationState(
                startRoute = Route.Login,
                topLevelRoutes = setOf(Route.Login, Route.Stations),
            )

            val navigator = remember { Navigator(navigationState) }

            val canPop = navigationState.backStacks[navigationState.topLevelRoute]?.size!! > 1

            TrainStationAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        topBar = {
                            Surface(shadowElevation = 16.dp) {
                                TopAppBar(
                                    title = { Text(stringResource(R.string.title_activity_main)) },
                                    navigationIcon = {
                                        if (canPop) {
                                            IconButton(onClick = { navigator.goBack() }) {
                                                Icon(
                                                    imageVector =
                                                    Icons.AutoMirrored.Filled.ArrowBack,
                                                    contentDescription = "Back",
                                                )
                                            }
                                        }
                                    },
                                )
                            }
                        },
                    ) { contentPadding ->
                        val entryProvider = entryProvider {
                            entry<Route.Login> {
                                LoginScreen(navigator = navigator, viewModel = loginViewModel)
                            }
                            entry<Route.Stations> {
                                var state by remember { mutableIntStateOf(0) }
                                val titles = listOf("Stations", "About")
                                Column {
                                    PrimaryTabRow(
                                        state,
                                        tabs = {
                                            titles.forEachIndexed { index, title ->
                                                Tab(
                                                    text = { Text(title) },
                                                    selected = state == index,
                                                    onClick = { state = index },
                                                )
                                            }
                                        },
                                    )
                                    when (state) {
                                        0 ->
                                            StationListScreen(
                                                navigator = navigator,
                                                viewModel = hiltViewModel<StationListViewModel>(),
                                            )

                                        1 -> AboutScreen()
                                    }
                                }
                            }
                            entry<Route.Detail> { key ->
                                DetailScreen(
                                    viewModel = viewModel(
                                        factory = assisted.provideFactory(key.id),
                                    ),
                                )
                            }
                        }

                        NavDisplay(
                            entries = navigationState.toEntries(entryProvider),
                            onBack = { navigator.goBack() },
                            sceneStrategies = remember { listOf(DialogSceneStrategy()) },
                            modifier = Modifier.padding(contentPadding),
                        )
                    }
                }
            }
        }
    }

    private fun loginOAuth(code: String) {
        loginViewModel.code = code
    }
}
