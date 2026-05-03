package com.example.trainstationapp.presentation.ui.components

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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

    class Detail(id: String) : Route("stations/$id")
}

@Composable
fun NavigationHost(
    detailViewModelFactory: DetailViewModel.AssistedFactory,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val loginViewModel = viewModel<LoginViewModel>(viewModelStoreOwner = LocalContext.current.findActivity())
    NavHost(
        navController = navController,
        startDestination = Route.Login.route,
        modifier = modifier,
    ) {
        composable(Route.Login.route) {
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
        composable(Route.Stations.route) { backStackEntry ->
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
                            navController = navController,
                            viewModel = hiltViewModel<StationListViewModel>(backStackEntry),
                        )

                    1 -> AboutScreen()
                }
            }
        }
        composable(
            Route.Detail("{id}").route,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { backStackEntry ->
            val stationId = backStackEntry.arguments?.getString("id")!!
            DetailScreen(
                viewModel = viewModel(
                    backStackEntry,
                    factory = detailViewModelFactory.provideFactory(stationId),
                ),
            )
        }
    }
}

fun Context.findActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    error("Not a context of activity")
}
