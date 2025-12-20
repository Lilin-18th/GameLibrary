package com.lilin.gamelibrary.ui.component.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.LocalOffer
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.GameDetail
import com.lilin.gamelibrary.ui.theme.DetailDescriptionGradientEnd
import com.lilin.gamelibrary.ui.theme.DetailDescriptionGradientStart
import com.lilin.gamelibrary.ui.theme.DetailRatingGradientEnd
import com.lilin.gamelibrary.ui.theme.DetailRatingGradientStart
import com.lilin.gamelibrary.ui.theme.DetailTagGradientEnd
import com.lilin.gamelibrary.ui.theme.DetailTagGradientStart

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
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        DetailSectionHeader(
            title = stringResource(R.string.detail_section_rating_title),
            icon = Icons.Rounded.Stars,
            gradientColors = DetailRatingGradientStart to DetailRatingGradientEnd,
            modifier = Modifier.fillMaxWidth(),
        )

        GameInfoCard(
            rating = gameDetail.rating,
            ratingsCount = gameDetail.ratingsCount,
            platforms = gameDetail.platformNames,
            genres = gameDetail.genres.map { it.name },
            releaseDate = gameDetail.releaseDate,
            esrbRating = gameDetail.esrbRating,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun GameDescriptionSection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        DetailSectionHeader(
            title = stringResource(R.string.detail_section_description_title),
            icon = Icons.Rounded.Description,
            gradientColors = DetailDescriptionGradientStart to DetailDescriptionGradientEnd,
            modifier = Modifier.fillMaxWidth(),
        )

        gameDetail.description?.let {
            GameDescription(
                description = it,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun GameTagsSection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    if (gameDetail.tags.isNotEmpty()) {
        Column(
            modifier = modifier.fillMaxWidth(),
        ) {
            DetailSectionHeader(
                title = stringResource(R.string.detail_section_tags_title),
                icon = Icons.Rounded.LocalOffer,
                gradientColors = DetailTagGradientStart to DetailTagGradientEnd,
            )

            GameTags(
                tags = gameDetail.tags.map { it.name },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
