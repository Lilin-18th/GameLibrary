package com.lilin.gamelibrary.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lilin.gamelibrary.feature.detail.GameDetailScreen
import com.lilin.gamelibrary.feature.discovery.DiscoveryScreen
import com.lilin.gamelibrary.feature.search.navigateSearchScreen
import com.lilin.gamelibrary.navigation.TOP_LEVEL_ROUTES
import com.lilin.gamelibrary.ui.component.GameLibraryNavigationBar
import kotlinx.serialization.Serializable

@Serializable
object DiscoveryScreen

@Serializable
data class DetailScreen(val gameId: Int)

@Composable
fun GameLibraryApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            GameLibraryNavigationBar(
                topLevelRoute = TOP_LEVEL_ROUTES,
                currentDestination = currentDestination,
                onNavigateToRoute = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
    ) { padding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = DiscoveryScreen,
    ) {
        composable<DiscoveryScreen> {
            DiscoveryScreen(
                onNavigateToDetail = { gameId ->
                    navController.navigate(DetailScreen(gameId))
                },
            )
        }

        composable<DetailScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<DetailScreen>()
            GameDetailScreen(
                onBackClick = { navController.popBackStack() },
            )
        }

        navigateSearchScreen()
    }
}
