package com.lilin.gamelibrary.ui.component.favorite.expanded

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.domain.model.FavoriteStatistics
import com.lilin.gamelibrary.domain.model.SortOption
import com.lilin.gamelibrary.ui.theme.GameLibraryTheme

@Composable
fun FavoriteSidebar(
    currentSort: SortOption,
    statistics: FavoriteStatistics,
    onSortChange: (SortOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        SortSection(
            currentSort = currentSort,
            onSortChange = onSortChange,
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        )

        StatisticsSection(
            statistics = statistics,
        )
    }
}

@Preview(showBackground = true, widthDp = 280, heightDp = 800)
@Composable
private fun FavoriteSidebarPreview() {
    GameLibraryTheme {
        FavoriteSidebar(
            currentSort = SortOption.ADDED_DATE_DESC,
            statistics = FavoriteStatistics(
                totalCount = 125,
                avgRating = 4.6,
                latestAdded = "2日前",
            ),
            onSortChange = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 280, heightDp = 800)
@Composable
private fun FavoriteSidebarEmptyPreview() {
    GameLibraryTheme {
        FavoriteSidebar(
            currentSort = SortOption.RATING_DESC,
            statistics = FavoriteStatistics(
                totalCount = 0,
                avgRating = 0.0,
                latestAdded = "-",
            ),
            onSortChange = {},
        )
    }
}
