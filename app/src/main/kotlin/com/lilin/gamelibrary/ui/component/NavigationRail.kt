package com.lilin.gamelibrary.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.lilin.gamelibrary.navigation.TOP_LEVEL_ROUTES
import com.lilin.gamelibrary.navigation.TopLevelDestination

@Composable
fun GameLibraryNavigationRail(
    topLevelRoute: List<TopLevelDestination<*>>,
    currentDestination: NavDestination?,
    onNavigateToRoute: (Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(
        modifier = modifier,
    ) {
        topLevelRoute.forEach { topLevelRoute ->
            val selected = currentDestination?.hierarchy?.any {
                it.hasRoute(topLevelRoute.route::class)
            } ?: false

            NavigationRailItem(
                selected = selected,
                onClick = { onNavigateToRoute(topLevelRoute.route) },
                icon = {
                    Icon(
                        imageVector = topLevelRoute.icon,
                        contentDescription = stringResource(topLevelRoute.label),
                    )
                },
                label = {
                    Text(text = stringResource(topLevelRoute.label))
                },
                alwaysShowLabel = true,
            )
        }
    }
}

@Preview
@Composable
private fun GameLibraryNavigationRailPreview() {
    GameLibraryNavigationRail(
        topLevelRoute = TOP_LEVEL_ROUTES,
        currentDestination = null,
        onNavigateToRoute = {},
    )
}
