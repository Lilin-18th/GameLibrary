package com.lilin.gamelibrary.ui.component.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.ui.component.LabeledIcon

@Composable
fun SearchResultGridCard(
    game: Game,
    onClickItem: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClickItem,
        modifier = modifier
            .size(200.dp, 220.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            GameImageWithOverlay(game.imageUrl)

            GameInfoOverlay(
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

@Composable
private fun GameInfoOverlay(
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

@Preview(showBackground = true)
@Composable
private fun SearchResultGridCardPreview() {
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
    SearchResultGridCard(
        game = game,
        onClickItem = {},
    )
}

