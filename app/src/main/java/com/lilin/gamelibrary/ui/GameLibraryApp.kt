package com.lilin.gamelibrary.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lilin.gamelibrary.feature.detail.GameDetailScreen
import com.lilin.gamelibrary.feature.discovery.DiscoveryScreen
import kotlinx.serialization.Serializable

@Serializable
object DiscoveryScreen

@Serializable
data class DetailScreen(val gameId: Int)

@Composable
fun GameLibraryApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DiscoveryScreen
    ) {
        composable<DiscoveryScreen> {
            DiscoveryScreen(
                onNavigateToDetail = { gameId ->
                    navController.navigate(DetailScreen(gameId))
                }
            )
        }
        
        composable<DetailScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<DetailScreen>()
            GameDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}