package com.lilin.gamelibrary.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.navigation.TOP_LEVEL_ROUTES
import com.lilin.gamelibrary.navigation.TopLevelDestination

@Composable
fun GameLibraryNavigationDrawer(
    topLevelRoutes: List<TopLevelDestination<*>>,
    currentDestination: NavDestination?,
    onNavigateToRoute: (Any) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(
                modifier = Modifier.width(280.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(12.dp),
                ) {
                    Text(
                        text = stringResource(R.string.discover_screen_title),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(28.dp, 16.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    topLevelRoutes.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(destination.route::class)
                        } ?: false

                        NavigationDrawerItem(
                            label = {
                                Text(text = stringResource(destination.label))
                            },
                            selected = selected,
                            onClick = { onNavigateToRoute(destination.route) },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = stringResource(destination.label),
                                )
                            },
                        )
                    }
                }
            }
        },
        content = content,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun GameLibraryNavigationDrawerPreview() {
    GameLibraryNavigationDrawer(
        topLevelRoutes = TOP_LEVEL_ROUTES,
        currentDestination = null,
        onNavigateToRoute = {},
        content = {},
    )
}
