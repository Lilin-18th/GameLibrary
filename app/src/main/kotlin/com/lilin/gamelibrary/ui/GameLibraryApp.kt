package com.lilin.gamelibrary.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
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

private const val DURATION_MILLIS = 500

@Composable
fun GameLibraryApp(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val isTopLevelDestination = currentDestination?.route?.let { route ->
        TOP_LEVEL_ROUTES.any { it.route::class.qualifiedName == route.substringBefore("/") }
    } ?: true

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
