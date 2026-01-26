package com.lilin.gamelibrary.feature.favorite.expanded

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.domain.model.FavoriteStatistics
import com.lilin.gamelibrary.domain.model.SortOption
import com.lilin.gamelibrary.feature.favorite.FavoriteExpandedUiState
import com.lilin.gamelibrary.ui.component.favorite.DeleteConfirmDialog
import com.lilin.gamelibrary.ui.component.favorite.expanded.FavoriteSidebar
import com.lilin.gamelibrary.ui.component.favorite.expanded.FavoriteTopAppBar
import com.lilin.gamelibrary.ui.theme.GameLibraryTheme

@Composable
fun FavoriteExpandedLayout(
    uiState: FavoriteExpandedUiState,
    bottomBarPadding: Dp,
    onSortChange: (SortOption) -> Unit,
    onGameClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize(),
    ) {
        FavoriteSidebar(
            currentSort = when (uiState) {
                is FavoriteExpandedUiState.Success -> uiState.sortOption
                else -> SortOption.ADDED_DATE_DESC
            },
            statistics = when (uiState) {
                is FavoriteExpandedUiState.Success -> uiState.statistics
                else -> FavoriteStatistics(
                    totalCount = 0,
                    avgRating = 0.0,
                    latestAdded = "-",
                )
            },
            onSortChange = onSortChange,
            modifier = Modifier.weight(0.3f),
        )

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            thickness = 1.dp,
        )

        Column(
            modifier = Modifier.weight(0.7f),
        ) {
            FavoriteTopAppBar(
                title = stringResource(R.string.favorite_screen_title),
                itemCount = when (uiState) {
                    is FavoriteExpandedUiState.Success -> uiState.games.size
                    else -> 0
                },
            )

            FavoriteGrid(
                uiState = uiState,
                bottomBarPadding = bottomBarPadding,
                onGameClick = onGameClick,
                onDeleteClick = onDeleteClick,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 800)
@Composable
private fun FavoriteExpandedLayoutLoadingPreview() {
    GameLibraryTheme {
        FavoriteExpandedLayout(
            uiState = FavoriteExpandedUiState.Loading,
            bottomBarPadding = 0.dp,
            onSortChange = {},
            onGameClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 800)
@Composable
private fun FavoriteExpandedLayoutEmptyPreview() {
    GameLibraryTheme {
        FavoriteExpandedLayout(
            uiState = FavoriteExpandedUiState.Empty,
            bottomBarPadding = 0.dp,
            onSortChange = {},
            onGameClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 800)
@Composable
private fun FavoriteExpandedLayoutSuccessPreview() {
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
        FavoriteGame(
            id = 5,
            name = "Half-Life 2",
            backgroundImage = null,
            rating = 4.6,
            metacritic = 96,
            released = "2004-11-16",
            addedAt = System.currentTimeMillis(),
        ),
        FavoriteGame(
            id = 6,
            name = "Minecraft",
            backgroundImage = null,
            rating = 4.4,
            metacritic = 93,
            released = "2011-11-18",
            addedAt = System.currentTimeMillis(),
        ),
    )

    GameLibraryTheme {
        var showDialog by remember { mutableStateOf(false) }
        var selectedGame by remember { mutableStateOf<FavoriteGame?>(null) }

        FavoriteExpandedLayout(
            uiState = FavoriteExpandedUiState.Success(
                games = sampleGames,
                statistics = FavoriteStatistics(
                    totalCount = sampleGames.size,
                    avgRating = 4.6,
                    latestAdded = "2日前",
                ),
                sortOption = SortOption.ADDED_DATE_DESC,
            ),
            bottomBarPadding = 0.dp,
            onSortChange = {},
            onGameClick = {},
            onDeleteClick = {},
        )

        if (showDialog && selectedGame != null) {
            DeleteConfirmDialog(
                gameName = selectedGame!!.name,
                onDismiss = { showDialog = false },
                onConfirm = {
                    showDialog = false
                    selectedGame = null
                },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 800)
@Composable
private fun FavoriteExpandedLayoutErrorPreview() {
    GameLibraryTheme {
        FavoriteExpandedLayout(
            uiState = FavoriteExpandedUiState.Error("データの読み込みに失敗しました"),
            bottomBarPadding = 0.dp,
            onSortChange = {},
            onGameClick = {},
            onDeleteClick = {},
        )
    }
}
