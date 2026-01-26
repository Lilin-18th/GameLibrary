package com.lilin.gamelibrary.ui.component.favorite.expanded

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.ui.theme.FavoriteGradientEnd
import com.lilin.gamelibrary.ui.theme.GameLibraryTheme
import com.lilin.gamelibrary.ui.util.formatAvgRating

@Composable
fun FavoriteGameExpandedCard(
    game: FavoriteGame,
    onClick: (Int) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(game.id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
            ) {
                if (game.backgroundImage.isNullOrBlank()) {
                    Image(
                        painter = painterResource(R.drawable.ic_broken_image),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    AsyncImage(
                        model = game.backgroundImage,
                        contentDescription = game.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = FavoriteGradientEnd,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = game.rating.formatAvgRating(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = game.released ?: "未定",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "削除",
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FavoriteGameExpandedCardPreview() {
    GameLibraryTheme {
        FavoriteGameExpandedCard(
            game = FavoriteGame(
                id = 1,
                name = "Grand Theft Auto V",
                backgroundImage = null,
                rating = 4.5,
                metacritic = 97,
                released = "2013-09-17",
                addedAt = System.currentTimeMillis(),
            ),
            onClick = {},
            onDelete = {},
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun FavoriteGameExpandedCardLongTitlePreview() {
    GameLibraryTheme {
        FavoriteGameExpandedCard(
            game = FavoriteGame(
                id = 2,
                name = "The Legend of Zelda: Breath of the Wild - Complete Edition with All DLCs",
                backgroundImage = null,
                rating = 4.8,
                metacritic = 97,
                released = "2017-03-03",
                addedAt = System.currentTimeMillis(),
            ),
            onClick = {},
            onDelete = {},
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun FavoriteGameExpandedCardNoDataPreview() {
    GameLibraryTheme {
        FavoriteGameExpandedCard(
            game = FavoriteGame(
                id = 3,
                name = "Cyberpunk 2077",
                backgroundImage = null,
                rating = 0.0,
                metacritic = null,
                released = null,
                addedAt = System.currentTimeMillis(),
            ),
            onClick = {},
            onDelete = {},
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp),
        )
    }
}
