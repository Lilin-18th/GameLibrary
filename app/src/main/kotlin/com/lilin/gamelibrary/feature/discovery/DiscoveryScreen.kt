package com.lilin.gamelibrary.feature.discovery

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.navigation.TOP_LEVEL_ROUTES
import com.lilin.gamelibrary.ui.component.DiscoveryTopBar
import com.lilin.gamelibrary.ui.component.GameLibraryNavigationBar
import com.lilin.gamelibrary.ui.component.discovery.ErrorSection
import com.lilin.gamelibrary.ui.component.discovery.HighRatedGamesSection
import com.lilin.gamelibrary.ui.component.discovery.LoadingGamesSection
import com.lilin.gamelibrary.ui.component.discovery.NewReleaseGamesSection
import com.lilin.gamelibrary.ui.component.discovery.SectionType
import com.lilin.gamelibrary.ui.component.discovery.TrendingGameCardLoading
import com.lilin.gamelibrary.ui.component.discovery.TrendingGamesSection
import com.lilin.gamelibrary.ui.theme.HighRatedGradientStart
import com.lilin.gamelibrary.ui.theme.NewReleaseGradientStart
import com.lilin.gamelibrary.ui.theme.TrendingGradientStart
import kotlinx.coroutines.flow.combine
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

    val isInitialLoading by remember {
        combine(
            viewModel.trendingState,
            viewModel.highlyRatedState,
            viewModel.newReleasesState,
        ) { trending, highlyRated, newReleases ->
            trending is DiscoveryUiState.InitialLoading ||
                highlyRated is DiscoveryUiState.InitialLoading ||
                newReleases is DiscoveryUiState.InitialLoading
        }
    }.collectAsState(initial = true)

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
            isInitialLoading = isInitialLoading,
            scrollBehavior = scrollBehavior,
            onNavigateToDetail = onNavigateToDetail,
            loadingAllSection = viewModel::loadAllSections,
            onReloadTrendSection = viewModel::reloadTrendingGames,
            onReloadHighRatedSection = viewModel::reloadHighlyRatedGames,
            onReloadNewReleaseSection = viewModel::reloadNewReleaseGame,
            onRetryTrendSection = viewModel::retryTrendingGames,
            onRetryHighRatedSection = viewModel::retryHighlyRatedGames,
            onRetryNewReleaseSection = viewModel::retryNewReleases,
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
    isInitialLoading: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigateToDetail: (Int) -> Unit,
    loadingAllSection: () -> Unit,
    onReloadTrendSection: () -> Unit,
    onReloadHighRatedSection: () -> Unit,
    onReloadNewReleaseSection: () -> Unit,
    onRetryTrendSection: () -> Unit,
    onRetryHighRatedSection: () -> Unit,
    onRetryNewReleaseSection: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isAllError =
        trendingState is DiscoveryUiState.Error &&
            highlyRatedState is DiscoveryUiState.Error &&
            newReleasesState is DiscoveryUiState.Error

    when {
        isInitialLoading -> {
            InitialLoadingScreen(
                modifier = modifier,
            )
        }

        isAllError -> {
            FullScreenError(
                onRetry = loadingAllSection,
                modifier = modifier,
            )
        }

        else -> {
            LazyColumn(
                modifier = modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(bottom = 12.dp),
            ) {
                item {
                    TrendingGames(
                        uiState = trendingState,
                        onNavigateToDetail = onNavigateToDetail,
                        onRetry = onRetryTrendSection,
                        onReload = onReloadTrendSection,
                        modifier = Modifier,
                    )
                }

                item {
                    HighRatedGames(
                        uiState = highlyRatedState,
                        onNavigateToDetail = onNavigateToDetail,
                        onRetry = onRetryHighRatedSection,
                        onReload = onReloadHighRatedSection,
                        modifier = Modifier,
                    )
                }

                item {
                    NewReleaseGames(
                        uiState = newReleasesState,
                        onNavigateToDetail = onNavigateToDetail,
                        onRetry = onRetryNewReleaseSection,
                        onReload = onReloadNewReleaseSection,
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}

@Composable
private fun InitialLoadingScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CircularProgressIndicator()
            Text(
                text = stringResource(R.string.discovery_loading_message),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun FullScreenError(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error,
            )

            Text(
                text = stringResource(R.string.discovery_error_message),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = stringResource(R.string.discovery_error_submessage),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onRetry) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.common_retry_button_text))
            }
        }
    }
}

@Composable
private fun TrendingGames(
    uiState: DiscoveryUiState,
    onNavigateToDetail: (Int) -> Unit,
    onReload: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is DiscoveryUiState.Success -> {
            TrendingGamesSection(
                isSuccessState = true,
                games = uiState.data,
                onGameClick = { game ->
                    onNavigateToDetail(game.id)
                },
                onReload = onReload,
                modifier = modifier,
            )
        }

        is DiscoveryUiState.Loading -> {
            LoadingGamesSection(
                sectionType = SectionType.TRENDING,
                modifier = modifier,
                content = { TrendingGameCardLoading() },
            )
        }

        is DiscoveryUiState.ReLoading -> {
            LoadingGamesSection(
                sectionType = SectionType.TRENDING,
                modifier = modifier,
                content = { TrendingGameCardLoading() },
            )
        }

        is DiscoveryUiState.Error -> {
            ErrorSection(
                sectionType = SectionType.TRENDING,
                sectionColor = TrendingGradientStart,
                sectionIcon = Icons.Filled.Whatshot,
                throwable = uiState.throwable,
                onRetry = onRetry,
                modifier = modifier,
            )
        }

        is DiscoveryUiState.ReLoadingError -> {
            ErrorSection(
                sectionType = SectionType.TRENDING,
                sectionColor = TrendingGradientStart,
                sectionIcon = Icons.Filled.Whatshot,
                throwable = uiState.throwable,
                onRetry = onRetry,
                modifier = modifier,
            )
        }
        is DiscoveryUiState.InitialLoading -> {
            // no-op
        }
    }
}

@Composable
private fun HighRatedGames(
    uiState: DiscoveryUiState,
    onNavigateToDetail: (Int) -> Unit,
    onReload: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is DiscoveryUiState.Success -> {
            HighRatedGamesSection(
                isSuccessState = true,
                games = uiState.data,
                onGameClick = { game ->
                    onNavigateToDetail(game.id)
                },
                onReload = onReload,
                modifier = modifier,
            )
        }

        is DiscoveryUiState.Loading -> {
            LoadingGamesSection(
                sectionType = SectionType.HIGH_RATED,
                modifier = modifier,
                content = { TrendingGameCardLoading() },
            )
        }

        is DiscoveryUiState.ReLoading -> {
            LoadingGamesSection(
                sectionType = SectionType.HIGH_RATED,
                modifier = modifier,
                content = { TrendingGameCardLoading() },
            )
        }

        is DiscoveryUiState.Error -> {
            ErrorSection(
                sectionType = SectionType.HIGH_RATED,
                sectionColor = HighRatedGradientStart,
                sectionIcon = Icons.Filled.Star,
                throwable = uiState.throwable,
                onRetry = onRetry,
                modifier = modifier,
            )
        }

        is DiscoveryUiState.ReLoadingError -> {
            ErrorSection(
                sectionType = SectionType.HIGH_RATED,
                sectionColor = HighRatedGradientStart,
                sectionIcon = Icons.Filled.Star,
                throwable = uiState.throwable,
                onRetry = onRetry,
                modifier = modifier,
            )
        }
        is DiscoveryUiState.InitialLoading -> {
            // no-op
        }
    }
}

@Composable
private fun NewReleaseGames(
    uiState: DiscoveryUiState,
    onNavigateToDetail: (Int) -> Unit,
    onReload: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is DiscoveryUiState.Success -> {
            NewReleaseGamesSection(
                isSuccessState = true,
                games = uiState.data,
                onGameClick = { game ->
                    onNavigateToDetail(game.id)
                },
                onReload = onReload,
                modifier = modifier,
            )
        }

        is DiscoveryUiState.Loading -> {
            LoadingGamesSection(
                sectionType = SectionType.NEW_RELEASE,
                modifier = modifier,
                content = { TrendingGameCardLoading() },
            )
        }

        is DiscoveryUiState.ReLoading -> {
            LoadingGamesSection(
                sectionType = SectionType.NEW_RELEASE,
                modifier = modifier,
                content = { TrendingGameCardLoading() },
            )
        }

        is DiscoveryUiState.Error -> {
            ErrorSection(
                sectionType = SectionType.NEW_RELEASE,
                sectionColor = NewReleaseGradientStart,
                sectionIcon = Icons.Filled.NewReleases,
                throwable = uiState.throwable,
                onRetry = onRetry,
                modifier = modifier,
            )
        }

        is DiscoveryUiState.ReLoadingError -> {
            ErrorSection(
                sectionType = SectionType.NEW_RELEASE,
                sectionColor = NewReleaseGradientStart,
                sectionIcon = Icons.Filled.NewReleases,
                throwable = uiState.throwable,
                onRetry = onRetry,
                modifier = modifier,
            )
        }
        is DiscoveryUiState.InitialLoading -> {
            // no-op
        }
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
        isInitialLoading = false,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        onNavigateToDetail = {},
        loadingAllSection = {},
        onReloadTrendSection = {},
        onReloadHighRatedSection = {},
        onReloadNewReleaseSection = {},
        onRetryTrendSection = {},
        onRetryHighRatedSection = {},
        onRetryNewReleaseSection = {},
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
