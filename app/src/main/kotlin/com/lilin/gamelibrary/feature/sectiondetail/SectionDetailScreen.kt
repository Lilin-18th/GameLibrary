package com.lilin.gamelibrary.feature.sectiondetail

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.toRoute
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.ui.component.SectionDetailTopAppBar
import com.lilin.gamelibrary.ui.component.discovery.HighRatedGameCard
import com.lilin.gamelibrary.ui.component.discovery.NewReleaseGameCard
import com.lilin.gamelibrary.ui.component.discovery.SectionType
import com.lilin.gamelibrary.ui.component.discovery.TrendingGameCard
import com.lilin.gamelibrary.ui.component.sectiondetail.SectionDetailBottomBar
import com.lilin.gamelibrary.ui.component.toErrorMessage
import kotlinx.serialization.Serializable

@Serializable
data class SectionDetailScreen(val sectionTypeName: String)

fun NavGraphBuilder.navigateSectionDetailScreen(
    onBackClick: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
) {
    composable<SectionDetailScreen> { backStackEntry ->
        backStackEntry.toRoute<SectionDetailScreen>()
        SectionDetailScreen(
            onBackClick = onBackClick,
            onNavigateToDetail = onNavigateToDetail,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionDetailScreen(
    onBackClick: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SectionDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            SectionDetailTopAppBar(
                sectionType = viewModel.sectionType,
                gameCount = if (uiState is SectionDetailUiState.Success) {
                    (uiState as SectionDetailUiState.Success).totalCount
                } else {
                    0
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            SectionDetailBottomBar(
                sectionType = viewModel.sectionType,
                onBackClick = onBackClick,
                modifier = Modifier,
            )
        },
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = modifier,
    ) { paddingValues ->
        SectionDetailScreen(
            uiState = uiState,
            sectionType = viewModel.sectionType,
            onNavigateToDetail = onNavigateToDetail,
            onRetry = viewModel::retry,
            bottomBarPadding = paddingValues.calculateBottomPadding(),
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
            ),
        )
    }
}

@Composable
private fun SectionDetailScreen(
    uiState: SectionDetailUiState,
    sectionType: SectionType,
    onNavigateToDetail: (Int) -> Unit,
    onRetry: () -> Unit,
    bottomBarPadding: Dp,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is SectionDetailUiState.Loading -> {
            SectionDetailLoadingContent(
                sectionType = sectionType,
                modifier = modifier,
            )
        }

        is SectionDetailUiState.Success -> {
            SectionDetailSuccessContent(
                sectionType = sectionType,
                games = uiState.data,
                onGameClick = onNavigateToDetail,
                bottomBarPadding = bottomBarPadding,
                modifier = modifier,
            )
        }

        is SectionDetailUiState.Error -> {
            SectionDetailErrorContent(
                throwable = uiState.throwable,
                onRetry = onRetry,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun SectionDetailLoadingContent(
    sectionType: SectionType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = sectionType.gradientStart,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

@Composable
private fun SectionDetailSuccessContent(
    sectionType: SectionType,
    games: List<Game>,
    onGameClick: (Int) -> Unit,
    bottomBarPadding: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 16.dp,
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
}

@Composable
private fun SectionDetailErrorContent(
    throwable: Throwable,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
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

                val errorMessage = throwable.toErrorMessage()
                Text(
                    text = stringResource(errorMessage.title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = stringResource(errorMessage.subtitle),
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
}

@VisibleForTesting
@Composable
internal fun SectionDetailScreenSample(
    uiState: SectionDetailUiState,
    sectionType: SectionType,
    modifier: Modifier = Modifier,
) {
    SectionDetailScreen(
        uiState = uiState,
        sectionType = sectionType,
        onNavigateToDetail = {},
        onRetry = {},
        bottomBarPadding = 90.dp,
        modifier = modifier,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun SectionDetailScreenTrendingPreview() {
    val sampleGames = listOf(
        Game(
            id = 1,
            name = "The Witcher 3: Wild Hunt",
            imageUrl = null,
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
            name = "Cyberpunk 2077",
            imageUrl = null,
            releaseDate = "2020-12-10",
            rating = 4.2,
            ratingsCount = 8765,
            metacritic = 86,
            isTba = false,
            addedCount = 30000,
            platforms = listOf("PC", "PS5", "Xbox Series X"),
        ),
    )

    SectionDetailScreenSample(
        uiState = SectionDetailUiState.Success(
            data = sampleGames,
            totalCount = 50,
        ),
        sectionType = SectionType.TRENDING,
    )
}
