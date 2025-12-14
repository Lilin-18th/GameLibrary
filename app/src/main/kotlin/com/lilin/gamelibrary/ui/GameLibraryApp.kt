package com.lilin.gamelibrary.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lilin.gamelibrary.feature.discovery.DiscoveryScreen
import com.lilin.gamelibrary.feature.discovery.navigateDiscoveryScreen
import com.lilin.gamelibrary.navigation.TOP_LEVEL_ROUTES
import com.lilin.gamelibrary.ui.component.GameLibraryNavigationBar

@Composable
fun GameLibraryApp(
    modifier: Modifier = Modifier,
) {
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
                },
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier,
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
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
        modifier = modifier,
    ) {
        navigateDiscoveryScreen(
            onNavigateToDetail = {
                // TODO: DetailScreen
            },
        )
    }
}
