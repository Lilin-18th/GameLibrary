package com.lilin.gamelibrary.feature.discovery

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.navigation.TOP_LEVEL_ROUTES
import com.lilin.gamelibrary.ui.component.DiscoveryTopBar
import com.lilin.gamelibrary.ui.component.GameLibraryNavigationBar
import com.lilin.gamelibrary.ui.component.discovery.HighRatedGamesSection
import com.lilin.gamelibrary.ui.component.discovery.NewReleaseGamesSection
import com.lilin.gamelibrary.ui.component.discovery.TrendingGamesSection
import kotlinx.serialization.Serializable

@Serializable
object DiscoveryScreen

fun NavGraphBuilder.navigateDiscoveryScreen(
    onNavigateToDetail: (Int) -> Unit,
) {
    composable<DiscoveryScreen> {
        DiscoveryScreen(
            onNavigateToDetail = { gameId ->
                onNavigateToDetail(gameId)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DiscoveryViewModel = hiltViewModel(),
) {
    val trendingState by viewModel.trendingState.collectAsState()
    val highlyRatedState by viewModel.highlyRatedState.collectAsState()
    val newReleasesState by viewModel.newReleasesState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            DiscoveryTopBar(scrollBehavior = scrollBehavior)
        },
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = modifier,
    ) { paddingValues ->
        DiscoveryScreen(
            trendingState = trendingState,
            highlyRatedState = highlyRatedState,
            newReleasesState = newReleasesState,
            scrollBehavior = scrollBehavior,
            onNavigateToDetail = onNavigateToDetail,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoveryScreen(
    trendingState: DiscoveryUiState,
    highlyRatedState: DiscoveryUiState,
    newReleasesState: DiscoveryUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(bottom = 12.dp),
    ) {
        item {
            TrendingGames(
                uiState = trendingState,
                onNavigateToDetail = onNavigateToDetail,
                modifier = Modifier,
            )
        }

        item {
            HighRatedGames(
                uiState = highlyRatedState,
                onNavigateToDetail = onNavigateToDetail,
                modifier = Modifier,
            )
        }

        item {
            NewReleaseGames(
                uiState = newReleasesState,
                onNavigateToDetail = onNavigateToDetail,
                modifier = Modifier,
            )
        }
    }
}

@Composable
private fun TrendingGames(
    uiState: DiscoveryUiState,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is DiscoveryUiState.Success -> {
            TrendingGamesSection(
                games = uiState.data,
                onGameClick = { game ->
                    onNavigateToDetail(game.id)
                },
                modifier = modifier,
            )
        }

        is DiscoveryUiState.Loading -> {}
        is DiscoveryUiState.Error -> {}
        is DiscoveryUiState.ReLoading -> {}
        is DiscoveryUiState.ReLoadingError -> {}
    }
}

@Composable
private fun HighRatedGames(
    uiState: DiscoveryUiState,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is DiscoveryUiState.Success -> {
            HighRatedGamesSection(
                games = uiState.data,
                onGameClick = { game ->
                    onNavigateToDetail(game.id)
                },
                modifier = modifier,
            )
        }

        is DiscoveryUiState.Loading -> {}
        is DiscoveryUiState.Error -> {}
        is DiscoveryUiState.ReLoading -> {}
        is DiscoveryUiState.ReLoadingError -> {}
    }
}

@Composable
private fun NewReleaseGames(
    uiState: DiscoveryUiState,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is DiscoveryUiState.Success -> {
            NewReleaseGamesSection(
                games = uiState.data,
                onGameClick = { game ->
                    onNavigateToDetail(game.id)
                },
                modifier = modifier,
            )
        }

        is DiscoveryUiState.Loading -> {}
        is DiscoveryUiState.Error -> {}
        is DiscoveryUiState.ReLoading -> {}
        is DiscoveryUiState.ReLoadingError -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@VisibleForTesting
@Composable
internal fun DiscoveryScreenSample(
    trendingState: DiscoveryUiState,
    highlyRatedState: DiscoveryUiState,
    newReleasesState: DiscoveryUiState,
    modifier: Modifier = Modifier,
) {
    DiscoveryScreen(
        trendingState = trendingState,
        highlyRatedState = highlyRatedState,
        newReleasesState = newReleasesState,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        onNavigateToDetail = {},
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun DiscoveryScreenPreview() {
    val games = listOf(
        Game(
            id = 1,
            name = "The Witcher 3: Wild Hunt",
            imageUrl = "https://example.com/image.jpg",
            releaseDate = "2015-05-19",
            rating = 4.5,
            ratingsCount = 15234,
            metacritic = 92,
            isTba = false,
            addedCount = 50000,
            platforms = listOf("PC", "PS4", "Xbox One"),
        ),
        Game(
            id = 2,
            name = "Super Mario Party",
            imageUrl = null,
            releaseDate = "2018-10-05",
            rating = 3.8,
            ratingsCount = 244,
            metacritic = 76,
            isTba = false,
            addedCount = 30000,
            platforms = listOf("Switch"),
        ),
        Game(
            id = 3,
            name = "Cyberpunk 2077",
            imageUrl = null,
            releaseDate = "2020-12-10",
            rating = 4.2,
            ratingsCount = 8765,
            metacritic = 60,
            isTba = false,
            addedCount = 30000,
            platforms = listOf("PC", "PS5", "Xbox Series X"),
        ),
    )

    Scaffold(
        topBar = {
            DiscoveryTopBar(scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior())
        },
        bottomBar = {
            GameLibraryNavigationBar(
                topLevelRoute = TOP_LEVEL_ROUTES,
                currentDestination = null,
                onNavigateToRoute = {},
            )
        },
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = Modifier,
    ) { paddingValues ->
        DiscoveryScreenSample(
            trendingState = DiscoveryUiState.Success(data = games),
            highlyRatedState = DiscoveryUiState.Success(data = games),
            newReleasesState = DiscoveryUiState.Success(data = games),
            modifier = Modifier.padding(paddingValues),
        )
    }
}
