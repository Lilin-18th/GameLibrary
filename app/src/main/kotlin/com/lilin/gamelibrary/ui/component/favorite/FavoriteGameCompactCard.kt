package com.lilin.gamelibrary.ui.component.favorite

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.ui.component.LabeledIcon
import com.lilin.gamelibrary.ui.theme.FavoriteGradientStart
import com.lilin.gamelibrary.ui.theme.RatingBronze
import com.lilin.gamelibrary.ui.theme.RatingEmpty
import com.lilin.gamelibrary.ui.theme.RatingGold
import com.lilin.gamelibrary.ui.theme.RatingSilver

private const val METACRITIC_GOLD_THRESHOLD = 90
private const val METACRITIC_SILVER_THRESHOLD = 70
private const val METACRITIC_BRONZE_THRESHOLD = 50

@Composable
fun FavoriteGameCompactCard(
    game: FavoriteGame,
    onClick: (Int) -> Unit,
    onClickDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1.0f,
        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
        label = "favorite_card_press",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = FavoriteGradientStart.copy(alpha = 0.3f),
                ambientColor = FavoriteGradientStart.copy(alpha = 0.3f),
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        val released = tryAwaitRelease()
                        isPressed = false
                        if (released) {
                            onClick(game.id)
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
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
            ) {
                if (game.backgroundImage.isNullOrBlank()) {
                    Image(
                        painter = painterResource(R.drawable.ic_broken_image),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    AsyncImage(
                        model = game.backgroundImage,
                        contentDescription = game.name,
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop,
                    )
                }

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f),
                                ),
                            ),
                        ),
                )

                game.metacritic?.let { metacritic ->
                    val badgeColor = when {
                        metacritic >= METACRITIC_GOLD_THRESHOLD -> RatingGold
                        metacritic >= METACRITIC_SILVER_THRESHOLD -> RatingSilver
                        metacritic >= METACRITIC_BRONZE_THRESHOLD -> RatingBronze
                        else -> RatingEmpty
                    }

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = badgeColor,
                    ) {
                        Text(
                            text = metacritic.toString(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    game.released?.let {
                        LabeledIcon(
                            content = it,
                            imageVector = Icons.Rounded.CalendarMonth,
                            contentDescription = "Release Date",
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        LabeledIcon(
                            content = String.format(
                                Locale.current.platformLocale,
                                "%.1f",
                                game.rating,
                            ),
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "Rating",
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.errorContainer,
                                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
                            ),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    onClick = { onClickDelete(game.id) },
                    modifier = Modifier.size(48.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteGameCompactCardPreview() {
    val game = FavoriteGame(
        id = 1,
        name = "The Legend of Zelda: Breath of the Wild",
        backgroundImage = null,
        rating = 4.64,
        metacritic = 92,
        released = "2017-03-03",
        addedAt = System.currentTimeMillis(),
    )
    FavoriteGameCompactCard(
        game = game,
        onClick = {},
        onClickDelete = {},
    )
}

@Preview(showBackground = true, name = "Low Score")
@Composable
private fun FavoriteGameCompactCardLowScorePreview() {
    val game = FavoriteGame(
        id = 2,
        name = "Low Score Game with Very Long Title That Should Be Truncated",
        backgroundImage = null,
        rating = 2.5,
        metacritic = 45,
        released = "2023-12-01",
        addedAt = System.currentTimeMillis(),
    )
    FavoriteGameCompactCard(
        game = game,
        onClick = {},
        onClickDelete = {},
    )
}
