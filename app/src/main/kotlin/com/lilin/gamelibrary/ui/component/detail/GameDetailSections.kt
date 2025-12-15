package com.lilin.gamelibrary.ui.component.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lilin.gamelibrary.domain.model.GameDetail

@Composable
fun GameBackgroundImageSection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    GameBackgroundImage(
        imageUrl = gameDetail.backgroundImage,
        contentDescription = gameDetail.name,
        metacriticScore = gameDetail.metacriticScore,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun GameBasicInfoSection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    GameBasicInfo(
        gameName = gameDetail.name,
        developers = gameDetail.developers.map {
            it.name
        },
        releaseYear = gameDetail.releaseDate.toString(),
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun GameRatingSummarySection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    GameInfoCard(
        rating = gameDetail.rating,
        ratingsCount = gameDetail.ratingsCount,
        platforms = gameDetail.platformNames,
        genres = gameDetail.genres.map { it.name },
        releaseDate = gameDetail.releaseDate,
        esrbRating = gameDetail.esrbRating,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun GameDescriptionSection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    gameDetail.description?.let {
        GameDescription(
            description = it,
            modifier = modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun GameTagsSection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    if (gameDetail.tags.isNotEmpty()) {
        GameTags(
            tags = gameDetail.tags.map { it.name },
            modifier = modifier.fillMaxWidth(),
        )
    }
}
