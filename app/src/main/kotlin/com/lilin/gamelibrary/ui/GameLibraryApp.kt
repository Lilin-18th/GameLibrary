package com.lilin.gamelibrary.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.feature.detail.GameDetailScreen
import com.lilin.gamelibrary.feature.detail.navigateDetailScreen
import com.lilin.gamelibrary.feature.discovery.DiscoveryScreen
import com.lilin.gamelibrary.feature.discovery.navigateDiscoveryScreen
import com.lilin.gamelibrary.feature.favorite.FavoriteScreen
import com.lilin.gamelibrary.feature.favorite.navigateFavoriteScreen
import com.lilin.gamelibrary.feature.search.SearchScreen
import com.lilin.gamelibrary.feature.search.navigateSearchScreen
import com.lilin.gamelibrary.feature.sectiondetail.SectionDetailScreen
import com.lilin.gamelibrary.feature.sectiondetail.navigateSectionDetailScreen
import com.lilin.gamelibrary.navigation.BottomNavGraph
import com.lilin.gamelibrary.navigation.TOP_LEVEL_ROUTES
import com.lilin.gamelibrary.ui.component.GameLibraryNavigationBar
import com.lilin.gamelibrary.ui.component.GameLibraryNavigationDrawer
import com.lilin.gamelibrary.ui.component.GameLibraryNavigationRail
import com.lilin.gamelibrary.ui.component.adaptive.AdaptiveLayout

private const val DURATION_MILLIS = 500

@Composable
fun GameLibraryApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val isTopLevelDestination = currentDestination?.route?.let { route ->
        TOP_LEVEL_ROUTES.any { it.route::class.qualifiedName == route.substringBefore("/") }
    } ?: true

    AdaptiveLayout(
        windowWidthSizeClass = windowSize,
        compactContent = {
            CompactApp(
                navController = navController,
                currentDestination = currentDestination,
                isTopLevelDestination = isTopLevelDestination,
                modifier = modifier,
            )
        },
        mediumContent = {
            MediumApp(
                navController = navController,
                currentDestination = currentDestination,
                modifier = modifier,
            )
        },
        expandedContent = {
            ExpandedApp(
                navController = navController,
                currentDestination = currentDestination,
                modifier = modifier,
            )
        },
    )
}

@Composable
private fun CompactApp(
    navController: NavHostController,
    currentDestination: NavDestination?,
    isTopLevelDestination: Boolean,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        bottomBar = {
            if (isTopLevelDestination) {
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
            }
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
private fun MediumApp(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    var isRailExpanded by rememberSaveable { mutableStateOf(true) }

    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isRailExpanded,
            enter = expandHorizontally() + fadeIn(),
            exit = shrinkHorizontally() + fadeOut(),
        ) {
            GameLibraryNavigationRail(
                topLevelRoutes = TOP_LEVEL_ROUTES,
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
        }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { isRailExpanded = !isRailExpanded },
                    modifier = Modifier.padding(16.dp),
                ) {
                    Icon(
                        imageVector = if (isRailExpanded) {
                            Icons.Default.ChevronLeft
                        } else {
                            Icons.Default.Menu
                        },
                        contentDescription = if (isRailExpanded) {
                            stringResource(R.string.navigation_rail_hide)
                        } else {
                            stringResource(R.string.navigation_rail_show)
                        },
                    )
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { paddingValues ->
            AppNavHost(
                navController = navController,
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
            )
        }
    }
}

@Composable
private fun ExpandedApp(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    GameLibraryNavigationDrawer(
        topLevelRoutes = TOP_LEVEL_ROUTES,
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
        modifier = modifier,
    ) {
        AppNavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
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
        startDestination = BottomNavGraph,
        modifier = modifier,
        enterTransition = { fadeIn(tween(DURATION_MILLIS)) },
        exitTransition = { fadeOut(tween(DURATION_MILLIS)) },
        popEnterTransition = { fadeIn(tween(DURATION_MILLIS)) },
        popExitTransition = { fadeOut(tween(DURATION_MILLIS)) },
    ) {
        bottomNavGraph(navController)

        navigateDetailScreen(onBackClick = navController::popBackStack)

        navigateSectionDetailScreen(
            onBackClick = navController::popBackStack,
            onNavigateToDetail = { gameId ->
                navController.navigate(GameDetailScreen(gameId))
            },
        )
    }
}

private fun NavGraphBuilder.bottomNavGraph(navController: NavHostController) {
    navigation<BottomNavGraph>(
        startDestination = DiscoveryScreen,
        enterTransition = {
            if (involvesDetailScreen(initialState, targetState)) {
                fadeIn(tween(DURATION_MILLIS))
            } else {
                slideIntoContainer(
                    towards = getSlideDirection(initialState, targetState),
                    animationSpec = tween(DURATION_MILLIS),
                )
            }
        },
        exitTransition = {
            if (involvesDetailScreen(initialState, targetState)) {
                fadeOut(tween(DURATION_MILLIS))
            } else {
                slideOutOfContainer(
                    towards = getSlideDirection(initialState, targetState),
                    animationSpec = tween(DURATION_MILLIS),
                ) + ExitTransition.KeepUntilTransitionsFinished
            }
        },
        popEnterTransition = {
            if (involvesDetailScreen(initialState, targetState)) {
                fadeIn(tween(DURATION_MILLIS))
            } else {
                slideIntoContainer(
                    towards = getSlideDirection(initialState, targetState),
                    animationSpec = tween(DURATION_MILLIS),
                )
            }
        },
        popExitTransition = {
            if (involvesDetailScreen(initialState, targetState)) {
                fadeOut(tween(DURATION_MILLIS))
            } else {
                slideOutOfContainer(
                    towards = getSlideDirection(initialState, targetState),
                    animationSpec = tween(DURATION_MILLIS),
                ) + ExitTransition.KeepUntilTransitionsFinished
            }
        },
    ) {
        navigateDiscoveryScreen(
            onNavigateToDetail = { gameId ->
                navController.navigate(GameDetailScreen(gameId))
            },
            onNavigateToSectionDetail = { sectionType ->
                navController.navigate(SectionDetailScreen(sectionTypeName = sectionType.name))
            },
        )

        navigateSearchScreen(
            navigateToDetail = { gameId ->
                navController.navigate(GameDetailScreen(gameId))
            },
        )

        navigateFavoriteScreen(
            navigateToDetail = { gameId ->
                navController.navigate(GameDetailScreen(gameId))
            },
        )
    }
}

/**
 * 画面遷移に詳細画面が含まれているか確認
 * @param initialState 現在の状態
 * @param targetState 新しい状態
 * @return 詳細画面が含まれている場合はtrue
 */
private fun involvesDetailScreen(
    initialState: NavBackStackEntry,
    targetState: NavBackStackEntry,
): Boolean {
    return initialState.isDetailScreen() || targetState.isDetailScreen()
}

/**
 * top level navigationの遷移方向を取得
 * @param from 現在の状態
 * @param to 新しい状態
 * @return スライド方向
 */
private fun getSlideDirection(
    from: NavBackStackEntry,
    to: NavBackStackEntry,
): AnimatedContentTransitionScope.SlideDirection {
    val fromIndex = getScreenIndex(from)
    val toIndex = getScreenIndex(to)

    return if (toIndex > fromIndex) {
        AnimatedContentTransitionScope.SlideDirection.Start
    } else {
        AnimatedContentTransitionScope.SlideDirection.End
    }
}

private fun NavBackStackEntry.isDetailScreen(): Boolean {
    return destination.hasRoute<GameDetailScreen>() ||
        destination.hasRoute<SectionDetailScreen>()
}

private fun getScreenIndex(entry: NavBackStackEntry): Int {
    return when {
        entry.destination.hasRoute<DiscoveryScreen>() -> 0
        entry.destination.hasRoute<SearchScreen>() -> 1
        entry.destination.hasRoute<FavoriteScreen>() -> 2
        else -> 0
    }
}
