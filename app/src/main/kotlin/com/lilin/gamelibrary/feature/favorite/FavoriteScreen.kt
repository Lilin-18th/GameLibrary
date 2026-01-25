package com.lilin.gamelibrary.feature.favorite

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.feature.favorite.expanded.FavoriteExpandedLayout
import com.lilin.gamelibrary.ui.component.favorite.FavoriteGameCompactCard
import com.lilin.gamelibrary.ui.component.favorite.FavoriteScreenHeader
import com.lilin.gamelibrary.ui.component.favorite.FavoriteSortFabMenu
import com.lilin.gamelibrary.ui.component.favorite.DeleteConfirmDialog
import com.lilin.gamelibrary.ui.theme.FavoriteGradientEnd
import com.lilin.gamelibrary.ui.theme.FavoriteGradientStart
import kotlinx.serialization.Serializable

@Serializable
object FavoriteScreen

fun NavGraphBuilder.navigateFavoriteScreen(
    isAtLeastMedium: Boolean,
    navigateToDetail: (Int) -> Unit,
) {
    composable<FavoriteScreen> {
        FavoriteScreen(
            isAtLeastMedium = isAtLeastMedium,
            navigateToDetail = { gameId ->
                navigateToDetail(gameId)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    isAtLeastMedium: Boolean,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()

    val expandedUiState by viewModel.expandedUiState.collectAsStateWithLifecycle()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsStateWithLifecycle()
    val selectedGameName by viewModel.selectedGameName.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            if (!isAtLeastMedium) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.favorite_screen_title),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                )
            }
        },
        floatingActionButton = {
            if (!isAtLeastMedium) {
                FavoriteSortFabMenu(
                    sortOrder = sortOrder,
                    onSortClick = viewModel::toggleSortOrder,
                )
            }
        },
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = modifier,
    ) { paddingValues ->
        if (isAtLeastMedium) {
            FavoriteExpandedLayout(
                uiState = expandedUiState,
                bottomBarPadding = paddingValues.calculateBottomPadding(),
                onSortChange = viewModel::onSortChange,
                onGameClick = { gameId ->
                    navigateToDetail(gameId)
                },
                onDeleteClick = viewModel::showDeleteConfirmDialog,
            )
        } else {
            FavoriteScreen(
                uiState = uiState,
                navigateToDetail = { gameId ->
                    navigateToDetail(gameId)
                },
                onClickDelete = viewModel::showDeleteConfirmDialog,
                modifier = Modifier.padding(paddingValues),
            )
        }

        if (showDeleteDialog) {
            DeleteConfirmDialog(
                gameName = selectedGameName,
                onDismiss = viewModel::dismissDeleteDialog,
                onConfirm = viewModel::confirmDelete,
            )
        }
    }
}

@Composable
private fun FavoriteScreen(
    uiState: FavoriteUiState,
    navigateToDetail: (Int) -> Unit,
    onClickDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is FavoriteUiState.Empty -> {
            FavoriteEmptyContent(
                modifier = modifier.fillMaxSize(),
            )
        }

        is FavoriteUiState.Loading -> {
            FavoriteGridLoading()
        }

        is FavoriteUiState.Success -> {
            val listState = rememberLazyListState()

            LaunchedEffect(uiState.sortOrder) {
                listState.scrollToItem(0)
            }

            Column(modifier = modifier.fillMaxSize()) {
                FavoriteScreenHeader(
                    gameCount = uiState.games.size,
                )

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 0.dp,
                        bottom = 88.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        items = uiState.games,
                        key = { it.id },
                    ) {
                        FavoriteGameCompactCard(
                            game = it,
                            onClick = { gameId ->
                                navigateToDetail(gameId)
                            },
                            onClickDelete = { gameId ->
                                onClickDelete(gameId)
                            },
                        )
                    }
                }
            }
        }

        is FavoriteUiState.Error -> {
            FavoriteGridError(
                message = uiState.message,
            )
        }
    }
}

@Composable
fun FavoriteGridLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = FavoriteGradientEnd,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun FavoriteEmptyContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            FavoriteGradientStart,
                            FavoriteGradientEnd.copy(alpha = 0.7f),
                        ),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color.White,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.favorite_empty_title),
            modifier = Modifier.padding(horizontal = 32.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.favorite_empty_message),
            modifier = Modifier.padding(horizontal = 32.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun FavoriteGridError(
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Preview
@Composable
private fun FavoriteEmptyContentPreview() {
    FavoriteEmptyContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@VisibleForTesting
@Composable
internal fun FavoriteScreenSample(
    uiState: FavoriteUiState,
    modifier: Modifier = Modifier,
) {
    FavoriteScreen(
        uiState = uiState,
        navigateToDetail = {},
        onClickDelete = {},
        modifier = modifier,
    )
}
