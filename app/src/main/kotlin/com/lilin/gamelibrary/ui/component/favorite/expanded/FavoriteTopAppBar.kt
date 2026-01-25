package com.lilin.gamelibrary.ui.component.favorite.expanded

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.ui.theme.FavoriteGradientEnd
import com.lilin.gamelibrary.ui.theme.GameLibraryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteTopAppBar(
    title: String,
    itemCount: Int,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = "${itemCount}件",
                    style = MaterialTheme.typography.bodyMedium,
                    color = FavoriteGradientEnd,
                )
            }
        },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun FavoriteTopAppBarPreview() {
    GameLibraryTheme {
        FavoriteTopAppBar(
            title = "お気に入り",
            itemCount = 125,
        )
    }
}

@Preview
@Composable
private fun FavoriteTopAppBarEmptyPreview() {
    GameLibraryTheme {
        FavoriteTopAppBar(
            title = "お気に入り",
            itemCount = 0,
        )
    }
}

@Preview
@Composable
private fun FavoriteTopAppBarLargeCountPreview() {
    GameLibraryTheme {
        FavoriteTopAppBar(
            title = "お気に入り",
            itemCount = 9999,
        )
    }
}
