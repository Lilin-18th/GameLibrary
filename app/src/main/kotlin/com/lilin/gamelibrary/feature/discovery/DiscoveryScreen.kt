@file:OptIn(ExperimentalMaterial3Api::class)

package com.lilin.gamelibrary.feature.discovery

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.ui.component.DiscoveryTopBar
import com.lilin.gamelibrary.ui.component.LoadingScreen
import com.lilin.gamelibrary.ui.component.SectionDetailTopAppBar
import com.lilin.gamelibrary.ui.component.discovery.DiscoveryBottomBar
import com.lilin.gamelibrary.ui.component.discovery.ErrorSection
import com.lilin.gamelibrary.ui.component.discovery.HighRatedGameCard
import com.lilin.gamelibrary.ui.component.discovery.HighRatedGamesSection
import com.lilin.gamelibrary.ui.component.discovery.LoadingGamesSection
import com.lilin.gamelibrary.ui.component.discovery.NewReleaseGameCard
import com.lilin.gamelibrary.ui.component.discovery.NewReleaseGamesSection
import com.lilin.gamelibrary.ui.component.discovery.SectionType
import com.lilin.gamelibrary.ui.component.discovery.TrendingGameCard
import com.lilin.gamelibrary.ui.component.discovery.TrendingGameCardLoading
import com.lilin.gamelibrary.ui.component.discovery.TrendingGamesSection
import com.lilin.gamelibrary.ui.theme.HighRatedGradientStart
import com.lilin.gamelibrary.ui.theme.NewReleaseGradientStart
import com.lilin.gamelibrary.ui.theme.TrendingGradientStart
import kotlinx.serialization.Serializable

@Serializable
object DiscoveryScreen

fun NavGraphBuilder.navigateDiscoveryScreen(
    isAtLeastMedium: Boolean,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToSectionDetail: (SectionType) -> Unit,
) {
    composable<DiscoveryScreen> {
        DiscoveryScreen(
            isAtLeastMedium = isAtLeastMedium,
            onNavigateToDetail = { gameId ->
                onNavigateToDetail(gameId)
            },
            onNavigateToSectionDetail = onNavigateToSectionDetail,
        )
    }
}

@Composable
fun DiscoveryScreen(
    isAtLeastMedium: Boolean,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToSectionDetail: (SectionType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DiscoveryViewModel = hiltViewModel(),
) {
    val trendingState by viewModel.trendingState.collectAsState()
    val highlyRatedState by viewModel.highlyRatedState.collectAsState()
    val newReleasesState by viewModel.newReleasesState.collectAsState()
    val expandedUiState by viewModel.expandedUiState.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    DiscoveryScreen(
        isAtLeastMedium = isAtLeastMedium,
        expandedUiState = expandedUiState,
        trendingState = trendingState,
        highlyRatedState = highlyRatedState,
        newReleasesState = newReleasesState,
        scrollBehavior = scrollBehavior,
        loadingAllSection = viewModel::loadAllSections,
        onReloadTrendSection = viewModel::reloadTrendingGames,
        onReloadHighRatedSection = viewModel::reloadHighlyRatedGames,
        onReloadNewReleaseSection = viewModel::reloadNewReleaseGame,
        onRetryTrendSection = viewModel::retryTrendingGames,
        onRetryHighRatedSection = viewModel::retryHighlyRatedGames,
        onRetryNewReleaseSection = viewModel::retryNewReleases,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToSectionDetail = onNavigateToSectionDetail,
        loadSectionExpanded = viewModel::loadSectionExpanded,
        modifier = modifier,
    )
}

@Composable
private fun DiscoveryScreen(
    isAtLeastMedium: Boolean,
    expandedUiState: DiscoveryExpandedUiState,
    trendingState: DiscoveryUiState,
    highlyRatedState: DiscoveryUiState,
    newReleasesState: DiscoveryUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    loadingAllSection: () -> Unit,
    onReloadTrendSection: () -> Unit,
    onReloadHighRatedSection: () -> Unit,
    onReloadNewReleaseSection: () -> Unit,
    onRetryTrendSection: () -> Unit,
    onRetryHighRatedSection: () -> Unit,
    onRetryNewReleaseSection: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToSectionDetail: (SectionType) -> Unit,
    loadSectionExpanded: (SectionType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable { mutableStateOf(SectionType.TRENDING) }

    Scaffold(
        topBar = {
            if (isAtLeastMedium) {
                SectionDetailTopAppBar(
                    sectionType = SectionType.TRENDING,
                    gameCount = if (expandedUiState is DiscoveryExpandedUiState.Success) {
                        expandedUiState.totalCount
                    } else {
                        0
                    },
                    scrollBehavior = scrollBehavior,
                )
            } else {
                DiscoveryTopBar(scrollBehavior = scrollBehavior)
            }
        },
        bottomBar = {
            if (isAtLeastMedium) {
                DiscoveryBottomBar(
                    onClickTrending = {
                        selectedTab = SectionType.TRENDING
                    },
                    onClickHighRated = {
                        selectedTab = SectionType.HIGH_RATED
                    },
                    onClickNewRelease = {
                        selectedTab = SectionType.NEW_RELEASE
                    },
                    selectedTab = selectedTab,
                )
            }
        },
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = modifier,
    ) { paddingValues ->
        if (isAtLeastMedium) {
            LaunchedEffect(selectedTab) {
                loadSectionExpanded(selectedTab)
            }

            ExpandedDiscoveryScreen(
                expandedUiState = expandedUiState,
                topBarPadding = paddingValues.calculateTopPadding(),
                bottomBarPadding = paddingValues.calculateBottomPadding(),
                onNavigateToDetail = onNavigateToDetail,
                onRetrySection = {},
                modifier = Modifier.padding(paddingValues),
            )
        } else {
            val isInitialLoading =
                trendingState is DiscoveryUiState.InitialLoading ||
                    highlyRatedState is DiscoveryUiState.InitialLoading ||
                    newReleasesState is DiscoveryUiState.InitialLoading

            LaunchedEffect(Unit) {
                loadingAllSection()
            }

            CompactDiscoveryScreen(
                trendingState = trendingState,
                highlyRatedState = highlyRatedState,
                newReleasesState = newReleasesState,
                scrollBehavior = scrollBehavior,
                isInitialLoading = isInitialLoading,
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToSectionDetail = onNavigateToSectionDetail,
                loadingAllSection = loadingAllSection,
                onReloadTrendSection = onReloadTrendSection,
                onReloadHighRatedSection = onReloadHighRatedSection,
                onReloadNewReleaseSection = onReloadNewReleaseSection,
                onRetryTrendSection = onRetryTrendSection,
                onRetryHighRatedSection = onRetryHighRatedSection,
                onRetryNewReleaseSection = onRetryNewReleaseSection,
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}

@Composable
private fun ExpandedDiscoveryScreen(
    expandedUiState: DiscoveryExpandedUiState,
    topBarPadding: Dp,
    bottomBarPadding: Dp,
    onNavigateToDetail: (Int) -> Unit,
    onRetrySection: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (expandedUiState) {
        is DiscoveryExpandedUiState.Loading -> {
            LoadingScreen(modifier = modifier)
        }

        is DiscoveryExpandedUiState.Error -> {
            FullScreenError(
                onRetry = onRetrySection,
                modifier = modifier,
            )
        }

        is DiscoveryExpandedUiState.Success -> {
            LeastMediumContent(
                games = expandedUiState.games,
                sectionType = expandedUiState.selectedSection,
                topBarPadding = topBarPadding,
                bottomBarPadding = bottomBarPadding,
                onGameClick = onNavigateToDetail,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun CompactDiscoveryScreen(
    trendingState: DiscoveryUiState,
    highlyRatedState: DiscoveryUiState,
    newReleasesState: DiscoveryUiState,
    isInitialLoading: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToSectionDetail: (SectionType) -> Unit,
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
            LoadingScreen(
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
            CompactContent(
                trendingState = trendingState,
                highlyRatedState = highlyRatedState,
                newReleasesState = newReleasesState,
                scrollBehavior = scrollBehavior,
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToSectionDetail = onNavigateToSectionDetail,
                onReloadTrendSection = onReloadTrendSection,
                onReloadHighRatedSection = onReloadHighRatedSection,
                onReloadNewReleaseSection = onReloadNewReleaseSection,
                onRetryTrendSection = onRetryTrendSection,
                onRetryHighRatedSection = onRetryHighRatedSection,
                onRetryNewReleaseSection = onRetryNewReleaseSection,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun CompactContent(
    trendingState: DiscoveryUiState,
    highlyRatedState: DiscoveryUiState,
    newReleasesState: DiscoveryUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToSectionDetail: (SectionType) -> Unit,
    onReloadTrendSection: () -> Unit,
    onReloadHighRatedSection: () -> Unit,
    onReloadNewReleaseSection: () -> Unit,
    onRetryTrendSection: () -> Unit,
    onRetryHighRatedSection: () -> Unit,
    onRetryNewReleaseSection: () -> Unit,
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
                onNavigateToSectionDetail = onNavigateToSectionDetail,
                onRetry = onRetryTrendSection,
                onReload = onReloadTrendSection,
                modifier = Modifier,
            )
        }

        item {
            HighRatedGames(
                uiState = highlyRatedState,
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToSectionDetail = onNavigateToSectionDetail,
                onRetry = onRetryHighRatedSection,
                onReload = onReloadHighRatedSection,
                modifier = Modifier,
            )
        }

        item {
            NewReleaseGames(
                uiState = newReleasesState,
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToSectionDetail = onNavigateToSectionDetail,
                onRetry = onRetryNewReleaseSection,
                onReload = onReloadNewReleaseSection,
                modifier = Modifier,
            )
        }
    }
}

@Composable
private fun LeastMediumContent(
    games: List<Game>,
    sectionType: SectionType,
    topBarPadding: Dp,
    bottomBarPadding: Dp,
    onGameClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridScrollState = rememberLazyGridState()
    LazyVerticalGrid(
        state = gridScrollState,
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = topBarPadding + 10.dp,
            bottom = bottomBarPadding,
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items = games, key = { it.id }) { game ->
            when (sectionType) {
                SectionType.TRENDING -> {
                    TrendingGameCard(
                        game = game,
                        onClick = { onGameClick(game.id) },
                    )
                }

                SectionType.HIGH_RATED -> {
                    HighRatedGameCard(
                        game = game,
                        onClick = { onGameClick(game.id) },
                    )
                }

                SectionType.NEW_RELEASE -> {
                    NewReleaseGameCard(
                        game = game,
                        onClick = { onGameClick(game.id) },
                    )
                }
            }
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
    onNavigateToSectionDetail: (SectionType) -> Unit,
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
                onSeeMoreClick = {
                    onNavigateToSectionDetail(SectionType.TRENDING)
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
    onNavigateToSectionDetail: (SectionType) -> Unit,
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
                onSeeMoreClick = {
                    onNavigateToSectionDetail(SectionType.HIGH_RATED)
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
    onNavigateToSectionDetail: (SectionType) -> Unit,
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
                onSeeMoreClick = {
                    onNavigateToSectionDetail(SectionType.NEW_RELEASE)
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

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@VisibleForTesting
@Composable
internal fun DiscoveryScreenSample(
    trendingState: DiscoveryUiState,
    highlyRatedState: DiscoveryUiState,
    newReleasesState: DiscoveryUiState,
    expandedUiState: DiscoveryExpandedUiState,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current
    val activity = context.findActivity()
    val windowWidthSize = calculateWindowSizeClass(activity).widthSizeClass

    val isAtLeastMedium = when (windowWidthSize) {
        WindowWidthSizeClass.Compact -> {
            false
        }

        else -> true
    }
    Scaffold(
        topBar = {
            if (isAtLeastMedium) {
                SectionDetailTopAppBar(
                    sectionType = SectionType.TRENDING,
                    gameCount = if (expandedUiState is DiscoveryExpandedUiState.Success) {
                        expandedUiState.totalCount
                    } else {
                        0
                    },
                    scrollBehavior = scrollBehavior,
                )
            } else {
                DiscoveryTopBar(scrollBehavior = scrollBehavior)
            }
        },
        bottomBar = {
            if (isAtLeastMedium) {
                DiscoveryBottomBar(
                    onClickTrending = {},
                    onClickHighRated = {},
                    onClickNewRelease = {},
                    selectedTab = SectionType.TRENDING,
                )
            }
        },
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = modifier,
    ) { paddingValues ->
        DiscoveryScreen(
            isAtLeastMedium = isAtLeastMedium,
            expandedUiState = expandedUiState,
            trendingState = trendingState,
            highlyRatedState = highlyRatedState,
            newReleasesState = newReleasesState,
            scrollBehavior = scrollBehavior,
            loadingAllSection = {},
            onReloadTrendSection = {},
            onReloadHighRatedSection = {},
            onReloadNewReleaseSection = {},
            onRetryTrendSection = {},
            onRetryHighRatedSection = {},
            onRetryNewReleaseSection = {},
            onNavigateToDetail = {},
            onNavigateToSectionDetail = {},
            loadSectionExpanded = {},
            modifier = modifier,
        )
    }
}

private fun Context.findActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("No ComponentActivity found")
}

@Preview(name = "Compact", widthDp = 400)
@Preview(name = "Medium", widthDp = 600)
@Preview(name = "Expand", widthDp = 800)
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

    DiscoveryScreenSample(
        expandedUiState = DiscoveryExpandedUiState.Success(
            games = games,
            selectedSection = SectionType.TRENDING,
            totalCount = games.size,
        ),
        trendingState = DiscoveryUiState.Success(data = games),
        highlyRatedState = DiscoveryUiState.Success(data = games),
        newReleasesState = DiscoveryUiState.Success(data = games),
    )
}
