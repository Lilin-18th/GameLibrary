package com.lilin.gamelibrary.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.FiberNew
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.ui.theme.RatingBronze
import com.lilin.gamelibrary.ui.theme.RatingEmpty
import com.lilin.gamelibrary.ui.theme.RatingGold
import com.lilin.gamelibrary.ui.theme.RatingSilver
import java.util.Locale

private const val METACRITIC_GOLD_THRESHOLD = 90
private const val METACRITIC_SILVER_THRESHOLD = 70
private const val METACRITIC_BRONZE_THRESHOLD = 50

@Composable
fun TrendingGameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.width(168.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column {
            if (game.imageUrl.isNullOrBlank()) {
                Image(
                    painter = painterResource(R.drawable.ic_broken_image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop,
                )
            } else {
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = game.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LabeledIcon(
                        content = game.displayRating,
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Rating",
                    )

                    game.releaseYear?.let {
                        Spacer(modifier = Modifier.padding(10.dp))

                        LabeledIcon(
                            content = it.toString(),
                            imageVector = Icons.Rounded.CalendarMonth,
                            contentDescription = "Release Year",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HighRatedGameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.width(168.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
            ) {
                if (game.imageUrl.isNullOrBlank()) {
                    Image(
                        painter = painterResource(R.drawable.ic_broken_image),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    AsyncImage(
                        model = game.imageUrl,
                        contentDescription = game.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(40.dp)
                        .background(
                            color = getMetacriticColor(game.metacriticScore),
                            shape = RoundedCornerShape(8.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = game.metacriticScore.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
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
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                )

                LabeledIcon(
                    content = buildString {
                        append(game.displayRating)
                        game.ratingsCount?.let { count ->
                            append(" (${formatCount(count)})")
                        }
                    },
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "Rating",
                )

                game.platforms?.let {
                    LabeledIcon(
                        content = formatPlatformData(it),
                        imageVector = Icons.Rounded.Games,
                        contentDescription = "Platform",
                    )
                }

                game.releaseYear?.let {
                    LabeledIcon(
                        content = it.toString(),
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = "Release Year",
                    )
                }
            }
        }
    }
}

@Composable
fun NewReleaseGameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (game.imageUrl.isNullOrBlank()) {
                Image(
                    painter = painterResource(R.drawable.ic_broken_image),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp),
                    contentScale = ContentScale.Crop,
                )
            } else {
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = game.name,
                    modifier = Modifier.size(90.dp),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                )

                game.releaseDate?.let {
                    LabeledIcon(
                        content = it,
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = "Release Year",
                    )
                }

                game.platforms?.let {
                    LabeledIcon(
                        content = formatPlatformData(it),
                        imageVector = Icons.Rounded.Games,
                        contentDescription = "Platform",
                    )
                }
            }
        }
    }
}

@Composable
private fun LabeledIcon(
    content: String,
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

/**
 * Metacriticスコアに応じた色を取得。
 */
fun getMetacriticColor(score: Int): Color {
    return when {
        score >= METACRITIC_GOLD_THRESHOLD -> RatingGold
        score >= METACRITIC_SILVER_THRESHOLD -> RatingSilver
        score >= METACRITIC_BRONZE_THRESHOLD -> RatingBronze
        else -> RatingEmpty
    }
}

/**
 * 数値をフォーマット（例：1234 → 1.2k）
 */
fun formatCount(count: Int): String {
    return when {
        count >= 1000 -> {
            String.format(Locale.ROOT, "%.1fk", count / 1000.0)
        }
        else -> count.toString()
    }
}

/**
 * 対象プラットフォームをフォーマット
 */
fun formatPlatformData(platforms: List<String>): String {
    return platforms.joinToString("・")
}

@Preview
@Composable
private fun TrendingGameCardPreview() {
    val game = Game(
        id = 1,
        name = "League of Legends",
        imageUrl = null,
        releaseDate = "2018-12-31",
        rating = 4.5,
        ratingsCount = 1523,
        metacritic = 80,
        isTba = false,
        addedCount = 5000,
        platforms = listOf("Switch", "PS5", "PC"),
    )
    TrendingGameCard(
        game = game,
        onClick = {},
    )
}

@Preview
@Composable
private fun HighRatedGameCardPreview() {
    val game = Game(
        id = 1,
        name = "League of Legends",
        imageUrl = null,
        releaseDate = "2018-12-31",
        rating = 4.5,
        ratingsCount = 1523,
        metacritic = 80,
        isTba = false,
        addedCount = 5000,
        platforms = listOf("Switch", "PS5", "PC"),
    )
    HighRatedGameCard(
        game = game,
        onClick = {},
    )
}

@Preview
@Composable
private fun NewReleaseGameCardPreview() {
    val game = Game(
        id = 1,
        name = "League of Legends",
        imageUrl = null,
        releaseDate = "2028-12-31",
        rating = 4.5,
        ratingsCount = 1523,
        metacritic = 80,
        isTba = false,
        addedCount = 20000,
        platforms = listOf("Switch", "PS5", "PC"),
    )
    NewReleaseGameCard(
        game = game,
        onClick = {},
    )
}
