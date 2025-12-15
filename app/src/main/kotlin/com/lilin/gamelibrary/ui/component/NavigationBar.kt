package com.lilin.gamelibrary.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.lilin.gamelibrary.navigation.TOP_LEVEL_ROUTES
import com.lilin.gamelibrary.navigation.TopLevelDestination

@Composable
fun GameLibraryNavigationBar(
    topLevelRoute: List<TopLevelDestination<*>>,
    currentDestination: NavDestination?,
    onNavigateToRoute: (Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        tonalElevation = 0.dp,
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        topLevelRoute.forEach { topLevelRoute ->
            val selected = currentDestination?.hierarchy?.any {
                it.hasRoute(topLevelRoute.route::class)
            } ?: false

            NavigationBarItem(
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
private fun GameLibraryNavigationBarPreview() {
    GameLibraryNavigationBar(
        topLevelRoute = TOP_LEVEL_ROUTES,
        currentDestination = null,
        onNavigateToRoute = {},
    )
}
