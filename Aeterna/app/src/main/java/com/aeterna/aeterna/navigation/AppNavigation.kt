package com.aeterna.aeterna.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aeterna.aeterna.ui.FullScreenPlayerScreen
import com.aeterna.aeterna.ui.HomeScreen
import com.aeterna.aeterna.ui.LibraryScreen
import com.aeterna.aeterna.ui.SearchScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Library : Screen("library")
    object FullScreenPlayer : Screen("full_screen_player")
}

@Composable
fun AppNavigation(
        navController: NavHostController = rememberNavController(),
        startDestination: String = Screen.Home.route,
        modifier: Modifier = Modifier
) {
    NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Search.route) { SearchScreen() }
        composable(Screen.Library.route) { LibraryScreen() }
        composable(Screen.FullScreenPlayer.route) {
            FullScreenPlayerScreen(navController = navController)
        }
    }
}
