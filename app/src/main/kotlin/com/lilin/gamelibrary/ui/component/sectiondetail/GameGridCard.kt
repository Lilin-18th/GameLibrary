@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.lilin.gamelibrary.ui.component.sectiondetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.feature.sectiondetail.DisplayMode
import com.lilin.gamelibrary.ui.component.LabeledIcon
import com.lilin.gamelibrary.ui.component.discovery.SectionType
import com.lilin.gamelibrary.ui.theme.HighRatedGradientEnd
import com.lilin.gamelibrary.ui.theme.HighRatedGradientStart
import com.lilin.gamelibrary.ui.theme.NewReleaseGradientEnd
import com.lilin.gamelibrary.ui.theme.NewReleaseGradientStart
import com.lilin.gamelibrary.ui.theme.RatingBronze
import com.lilin.gamelibrary.ui.theme.RatingEmpty
import com.lilin.gamelibrary.ui.theme.RatingGold
import com.lilin.gamelibrary.ui.theme.RatingSilver
import com.lilin.gamelibrary.ui.theme.TrendingGradientEnd
import com.lilin.gamelibrary.ui.theme.TrendingGradientStart
import java.util.Locale

private const val METACRITIC_GOLD_THRESHOLD = 90
private const val METACRITIC_SILVER_THRESHOLD = 70
private const val METACRITIC_BRONZE_THRESHOLD = 50

@Composable
fun TrendingGameCard(
    game: Game,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    with(sharedTransitionScope) {
        Card(
            onClick = onClick,
            modifier = modifier
                .size(200.dp, 220.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = TrendingGradientStart.copy(alpha = 0.3f),
                    spotColor = TrendingGradientStart.copy(alpha = 0.3f),
                )
                .sharedElement(
                    rememberSharedContentState(key = "game-${game.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(0.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                GameImageWithOverlay(game.imageUrl)

                TrendingBadge(
                    badgeText = stringResource(R.string.discover_trend_card_badge),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                )

                TrendingGameInfoOverlay(
                    name = game.name,
                    rating = game.displayRating,
                    releaseYear = game.releaseYear?.toString(),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                )
            }
        }
    }
}

@Composable
fun HighRatedGameCard(
    game: Game,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    with(sharedTransitionScope) {
        Card(
            onClick = onClick,
            modifier = modifier
                .size(200.dp, 220.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = HighRatedGradientStart.copy(alpha = 0.3f),
                    spotColor = HighRatedGradientStart.copy(alpha = 0.3f),
                )
                .sharedElement(
                    rememberSharedContentState(key = "game-${game.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(0.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                GameImageWithOverlay(
                    imageUrl = game.imageUrl,
                    modifier = Modifier.fillMaxSize(),
                )

                HighRatedBadge(
                    badgeText = stringResource(R.string.discover_high_rated_card_badge),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                )

                MetacriticBadge(
                    metaScore = game.metacriticScore,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                )

                HighRatedGameInfoOverlay(
                    name = game.name,
                    rating = game.displayRating,
                    ratingsCount = game.ratingsCount,
                    platforms = game.platforms,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                )
            }
        }
    }
}

@Composable
fun NewReleaseGameCard(
    game: Game,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    with(sharedTransitionScope) {
        Card(
            onClick = onClick,
            modifier = modifier
                .width(200.dp)
                .height(220.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = NewReleaseGradientStart.copy(alpha = 0.3f),
                    spotColor = NewReleaseGradientStart.copy(alpha = 0.3f),
                )
                .sharedElement(
                    rememberSharedContentState(key = "game-${game.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(0.dp),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                GameImageWithOverlay(game.imageUrl)

                NewReleaseBadge(
                    badgeText = stringResource(R.string.discover_new_release_card_badge),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                )

                NewReleaseGameInfoOverlay(
                    name = game.name,
                    releaseDate = game.releaseDate,
                    platforms = game.platforms,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun GameImageWithOverlay(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        if (imageUrl.isNullOrBlank()) {
            Image(
                painter = painterResource(R.drawable.ic_broken_image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f),
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY,
                    ),
                ),
        )
    }
}

@Composable
private fun TrendingBadge(
    badgeText: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(60.dp)
            .height(28.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        TrendingGradientStart,
                        TrendingGradientEnd,
                    ),
                ),
                shape = RoundedCornerShape(6.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = badgeText,
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun HighRatedBadge(
    badgeText: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(72.dp)
            .height(28.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        HighRatedGradientStart,
                        HighRatedGradientEnd,
                    ),
                ),
                shape = RoundedCornerShape(6.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = badgeText,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

@Composable
private fun NewReleaseBadge(
    badgeText: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(52.dp)
            .height(28.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        NewReleaseGradientStart,
                        NewReleaseGradientEnd,
                    ),
                ),
                shape = RoundedCornerShape(6.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = badgeText,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

@Composable
private fun MetacriticBadge(
    metaScore: Int,
    modifier: Modifier = Modifier,
) {
    val badgeColor = getMetacriticColor(metaScore)

    Box(
        modifier = modifier
            .size(56.dp, 56.dp)
            .background(
                color = badgeColor,
                shape = RoundedCornerShape(12.dp),
            )
            .shadow(
                elevation = 8.dp,
                ambientColor = badgeColor.copy(alpha = 0.3f),
                spotColor = badgeColor.copy(alpha = 0.3f),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = metaScore.toString(),
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

@Composable
private fun TrendingGameInfoOverlay(
    name: String,
    rating: String,
    releaseYear: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LabeledIcon(
                content = rating,
                imageVector = Icons.Rounded.Star,
                contentDescription = "Rating",
                tint = Color.White.copy(alpha = 0.9f),
            )
            releaseYear?.let { year ->
                LabeledIcon(
                    content = year,
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = "Release Year",
                    tint = Color.White.copy(alpha = 0.9f),
                )
            }
        }
    }
}

@Composable
private fun HighRatedGameInfoOverlay(
    name: String,
    rating: String,
    ratingsCount: Int?,
    platforms: List<String>?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            LabeledIcon(
                content = buildString {
                    append(rating)
                    ratingsCount?.let { count ->
                        append(" (${formatCount(count)})")
                    }
                },
                imageVector = Icons.Rounded.Star,
                contentDescription = "Rating",
                tint = Color.White.copy(alpha = 0.9f),
            )
            platforms?.let { platforms ->
                LabeledIcon(
                    content = platforms.joinToString("・"),
                    imageVector = Icons.Rounded.Games,
                    contentDescription = "Platform",
                    tint = Color.White.copy(alpha = 0.9f),
                )
            }
        }
    }
}

@Composable
private fun NewReleaseGameInfoOverlay(
    name: String,
    releaseDate: String?,
    platforms: List<String>?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            releaseDate?.let { date ->
                LabeledIcon(
                    content = date,
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = "Release Date",
                    tint = Color.White.copy(alpha = 0.9f),
                )
            }

            platforms?.let { platforms ->
                LabeledIcon(
                    content = platforms.joinToString("・"),
                    imageVector = Icons.Rounded.Games,
                    contentDescription = "Platform",
                    tint = Color.White.copy(alpha = 0.9f),
                )
            }
        }
    }
}

@Composable
fun SeeMoreCard(
    sectionType: SectionType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.size(200.dp, 220.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = sectionType.gradientStart,
                )

                Text(
                    text = stringResource(R.string.discover_game_card_see_more),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = stringResource(sectionType.titleId),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
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
    SharedTransitionLayout {
        AnimatedContent(
            targetState = DisplayMode.GRID_COLUMN,
        ) {
            TrendingGameCard(
                game = game,
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this@AnimatedContent,
                onClick = {},
            )
        }
    }
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
        metacritic = 90,
        isTba = false,
        addedCount = 5000,
        platforms = listOf("Switch", "PS5", "PC"),
    )
    SharedTransitionLayout {
        AnimatedContent(
            targetState = DisplayMode.GRID_COLUMN,
        ) {
            HighRatedGameCard(
                game = game,
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this@AnimatedContent,
                onClick = {},
            )
        }
    }
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
    SharedTransitionLayout {
        AnimatedContent(
            targetState = DisplayMode.GRID_COLUMN,
        ) {
            NewReleaseGameCard(
                game = game,
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this@AnimatedContent,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun SeeMoreCardPreview() {
    SeeMoreCard(
        sectionType = SectionType.TRENDING,
        onClick = {},
    )
}
