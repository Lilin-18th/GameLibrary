package com.lilin.gamelibrary.domain.model

import com.lilin.gamelibrary.domain.provider.DateTimeProvider
import com.lilin.gamelibrary.domain.provider.DefaultDateTimeProvider
import kotlinx.datetime.LocalDate
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
    val metacritic: Int?,
    // その他
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

/**
 * ゲームの予約受付中判定
 *
 * 以下のいずれかの条件を満たす場合にtrueを返します：
 * - リリース日が現在の日付より未来の場合
 *
 * ゲームが発売日未定（TBA: To Be Announced）の場合やリリース日のパースに失敗した場合、リリース日がnullの場合はfalseを返します。
 *
 * @param dateTimeProvider 現在の日付を取得するためのプロバイダー。
 * デフォルトでは[DefaultDateTimeProvider]を使用します。テスト時にはモックを渡すことで固定日付での動作確認が可能です。
 *
 * @return ゲームが予約受付中の場合はtrue、それ以外はfalse
 */
fun Game.isPreOrder(
    dateTimeProvider: DateTimeProvider = DefaultDateTimeProvider(),
): Boolean {
    if (isTba) return false

    return releaseDate?.let { dateString ->
        try {
            val date = LocalDate.parse(dateString)
            date > dateTimeProvider.today()
        } catch (e: IllegalArgumentException) {
            println("Can not parse ${e.message}")
            false
        }
    } ?: false
}
