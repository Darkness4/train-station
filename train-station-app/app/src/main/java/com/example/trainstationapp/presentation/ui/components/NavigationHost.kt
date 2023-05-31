package com.example.trainstationapp.presentation.ui.components

import androidx.compose.runtime.Composable
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

    object About : Route("about")

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
            StationListScreen(
                navController = navController,
                viewModel = hiltViewModel<StationListViewModel>()
            )
        }
        composable(Route.About.route) { AboutScreen() }
        composable(
            Route.Detail("{id}").route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val stationId = it.arguments?.getString("id")!!
            DetailScreen(
                viewModel = viewModel(factory = detailViewModelFactory.provideFactory(stationId))
            )
        }
    }
}
