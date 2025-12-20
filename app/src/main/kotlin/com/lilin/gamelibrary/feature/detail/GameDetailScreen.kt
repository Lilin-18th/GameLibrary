package com.lilin.gamelibrary.feature.detail

import android.content.Context
import android.content.Intent
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lilin.gamelibrary.domain.model.GameDetail
import com.lilin.gamelibrary.ui.component.GameDetailTopAppBar
import com.lilin.gamelibrary.ui.component.detail.GameBackgroundImageSection
import com.lilin.gamelibrary.ui.component.detail.GameBasicInfoSection
import com.lilin.gamelibrary.ui.component.detail.GameDescriptionSection
import com.lilin.gamelibrary.ui.component.detail.GameDetailBottomBar
import com.lilin.gamelibrary.ui.component.detail.GameRatingSummarySection
import com.lilin.gamelibrary.ui.component.detail.GameTagsSection
import kotlinx.serialization.Serializable

@Serializable
data class GameDetailScreen(val gameId: Int)

fun NavGraphBuilder.navigateDetailScreen(
    onBackClick: () -> Unit,
) {
    composable<GameDetailScreen> { backStackEntry ->
        backStackEntry.toRoute<GameDetailScreen>()
        GameDetailScreen(
            onBackClick = onBackClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsState()
    val gameTitle by viewModel.gameTitle.collectAsState()
    val shareUrl by viewModel.shareUrl.collectAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            GameDetailTopAppBar(
                title = gameTitle,
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            GameDetailBottomBar(
                onBackClick = onBackClick,
                onFavoriteClick = { viewModel.toggleFavorite() },
                onShareClick = {
                    shareGameWebSite(
                        context = context,
                        url = shareUrl,
                    )
                },
                isFavorite = when (uiState) {
                    is GameDetailUiState.Success -> (uiState as GameDetailUiState.Success).isFavorite
                    else -> false
                },
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier,
    ) { paddingValues ->
        GameDetailScreen(
            uiState = uiState,
            scrollBehavior = scrollBehavior,
            onRetry = viewModel::retryLoadGameDetail,
            bottomBarPadding = paddingValues.calculateBottomPadding(),
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameDetailScreen(
    uiState: GameDetailUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    onRetry: () -> Unit,
    bottomBarPadding: Dp,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is GameDetailUiState.Loading -> {
            GameDetailLoadingContent(
                scrollBehavior = scrollBehavior,
                modifier = modifier,
            )
        }

        is GameDetailUiState.Success -> {
            GameDetailSuccessContent(
                gameDetail = uiState.gameDetail,
                scrollBehavior = scrollBehavior,
                bottomBarPadding = bottomBarPadding,
                modifier = modifier,
            )
        }

        is GameDetailUiState.Error -> {
            GameDetailErrorContent(
                throwable = uiState.throwable,
                onRetry = onRetry,
                modifier = modifier,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailLoadingContent(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameDetailSuccessContent(
    gameDetail: GameDetail,
    scrollBehavior: TopAppBarScrollBehavior,
    bottomBarPadding: Dp,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(bottom = bottomBarPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            GameBackgroundImageSection(gameDetail = gameDetail)
        }

        item {
            GameBasicInfoSection(gameDetail = gameDetail)
        }

        item {
            GameRatingSummarySection(gameDetail = gameDetail)
        }

        item {
            GameDescriptionSection(gameDetail = gameDetail)
        }

        item {
            GameTagsSection(gameDetail = gameDetail)
        }
    }
}

@Composable
private fun GameDetailErrorContent(
    throwable: Throwable,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "エラーが発生しました",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error,
                )

                Text(
                    text = throwable.message ?: "不明なエラーが発生しました",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Button(onClick = onRetry) {
                    Text("再試行")
                }
            }
        }
    }
}

private fun shareGameWebSite(context: Context, url: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)

    context.startActivity(shareIntent)
}

@OptIn(ExperimentalMaterial3Api::class)
@VisibleForTesting
@Composable
internal fun GameDetailScreenSample(
    uiState: GameDetailUiState,
    modifier: Modifier = Modifier,
) {
    GameDetailScreen(
        uiState = uiState,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        onRetry = {},
        bottomBarPadding = 90.dp,
        modifier = modifier,
    )
}
