package com.lilin.gamelibrary.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.lilin.gamelibrary.domain.model.GameDetail
import com.lilin.gamelibrary.ui.component.GameBackgroundImageSection
import com.lilin.gamelibrary.ui.component.GameBasicInfoSection
import com.lilin.gamelibrary.ui.component.GameDescriptionSection
import com.lilin.gamelibrary.ui.component.GameDetailTopAppBar
import com.lilin.gamelibrary.ui.component.GameRatingSummarySection
import com.lilin.gamelibrary.ui.component.GameScreenshotsSection
import com.lilin.gamelibrary.ui.component.GameTagsSection
import com.lilin.gamelibrary.ui.component.SkeletonGameBackgroundImageSection
import com.lilin.gamelibrary.ui.component.SkeletonGameBasicInfoSection
import com.lilin.gamelibrary.ui.component.SkeletonGameDescriptionSection
import com.lilin.gamelibrary.ui.component.SkeletonGameRatingSummarySection
import com.lilin.gamelibrary.ui.component.SkeletonGameScreenshotsSection
import com.lilin.gamelibrary.ui.component.SkeletonGameTagsSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    onBackClick: () -> Unit,
    viewModel: GameDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior() // 詳細画面に必要かどうか？

    Scaffold(
        topBar = {
            GameDetailTopAppBar(
                title = when (uiState) {
                    is GameDetailUiState.Success -> (uiState as GameDetailUiState.Success).gameDetail.name
                    else -> ""
                },
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick,
                onStarClick = { /*TODO*/ },
                onShareClick = { /*TODO*/ },
            )
        },
        contentWindowInsets = WindowInsets.navigationBars
    ) {
        GameDetailScreen(
            uiState = uiState,
            scrollBehavior = scrollBehavior,
            onRetry = viewModel::retryLoadGameDetail,
            modifier = Modifier.padding(it),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameDetailScreen(
    uiState: GameDetailUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    onRetry: () -> Unit,
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
                modifier = modifier,
            )
        }

        is GameDetailUiState.Error -> {
            GameDetailErrorContent(
                throwable = uiState.throwable,
                onRetry = onRetry,
                modifier = modifier
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
    ) {
        item { SkeletonGameBackgroundImageSection() }
        item { SkeletonGameBasicInfoSection() }
        item { SkeletonGameRatingSummarySection() }
        item { SkeletonGameDescriptionSection() }
        item { SkeletonGameScreenshotsSection() }
        item { SkeletonGameTagsSection() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameDetailSuccessContent(
    gameDetail: GameDetail,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    val context = LocalUriHandler.current

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // セクション2: 背景画像
        item {
            GameBackgroundImageSection(gameDetail = gameDetail)
        }

        // セクション3: 基本情報
        item {
            GameBasicInfoSection(gameDetail = gameDetail)
        }

        // セクション4: 評価サマリー メタデータ情報
        item {
            GameRatingSummarySection(gameDetail = gameDetail)
        }

        // セクション6: 説明
        item {
            GameDescriptionSection(gameDetail = gameDetail)
        }

        // セクション7: スクリーンショット
        item {
            GameScreenshotsSection(gameDetail = gameDetail)
        }

        // セクション8: タグ
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
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "エラーが発生しました",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error
                )

                Text(
                    text = throwable.message ?: "不明なエラーが発生しました",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Button(onClick = onRetry) {
                    Text("再試行")
                }
            }
        }
    }
}
