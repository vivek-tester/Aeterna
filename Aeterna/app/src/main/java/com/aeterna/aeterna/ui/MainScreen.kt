package com.aeterna.aeterna.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aeterna.aeterna.navigation.AppNavigation
import com.aeterna.aeterna.navigation.Screen

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(Screen.Home.route, Icons.Default.Home, "Home")
    object Search : BottomNavItem(Screen.Search.route, Icons.Default.Search, "Search")
    object Library : BottomNavItem(Screen.Library.route, Icons.Default.LibraryMusic, "Library")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
        navController: NavHostController = rememberNavController(),
        isAuthenticated: Boolean = false,
        onSignInClick: () -> Unit
) {
    val items = listOf(BottomNavItem.Home, BottomNavItem.Search, BottomNavItem.Library)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showMiniPlayer by remember {
        mutableStateOf(false)
    } // State to control MiniPlayer visibility

    Scaffold(
            bottomBar = {
                if (isAuthenticated) {
                    Column {
                        if (showMiniPlayer) {
                            MiniPlayer(
                                    navController = navController,
                                    modifier =
                                            Modifier.padding(
                                                    bottom = 0.dp
                                            ), // Adjust padding as needed
                                    onMiniPlayerClick = {
                                        navController.navigate(Screen.FullScreenPlayer.route)
                                    }
                            )
                        }
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = null) },
                                        label = { Text(screen.label) },
                                        selected =
                                                currentDestination?.hierarchy?.any {
                                                    it.route == screen.route
                                                } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
            }
    ) { innerPadding ->
        if (isAuthenticated) {
            AppNavigation(navController = navController, modifier = Modifier.padding(innerPadding))
            // For demonstration, show mini player after authentication
            // In a real app, this would be triggered by actual playback
            if (!showMiniPlayer) {
                // Simulate a song playing to show the mini player
                // This should be replaced by actual player state observation
                // For now, just set it to true after a short delay or on first song play
                // For now, just set it to true
                showMiniPlayer = true
            }
        } else {
            Column(
                    modifier = Modifier.padding(innerPadding).fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Please sign in to use Aeterna")
                Button(onClick = onSignInClick) { Text("Sign In with YouTube Music") }
            }
        }
    }
}
