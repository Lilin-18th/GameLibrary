package com.lilin.gamelibrary.domain.model

import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class Game(
    // 基本情報
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val releaseDate: String?,
    val rating: Double,
    val ratingsCount: Int?,
    val metacritic: Int?,
    val isTba: Boolean,
    val addedCount: Int?,
    val platforms: List<String>?,
) {
    val releaseYear: Int? = releaseDate?.let { dateString ->
        try {
            LocalDate.parse(dateString).year
        } catch (e: IllegalArgumentException) {
            println("Can not parse ${e.message}")
            null
        }
    }
    val metacriticScore = metacritic ?: 0
    val isPopular: Boolean = addedCount?.let { it >= POPULAR_THRESHOLD } ?: false
    val displayRating: String = "%.1f".format(rating)

    companion object {
        const val POPULAR_THRESHOLD = 10000
    }
}
