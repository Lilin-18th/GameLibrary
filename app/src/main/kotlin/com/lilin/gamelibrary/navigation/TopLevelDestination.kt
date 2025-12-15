package com.lilin.gamelibrary.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.feature.discovery.DiscoveryScreen
import com.lilin.gamelibrary.feature.favorite.FavoriteScreen
import com.lilin.gamelibrary.feature.search.SearchScreen

data class TopLevelDestination<T : Any>(
    val route: T,
    val icon: ImageVector,
    @param:StringRes val label: Int,
)

val TOP_LEVEL_ROUTES = listOf(
    TopLevelDestination(
        route = DiscoveryScreen,
        icon = Icons.Filled.Home,
        label = R.string.navigation_label_home,
    ),
    TopLevelDestination(
        route = SearchScreen,
        icon = Icons.Filled.Search,
        label = R.string.navigation_label_search,
    ),
    TopLevelDestination(
        route = FavoriteScreen,
        icon = Icons.Filled.LocalLibrary,
        label = R.string.navigation_label_favorite,
    ),
)
