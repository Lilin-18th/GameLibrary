package com.lilin.gamelibrary.data.mapper

import com.lilin.gamelibrary.data.dto.GameDto
import com.lilin.gamelibrary.data.dto.GamesResponse
import com.lilin.gamelibrary.domain.model.Game

fun GameDto.toDomain(): Game {
    return Game(
        id = id,
        name = name,
        imageUrl = backgroundImage,
        releaseDate = released,
        rating = rating,
        ratingsCount = ratingsCount,
        metacritic = metacritic,
        isTba = tba ?: false,
        addedCount = added,
        platforms = platforms?.map { platformInfoDto ->
            platformInfoDto.toPlatformName()
        },
    )
}

fun GamesResponse.toDomainList(): List<Game> {
    return results.map { it.toDomain() }
}
