package com.lilin.gamelibrary.feature.discovery

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.ui.component.DiscoveryTopBar
import com.lilin.gamelibrary.ui.component.ErrorSection
import com.lilin.gamelibrary.ui.component.HighRatedGamesSection
import com.lilin.gamelibrary.ui.component.NewReleaseGamesSection
import com.lilin.gamelibrary.ui.component.SkeletonGamesSection
import com.lilin.gamelibrary.ui.component.SkeletonNewReleaseGamesSection
import com.lilin.gamelibrary.ui.component.TrendingGamesSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: DiscoveryViewModel = hiltViewModel(),
) {
    val trendingState by viewModel.trendingState.collectAsState()
    val highlyRatedState by viewModel.highlyRatedState.collectAsState()
    val newReleasesState by viewModel.newReleasesState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            DiscoveryTopBar(
                onSearchClick = {},
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = WindowInsets.navigationBars,
    ) { paddingValues ->
        DiscoveryScreen(
            trendingState = trendingState,
            highlyRatedState = highlyRatedState,
            newReleasesState = newReleasesState,
            scrollBehavior = scrollBehavior,
            onNavigateToDetail = onNavigateToDetail,
            modifier = Modifier.padding(paddingValues)
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
            when (trendingState) {
                is DiscoveryUiState.Success -> {
                    TrendingGamesSection(
                        games = trendingState.data,
                        onGameClick = { game ->
                            onNavigateToDetail(game.id)
                        },
                    )
                }

                is DiscoveryUiState.Loading -> {
                    SkeletonGamesSection(
                        sectionHeaderTitle = stringResource(R.string.trending_section_title),
                    )
                }

                is DiscoveryUiState.Error -> {
                    ErrorSection(
                        sectionHeaderTitle = stringResource(R.string.trending_section_title),
                        throwable = trendingState.throwable,
                    )
                }

                else -> {}
//                is DiscoveryUiState.ReLoading -> TODO()
//                is DiscoveryUiState.ReLoadingError -> TODO()
            }
        }

        item {
            when (highlyRatedState) {
                is DiscoveryUiState.Success -> {
                    HighRatedGamesSection(
                        games = highlyRatedState.data,
                        onGameClick = { game ->
                            onNavigateToDetail(game.id)
                        },
                    )
                }

                is DiscoveryUiState.Loading -> {
                    SkeletonGamesSection(
                        sectionHeaderTitle = stringResource(R.string.metacritic_section_title),
                    )
                }

                is DiscoveryUiState.Error -> {
                    ErrorSection(
                        sectionHeaderTitle = stringResource(R.string.metacritic_section_title),
                        throwable = highlyRatedState.throwable
                    )
                }

                else -> {}
//                is DiscoveryUiState.ReLoading -> TODO()
//                is DiscoveryUiState.ReLoadingError -> TODO()
            }
        }

        item {
            when (newReleasesState) {
                is DiscoveryUiState.Success -> {
                    NewReleaseGamesSection(
                        games = newReleasesState.data,
                        onGameClick = { game ->
                            onNavigateToDetail(game.id)
                        },
                    )
                }

                is DiscoveryUiState.Loading -> {
                    SkeletonNewReleaseGamesSection()
                }

                is DiscoveryUiState.Error -> {
                    ErrorSection(
                        sectionHeaderTitle = stringResource(R.string.new_release_section_title),
                        throwable = newReleasesState.throwable
                    )
                }

                else -> {}
//                is DiscoveryUiState.ReLoading -> TODO()
//                is DiscoveryUiState.ReLoadingError -> TODO()
            }
        }
    }
}
