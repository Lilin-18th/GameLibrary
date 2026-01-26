package com.lilin.gamelibrary.ui.component.favorite.expanded

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.FavoriteStatistics
import com.lilin.gamelibrary.ui.theme.GameLibraryTheme
import com.lilin.gamelibrary.ui.util.formatAvgRatingWithStar

@Composable
fun StatisticsSection(
    statistics: FavoriteStatistics,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.favorite_statistics),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(12.dp))
        StatisticItem(
            label = stringResource(R.string.favorite_statistics_total),
            value = statistics.totalCount.toString(),
        )

        Spacer(modifier = Modifier.height(8.dp))
        StatisticItem(
            label = stringResource(R.string.favorite_statistics_average),
            value = statistics.avgRating.formatAvgRatingWithStar(),
        )

        Spacer(modifier = Modifier.height(8.dp))
        StatisticItem(
            label = stringResource(R.string.favorite_statistics_latest),
            value = statistics.latestAdded,
        )
    }
}

@Composable
private fun StatisticItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsSectionPreview() {
    GameLibraryTheme {
        StatisticsSection(
            statistics = FavoriteStatistics(
                totalCount = 125,
                avgRating = 4.6,
                latestAdded = "2日前",
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsSectionEmptyPreview() {
    GameLibraryTheme {
        StatisticsSection(
            statistics = FavoriteStatistics(
                totalCount = 0,
                avgRating = 0.0,
                latestAdded = "-",
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}
