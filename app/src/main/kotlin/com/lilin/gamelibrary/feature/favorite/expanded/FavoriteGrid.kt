package com.lilin.gamelibrary.feature.favorite.expanded

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.feature.favorite.FavoriteEmptyContent
import com.lilin.gamelibrary.feature.favorite.FavoriteExpandedUiState
import com.lilin.gamelibrary.feature.favorite.FavoriteGridError
import com.lilin.gamelibrary.feature.favorite.FavoriteGridLoading
import com.lilin.gamelibrary.ui.component.favorite.DeleteConfirmDialog
import com.lilin.gamelibrary.ui.component.favorite.expanded.FavoriteGameExpandedCard
import com.lilin.gamelibrary.ui.theme.GameLibraryTheme

@Composable
fun FavoriteGrid(
    uiState: FavoriteExpandedUiState,
    bottomBarPadding: Dp,
    onGameClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is FavoriteExpandedUiState.Empty -> {
            FavoriteEmptyContent()
        }

        is FavoriteExpandedUiState.Loading -> {
            FavoriteGridLoading()
        }

        is FavoriteExpandedUiState.Success -> {
            FavoriteGridSuccess(
                games = uiState.games,
                bottomBarPadding = bottomBarPadding,
                onGameClick = onGameClick,
                onDeleteClick = onDeleteClick,
                modifier = modifier,
            )
        }

        is FavoriteExpandedUiState.Error -> {
            FavoriteGridError(
                message = uiState.message,
            )
        }
    }
}

@Composable
private fun FavoriteGridSuccess(
    games: List<FavoriteGame>,
    bottomBarPadding: Dp,
    onGameClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridScrollState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize(),
        state = gridScrollState,
        contentPadding = PaddingValues(
            start = 10.dp,
            end = 10.dp,
            bottom = bottomBarPadding,
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = games,
            key = { it.id },
        ) {
            FavoriteGameExpandedCard(
                game = it,
                onClick = onGameClick,
                onDelete = {
                    onDeleteClick(it.id)
                },
                modifier = Modifier.animateItem(),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
private fun FavoriteGridLoadingPreview() {
    GameLibraryTheme {
        FavoriteGrid(
            uiState = FavoriteExpandedUiState.Loading,
            bottomBarPadding = 16.dp,
            onGameClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
private fun FavoriteGridEmptyPreview() {
    GameLibraryTheme {
        FavoriteGrid(
            uiState = FavoriteExpandedUiState.Empty,
            bottomBarPadding = 0.dp,
            onGameClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
private fun FavoriteGridSuccessPreview() {
    val sampleGames = listOf(
        FavoriteGame(
            id = 1,
            name = "Grand Theft Auto V",
            backgroundImage = null,
            rating = 4.5,
            metacritic = 97,
            released = "2013-09-17",
            addedAt = System.currentTimeMillis(),
        ),
        FavoriteGame(
            id = 2,
            name = "The Witcher 3",
            backgroundImage = null,
            rating = 4.8,
            metacritic = 93,
            released = "2015-05-19",
            addedAt = System.currentTimeMillis(),
        ),
        FavoriteGame(
            id = 3,
            name = "Red Dead Redemption 2",
            backgroundImage = null,
            rating = 4.6,
            metacritic = 97,
            released = "2018-10-26",
            addedAt = System.currentTimeMillis(),
        ),
        FavoriteGame(
            id = 4,
            name = "Portal 2",
            backgroundImage = null,
            rating = 4.7,
            metacritic = 95,
            released = "2011-04-19",
            addedAt = System.currentTimeMillis(),
        ),
    )

    GameLibraryTheme {
        var showDialog by remember { mutableStateOf(false) }

        FavoriteGrid(
            uiState = FavoriteExpandedUiState.Success(
                games = sampleGames,
                statistics = com.lilin.gamelibrary.domain.model.FavoriteStatistics(
                    totalCount = sampleGames.size,
                    avgRating = 4.6,
                    latestAdded = "2日前",
                ),
                sortOption = com.lilin.gamelibrary.domain.model.SortOption.ADDED_DATE_DESC,
            ),
            bottomBarPadding = 0.dp,
            onGameClick = {},
            onDeleteClick = { showDialog = true },
        )

        if (showDialog) {
            DeleteConfirmDialog(
                gameName = "Grand Theft Auto V",
                onDismiss = { showDialog = false },
                onConfirm = { showDialog = false },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
private fun FavoriteGridErrorPreview() {
    GameLibraryTheme {
        FavoriteGrid(
            uiState = FavoriteExpandedUiState.Error("データの読み込みに失敗しました"),
            bottomBarPadding = 0.dp,
            onGameClick = {},
            onDeleteClick = {},
        )
    }
}
