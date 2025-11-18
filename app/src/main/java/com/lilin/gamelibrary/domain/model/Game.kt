package com.lilin.gamelibrary.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class Game(
    // 基本情報
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val releaseDate: String?,
    // 評価情報
    val rating: Double,
    val ratingsCount: Int?,
    val metacriticScore: Int?,
    // その他
    val isTba: Boolean,
    val addedCount: Int?,
    val platforms: List<String>?,
) {
    val releaseYear: Int? = releaseDate?.let { dateString ->
        LocalDate.parse(dateString).year
    }

    // 注目バッジ判定
    val isPopular: Boolean = addedCount?.let { it >= POPULAR_THRESHOLD } ?: false

    // 予約受付中バッジ判定
    val isPreOrder: Boolean = if (isTba) {
        true
    } else {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        releaseDate?.let { dateString ->
            try {
                LocalDate.parse(dateString) > today
            } catch (e: Exception) {
                null
            }
        } ?: false
    }

    val displayRating: String = String.format("%.1f", rating)

    companion object {
        private const val POPULAR_THRESHOLD = 10000
    }
}