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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Score
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.ui.component.LabeledIcon
import com.lilin.gamelibrary.ui.theme.FavoriteGradientStart

@Composable
fun FavoriteGameCard(
    game: FavoriteGame,
    onClick: (Int) -> Unit,
    onClickDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
        label = "search_card_press",
    )

    Card(
        modifier = modifier
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
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp)),
            ) {
                if (game.backgroundImage.isNullOrBlank()) {
                    Image(
                        painter = painterResource(R.drawable.ic_broken_image),
                        contentDescription = null,
                        modifier = Modifier.size(90.dp),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    AsyncImage(
                        model = game.backgroundImage,
                        contentDescription = game.name,
                        modifier = Modifier.size(90.dp),
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
                                    Color.Black.copy(alpha = 0.2f),
                                ),
                            ),
                        ),
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

                game.released?.let {
                    LabeledIcon(
                        content = it,
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = "Release Year",
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    LabeledIcon(
                        content = game.rating.toString(),
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Rating",
                    )

                    game.metacritic?.let { metacritic ->
                        LabeledIcon(
                            content = metacritic.toString(),
                            imageVector = Icons.Rounded.Score,
                            contentDescription = "Metacritic Score",
                        )
                    }
                }
            }

            IconButton(
                onClick = {
                    onClickDelete(game.id)
                },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(12.dp),
                    ),
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Preview
@Composable
private fun FavoriteGameListItemPreview() {
    val game = FavoriteGame(
        id = 1,
        name = "The Legend of Zelda: Breath of the Wild",
        backgroundImage = null,
        rating = 4.5,
        metacritic = 90,
        released = "2017-03-03",
        addedAt = System.currentTimeMillis(),
    )
    FavoriteGameCard(
        game = game,
        onClick = {},
        onClickDelete = {},
    )
}
