package com.lilin.gamelibrary.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.lilin.gamelibrary.feature.discovery.DiscoveryScreen
import com.lilin.gamelibrary.feature.search.SearchScreen

data class TopLevelDestination<T : Any>(
    val route: T,
    val icon: ImageVector,
    val label: String,
)

val TOP_LEVEL_ROUTES = listOf(
    TopLevelDestination(
        route = DiscoveryScreen,
        icon = Icons.Filled.Home,
        label = "Discovery",
    ),
    TopLevelDestination(
        route = SearchScreen,
        icon = Icons.Filled.Search,
        label = "Search",
    ),
)
