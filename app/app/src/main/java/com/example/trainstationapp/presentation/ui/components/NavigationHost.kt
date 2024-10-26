package com.example.trainstationapp.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trainstationapp.presentation.viewmodels.DetailViewModel
import com.example.trainstationapp.presentation.viewmodels.DetailViewModel.Companion.provideFactory
import com.example.trainstationapp.presentation.viewmodels.LoginViewModel
import com.example.trainstationapp.presentation.viewmodels.StationListViewModel

sealed class Route(val route: String) {
    object Login : Route("login")

    object Stations : Route("stations")

    class Detail(id: String) : Route("stations/${id}")
}

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    detailViewModelFactory: DetailViewModel.AssistedFactory,
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login.route,
        modifier = modifier,
    ) {
        composable(Route.Login.route) {
            LoginScreen(navController = navController, viewModel = hiltViewModel<LoginViewModel>())
        }
        composable(Route.Stations.route) {
            var state by remember { mutableStateOf(0) }
            val titles = listOf("Stations", "About")
            Column {
                TabRow(selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = state == index,
                            onClick = { state = index },
                        )
                    }
                }
                when (state) {
                    0 ->
                        StationListScreen(
                            navController = navController,
                            viewModel = hiltViewModel<StationListViewModel>(),
                        )
                    1 -> AboutScreen()
                }
            }
        }
        composable(
            Route.Detail("{id}").route,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) {
            val stationId = it.arguments?.getString("id")!!
            DetailScreen(
                viewModel = viewModel(factory = detailViewModelFactory.provideFactory(stationId))
            )
        }
    }
}
