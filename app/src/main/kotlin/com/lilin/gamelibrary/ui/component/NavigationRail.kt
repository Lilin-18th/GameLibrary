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
    topLevelRoutes: List<TopLevelDestination<*>>,
    currentDestination: NavDestination?,
    onNavigateToRoute: (Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(
        modifier = modifier,
    ) {
        topLevelRoutes.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any {
                it.hasRoute(destination.route::class)
            } ?: false

            NavigationRailItem(
                selected = selected,
                onClick = { onNavigateToRoute(destination.route) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.label),
                    )
                },
                label = {
                    Text(text = stringResource(destination.label))
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
        topLevelRoutes = TOP_LEVEL_ROUTES,
        currentDestination = null,
        onNavigateToRoute = {},
    )
}
