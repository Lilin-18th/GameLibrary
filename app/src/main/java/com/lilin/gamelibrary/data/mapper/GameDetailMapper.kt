package com.lilin.gamelibrary.data.mapper

import com.lilin.gamelibrary.data.dto.GameDetailResponse
import com.lilin.gamelibrary.domain.model.GameDetail

fun GameDetailResponse.toDomain(): GameDetail {
    return GameDetail(
        id = id,
        name = name,
        backgroundImage = backgroundImage,
        releaseDate = released,
        metacritic = metacritic,
        rating = rating,
        ratingsCount = ratingsCount,
        platformNames = platforms.map { it.toPlatformName() },
        genres = genres.map { it.toGenre() },
        description = descriptionRaw,
        shortScreenshots = shortScreenshots.map { it.toScreenshot() },
        developers = developers.map { it.toDeveloper() },
        publishers = publishers.map { it.toPublisher() },
        esrbRating = esrbRating?.name,
        playtime = playtime,
        tags = tags.map { it.toTag() },
    )
}
