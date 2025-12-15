package com.lilin.gamelibrary.ui.component.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.lilin.gamelibrary.ui.component.LabeledIcon

@Composable
fun FavoriteGameListItem(
    game: FavoriteGame,
    onClick: (Int) -> Unit,
    onClickDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = { onClick(game.id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
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
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
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
    FavoriteGameListItem(
        game = game,
        onClick = {},
        onClickDelete = {},
    )
}
