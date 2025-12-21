package com.lilin.gamelibrary.ui.component.search

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.ui.component.LabeledIcon
import com.lilin.gamelibrary.ui.theme.RatingBronze
import com.lilin.gamelibrary.ui.theme.RatingEmpty
import com.lilin.gamelibrary.ui.theme.RatingGold
import com.lilin.gamelibrary.ui.theme.RatingSilver
import java.util.Locale

private const val METACRITIC_GOLD_THRESHOLD = 90
private const val METACRITIC_SILVER_THRESHOLD = 70
private const val METACRITIC_BRONZE_THRESHOLD = 50

@Composable
fun SearchResultCard(
    game: Game,
    onClickItem: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.05f else 1.0f,
        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
        label = "search_card_press",
    )

    Card(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0xFF7C4DFF).copy(alpha = 0.3f),
                ambientColor = Color(0xFF7C4DFF).copy(alpha = 0.3f),
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        val released = tryAwaitRelease()
                        isPressed = false
                        if (released) {
                            // リリース時にクリックイベントを発火
                            onClickItem()
                        }
                    },
                )
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier.size(90.dp),
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
                        modifier = Modifier
                            .size(90.dp)
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.5f),
                                        ),
                                        startY = 0f,
                                        endY = size.height,
                                    ),
                                )
                            },
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                LabeledIcon(
                    imageVector = Icons.Rounded.Star,
                    content = buildString {
                        append(game.rating.toString())
                        game.ratingsCount?.let { count ->
                            append(" (${formatCount(count)})")
                        }
                    },
                    contentDescription = "Rating",
                )

                game.releaseDate?.let {
                    LabeledIcon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        content = it,
                        contentDescription = "Release Date",
                    )
                }

                game.platforms?.let {
                    LabeledIcon(
                        content = it.joinToString("・"),
                        imageVector = Icons.Rounded.Games,
                        contentDescription = "Platform",
                    )
                }
            }

            MetacriticBadge(
                metaScore = game.metacriticScore,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
    }
}

@Composable
fun SearchNoResultState(
    query: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF26C6DA),
            )

            Text(
                text = stringResource(R.string.search_no_result_title, query),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )

            Text(
                text = stringResource(R.string.search_no_result_suggestion),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun SearchErrorState(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error,
            )

            Text(
                text = stringResource(R.string.search_error_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
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
            .size(56.dp)
            .background(
                color = badgeColor,
                shape = RoundedCornerShape(12.dp),
            )
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = badgeColor.copy(alpha = 0.3f),
                spotColor = badgeColor.copy(alpha = 0.3f),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = metaScore.toString(),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
        )
    }
}

/**
 * Metacriticスコアに応じた色を取得（Discovery画面と同じロジック）
 */
private fun getMetacriticColor(score: Int): Color {
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
private fun formatCount(count: Int): String {
    return when {
        count >= 1000 -> {
            String.format(Locale.ROOT, "%.1fk", count / 1000.0)
        }
        else -> count.toString()
    }
}

@Preview
@Composable
private fun SearchResultCardHighScorePreview() {
    val game = Game(
        id = 1,
        name = "The Witcher 3: Wild Hunt",
        imageUrl = null,
        releaseDate = "2015-05-19",
        rating = 4.5,
        ratingsCount = 12345,
        metacritic = 92,
        isTba = false,
        addedCount = 20000,
        platforms = listOf("PS5", "Xbox", "PC"),
    )
    SearchResultCard(
        game = game,
        onClickItem = {},
    )
}

@Preview
@Composable
private fun SearchResultCardMidScorePreview() {
    val game = Game(
        id = 2,
        name = "God of War",
        imageUrl = null,
        releaseDate = "2018-04-20",
        rating = 4.3,
        ratingsCount = 8765,
        metacritic = 75,
        isTba = false,
        addedCount = 15000,
        platforms = listOf("PS5", "PS4"),
    )
    SearchResultCard(
        game = game,
        onClickItem = {},
    )
}

@Preview
@Composable
private fun SearchResultCardNoMetacriticPreview() {
    val game = Game(
        id = 3,
        name = "Indie Game Example",
        imageUrl = null,
        releaseDate = "2023-12-01",
        rating = 3.8,
        ratingsCount = 234,
        metacritic = null,
        isTba = false,
        addedCount = 500,
        platforms = listOf("PC"),
    )
    SearchResultCard(
        game = game,
        onClickItem = {},
    )
}
