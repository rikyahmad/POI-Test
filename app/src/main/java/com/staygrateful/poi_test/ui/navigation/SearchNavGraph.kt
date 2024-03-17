package com.staygrateful.poi_test.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.staygrateful.poi_test.ui.presentation.home.view.composable.SearchDetailView
import com.staygrateful.poi_test.ui.presentation.home.view.composable.SearchView
import com.staygrateful.poi_test.ui.presentation.home.viewmodel.HomeViewModel

@Composable
fun SearchNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    onExpandChanged: (Boolean) -> Unit,
) {
    NavHost(navController = navController, startDestination = Screen.RootScreen.route) {

        composableSlide(
            route = Screen.RootScreen.route,
        ) {
            SearchView(
                navController = navController,
                viewModels = homeViewModel,
                onExpandChanged
            )
        }

        composableSlide(
            route = Screen.DetailSearchScreen.route,
        ) {
            SearchDetailView(
                navController = navController,
                viewModels = homeViewModel,
                onExpandChanged
            )
        }

        /*composableSlide(
            route = Screen.FormCreateCodeScreen.withStringKey("data"),
            arguments = listOf(
                Argument.named("data", NavType.IntType)
            )
        ) {
            FormCreateCodeScreen(
                navController,
                it
            )
        }

        composableSlide(
            route = Screen.AboutScreen.route,
        ) {
            AboutScreen(navController, it)
        }

        composableSlide(
            route = Screen.ComingSoonScreen.withStringKey("feature_message"),
            arguments = Argument.singleString("feature_message")
        ) {
            ComingSoonScreen(navController, it)
        }

        composableSlide(
            route = Screen.WebScreen.withStringKeys("url", "title"),
            arguments = listOf(
                Argument.named("url", NavType.StringType),
                Argument.named("title", NavType.StringType),
            )
        ) {
            WebScreen(navController, it)
        }*/
    }
}