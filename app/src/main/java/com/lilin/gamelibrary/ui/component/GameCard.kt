package com.lilin.gamelibrary.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.lilin.gamelibrary.domain.model.isPreOrder
import com.lilin.gamelibrary.ui.theme.LoadingColor
import com.lilin.gamelibrary.ui.theme.RatingBronze
import com.lilin.gamelibrary.ui.theme.RatingEmpty
import com.lilin.gamelibrary.ui.theme.RatingGold
import com.lilin.gamelibrary.ui.theme.RatingSilver
import com.valentinilk.shimmer.shimmer

@Composable
fun TrendingGameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.width(dimensionResource(R.dimen.card_width)),
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
    ) {
        Column {
            if (game.imageUrl.isNullOrBlank()) {
                Image(
                    painter = painterResource(R.drawable.ic_broken_image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.image_height)),
                    contentScale = ContentScale.Crop,
                )
            } else {
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = game.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.image_height)),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp)
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
        modifier = modifier.width(dimensionResource(R.dimen.card_width)),
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.image_height)),
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
                game.metacriticScore?.let { score ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(40.dp)
                            .background(
                                color = getMetacriticColor(score),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = score.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp)
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
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
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
                    modifier = Modifier.size(dimensionResource(R.dimen.image_size)),
                    contentScale = ContentScale.Crop,
                )
            } else {
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = game.name,
                    modifier = Modifier.size(dimensionResource(R.dimen.image_size)),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
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
                        content = formatReleaseDate(it),
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

                Row {
                    if (game.isPreOrder()) {
                        LabeledIcon(
                            content = stringResource(R.string.preorder),
                            imageVector = Icons.Rounded.FiberNew,
                            contentDescription = "PreOrder",
                        )
                    }
                    if (game.isPopular) {
                        LabeledIcon(
                            content = stringResource(R.string.popular),
                            imageVector = Icons.Rounded.LocalFireDepartment,
                            contentDescription = "Popular",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameCardSkeleton(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .width(dimensionResource(R.dimen.card_width))
            .shimmer(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.image_height))
                    .background(LoadingColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(LoadingColor)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(LoadingColor)
                    )
                }
            }
        }
    }
}

@Composable
fun NewReleaseGameCardSkeleton(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.shimmer(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.image_size))
                    .background(LoadingColor)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(LoadingColor)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(LoadingColor)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(LoadingColor)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(LoadingColor)
                )
            }
        }
    }
}

@Composable
fun ErrorCard(
    throwable: Throwable,
    onClickRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(dimensionResource(R.dimen.image_height))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = throwable.message ?: "Unknown Error",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onClickRetry) {
                Text(
                    text = "Retry",
                    fontWeight = FontWeight.Bold,
                )
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

private fun formatReleaseDate(releaseDate: String): String {
    return releaseDate.replace("-", "/")
}

/**
 * 対象プラットフォームをフォーマット
 */
private fun formatPlatformData(platforms: List<String>): String {
    return platforms.joinToString("・")
}

/**
 * 数値をフォーマット（例：1234 → 1.2k）
 */
private fun formatCount(count: Int): String {
    return when {
        count >= 1000 -> String.format("%.1fk", count / 1000.0)
        else -> count.toString()
    }
}

/**
 * Metacriticスコアに応じた色を取得。
 */
private fun getMetacriticColor(score: Int): androidx.compose.ui.graphics.Color {
    return when {
        score >= 80 -> RatingGold
        score >= 70 -> RatingSilver
        score >= 50 -> RatingBronze
        else -> RatingEmpty
    }
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
        metacriticScore = 80,
        isTba = false,
        addedCount = 5000,
        platforms = listOf("Switch", "PS5", "PC")
    )
    TrendingGameCard(
        game = game,
        onClick = {},
    )
}

@Preview
@Composable
fun HighRatedGameCardPreview() {
    val game = Game(
        id = 1,
        name = "League of Legends",
        imageUrl = null,
        releaseDate = "2018-12-31",
        rating = 4.5,
        ratingsCount = 1523,
        metacriticScore = 80,
        isTba = false,
        addedCount = 5000,
        platforms = listOf("Switch", "PS5", "PC")
    )
    HighRatedGameCard(
        game = game,
        onClick = {},
    )
}

@Preview
@Composable
fun NewReleaseGameCardPreview() {
    val game = Game(
        id = 1,
        name = "League of Legends",
        imageUrl = null,
        releaseDate = "2028-12-31",
        rating = 4.5,
        ratingsCount = 1523,
        metacriticScore = 80,
        isTba = false,
        addedCount = 20000,
        platforms = listOf("Switch", "PS5", "PC")
    )
    NewReleaseGameCard(
        game = game,
        onClick = {},
    )
}

@Preview
@Composable
private fun GameCardSkeletonPreview() {
    GameCardSkeleton()
}

@Preview
@Composable
private fun NewReleaseGameCardSkeletonPreview() {
    NewReleaseGameCardSkeleton()
}

@Preview
@Composable
private fun ErrorCardPreview() {
    ErrorCard(
        throwable = Throwable("Unknown Error"),
        onClickRetry = {},
    )
}
