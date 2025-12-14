package com.lilin.gamelibrary.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.feature.discovery.DiscoveryScreen

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
)
