package com.lilin.gamelibrary.ui.component.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material.icons.rounded.MilitaryTech
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.ui.component.LabeledIcon

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = stringResource(R.string.search_field_hit),
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "Clear text",
                    )
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                keyboardController?.hide()
            },
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
fun SearchResultCard(
    game: Game,
    onClickItem: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = onClickItem,
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
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    LabeledIcon(
                        imageVector = Icons.Rounded.Star,
                        content = game.rating.toString(),
                        contentDescription = "Rating",
                    )

                    LabeledIcon(
                        imageVector = Icons.Rounded.MilitaryTech,
                        content = game.metacriticScore.toString(),
                        contentDescription = "MetacriticScore",
                    )
                }

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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchFieldPreview() {
    SearchField(
        query = "The Witcher",
        onQueryChange = {},
        onSearch = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun NoInputSearchFieldPreview() {
    SearchField(
        query = "",
        onQueryChange = {},
        onSearch = {},
    )
}

@Preview
@Composable
private fun SearchResultCardPreview() {
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
    SearchResultCard(
        game = game,
        onClickItem = {},
    )
}
