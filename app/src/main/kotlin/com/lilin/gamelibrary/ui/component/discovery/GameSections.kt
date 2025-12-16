package com.lilin.gamelibrary.ui.component.discovery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.domain.model.Game

@Composable
fun TrendingGamesSection(
    games: List<Game>,
    onGameClick: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        GameSectionHeader(
            sectionType = SectionType.TRENDING,
            modifier = Modifier,
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(items = games, key = { it.id }) { game ->
                TrendingGameCard(
                    game = game,
                    onClick = { onGameClick(game) },
                )
            }
        }
    }
}

@Composable
fun HighRatedGamesSection(
    games: List<Game>,
    onGameClick: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        GameSectionHeader(
            sectionType = SectionType.HIGH_RATED,
            modifier = Modifier,
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(items = games, key = { it.id }) { game ->
                HighRatedGameCard(
                    game = game,
                    onClick = { onGameClick(game) },
                )
            }
        }
    }
}

@Composable
fun NewReleaseGamesSection(
    games: List<Game>,
    onGameClick: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        GameSectionHeader(
            sectionType = SectionType.NEW_RELEASE,
            modifier = Modifier,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            games.forEach { game ->
                NewReleaseGameCard(
                    game = game,
                    onClick = { onGameClick(game) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun TrendingGamesSectionPreview() {
    val sampleGames = listOf(
        Game(
            id = 1,
            name = "The Witcher 3: Wild Hunt",
            imageUrl = null,
            releaseDate = "2015-05-19",
            rating = 4.5,
            ratingsCount = 15234,
            metacritic = 92,
            isTba = false,
            addedCount = 50000,
            platforms = listOf("PC", "PS4", "Xbox One"),
        ),
        Game(
            id = 2,
            name = "Cyberpunk 2077",
            imageUrl = null,
            releaseDate = "2020-12-10",
            rating = 4.2,
            ratingsCount = 8765,
            metacritic = 86,
            isTba = false,
            addedCount = 30000,
            platforms = listOf("PC", "PS5", "Xbox Series X"),
        ),
    )

    TrendingGamesSection(
        games = sampleGames,
        onGameClick = {},
    )
}

@Preview
@Composable
private fun HighMetacriticGamesSectionPreview() {
    val sampleGames = listOf(
        Game(
            id = 1,
            name = "The Witcher 3: Wild Hunt",
            imageUrl = null,
            releaseDate = "2015-05-19",
            rating = 4.5,
            ratingsCount = 15234,
            metacritic = 92,
            isTba = false,
            addedCount = 50000,
            platforms = listOf("PC", "PS4", "Xbox One"),
        ),
        Game(
            id = 2,
            name = "Cyberpunk 2077",
            imageUrl = null,
            releaseDate = "2020-12-10",
            rating = 4.2,
            ratingsCount = 8765,
            metacritic = 55,
            isTba = false,
            addedCount = 30000,
            platforms = listOf("PC", "PS5", "Xbox Series X"),
        ),
    )

    HighRatedGamesSection(
        games = sampleGames,
        onGameClick = {},
    )
}

@Preview
@Composable
private fun NewReleaseGamesSectionPreview() {
    val sampleGames = listOf(
        Game(
            id = 1,
            name = "The Witcher 3: Wild Hunt",
            imageUrl = null,
            releaseDate = "2015-05-19",
            rating = 4.5,
            ratingsCount = 15234,
            metacritic = 92,
            isTba = false,
            addedCount = 50000,
            platforms = listOf("PC", "PS4", "Xbox One"),
        ),
        Game(
            id = 2,
            name = "Cyberpunk 2077",
            imageUrl = null,
            releaseDate = "2020-12-10",
            rating = 4.2,
            ratingsCount = 8765,
            metacritic = 55,
            isTba = false,
            addedCount = 30000,
            platforms = listOf("PC", "PS5", "Xbox Series X"),
        ),
    )

    NewReleaseGamesSection(
        games = sampleGames,
        onGameClick = {},
    )
}
