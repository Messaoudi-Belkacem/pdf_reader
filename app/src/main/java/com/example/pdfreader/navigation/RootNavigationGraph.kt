package com.example.pdfreader.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pdfreader.SharedViewModel
import com.example.pdfreader.ui.screen.MainScreen
import com.example.pdfreader.ui.screen.PdfPage

@Composable
fun RootNavigationGraph(
    sharedViewModel: SharedViewModel,
    onButtonClick: () -> Unit
) {
    val navHostController = rememberNavController()
    val time = 500
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Screen.Main.route,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(time),
                initialOffsetX = { fullWidth -> -fullWidth }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(time),
                targetOffsetX = { fullWidth -> fullWidth }
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                animationSpec = tween(time),
                initialOffsetX = {fullWidth -> -fullWidth }
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(time),
                targetOffsetX = {fullWidth -> fullWidth }
            )
        }
    ) {
        composable(route = Screen.Main.route) {
            MainScreen(
                navHostController = navHostController,
                sharedViewModel = sharedViewModel,
                onButtonClick = onButtonClick
            )
        }
        composable(route = Screen.Reader.route) {
            PdfPage(
                sharedViewModel = sharedViewModel,
            )
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
}