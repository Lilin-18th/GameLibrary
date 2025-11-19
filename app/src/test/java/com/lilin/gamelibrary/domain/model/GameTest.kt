package com.lilin.gamelibrary.domain.model

import com.lilin.gamelibrary.domain.model.Game.Companion.POPULAR_THRESHOLD
import com.lilin.gamelibrary.domain.provider.DateTimeProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class GameTest {
    @ParameterizedTest
    @MethodSource("provideReleaseDatesAndYears")
    fun test_releaseYear(
        inputDate: String?,
        expectedYear: Int?,
    ) {
        // Given
        val game = baseGameModel.copy(releaseDate = inputDate)
        // When
        val actualYear = game.releaseYear
        // Then
        assertEquals(expectedYear, actualYear)
    }

    @ParameterizedTest
    @MethodSource("provideAddedCountsAndPopular")
    fun test_isPopular(
        inputAddedCount: Int?,
        expectedIsPopular: Boolean,
    ) {
        // Given
        val game = baseGameModel.copy(addedCount = inputAddedCount)
        // When (計算されるプロパティなので、実質的にWhenは不要)
        val actualIsPopular = game.isPopular
        // Then
        assertEquals(expectedIsPopular, actualIsPopular)
    }

    @ParameterizedTest
    @MethodSource("provideIsPreOrderConditions")
    fun test_isPreOrder(
        inputIsTba: Boolean,
        inputReleaseDate: String?,
        mockTodayDate: String,
        expectedIsPreOrder: Boolean,
    ) {
        // Given
        val mockDateTimeProvider = mockk<DateTimeProvider>()
        every { mockDateTimeProvider.today() } returns LocalDate.parse(mockTodayDate)
        
        val game = baseGameModel.copy(
            isTba = inputIsTba,
            releaseDate = inputReleaseDate,
        )
        
        // When
        val actualIsPreOrder = game.isPreOrder(mockDateTimeProvider)
        
        // Then
        assertEquals(expectedIsPreOrder, actualIsPreOrder)
    }

    companion object {
        private val baseGameModel = Game(
            id = 1,
            name = "Test Game",
            imageUrl = null,
            releaseDate = "2000-04-10",
            rating = 1.0,
            ratingsCount = null,
            metacriticScore = null,
            isTba = false,
            addedCount = null,
            platforms = null,
        )

        @JvmStatic
        fun provideReleaseDatesAndYears(): Stream<Arguments> {
            return Stream.of(
                // 正常なケース
                Arguments.of("2010-01-01", 2010),
                Arguments.of("2018-12-31", 2018),
                Arguments.of("2025-06-15", 2025),
                Arguments.of("1990-01-01", 1990),
                // エッジケース
                Arguments.of("2000-02-29", 2000), // うるう年
                Arguments.of("2100-01-01", 2100), // 将来の日付
                Arguments.of("1970-01-01", 1970), // Epoch開始日
                // 異常なケース
                Arguments.of(null, null), // null
                Arguments.of("", null), // 空文字
                Arguments.of("invalid-date", null), // 無効な形式
                Arguments.of("2023-13-01", null), // 存在しない月
                Arguments.of("2023-02-30", null), // 存在しない日
                Arguments.of("not-a-date", null), // 完全に無効な文字列
                Arguments.of("2023", null), // 不完全な形式
                Arguments.of("2023-01", null), // 不完全な形式
                Arguments.of("2023/01/01", null), // 異なる区切り文字
            )
        }

        @JvmStatic
        fun provideAddedCountsAndPopular(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(null, false),
                Arguments.of(0, false),
                Arguments.of(POPULAR_THRESHOLD - 1, false), // 9999
                Arguments.of(POPULAR_THRESHOLD, true), // 10000
                Arguments.of(POPULAR_THRESHOLD + 1, true), // 10001
                Arguments.of(20000, true)
            )
        }

        @JvmStatic
        fun provideIsPreOrderConditions(): Stream<Arguments> {
            return Stream.of(
                // isTba = trueの場合、常にfalse
                Arguments.of(true, "2024-01-01", "2025-01-01", false),
                Arguments.of(true, "2025-01-10", "2025-01-01", false),
                Arguments.of(true, null, "2025-01-01", false),
                
                // isTba = falseで将来の日付
                Arguments.of(false, "2025-06-01", "2025-01-01", true), // 将来の日付
                Arguments.of(false, "2030-12-31", "2025-01-01", true), // 遠い将来
                Arguments.of(false, "2025-01-02", "2025-01-01", true), // 1日後
                
                // isTba = falseで過去または今日の日付
                Arguments.of(false, "2024-12-31", "2025-01-01", false), // 過去
                Arguments.of(false, "2025-01-01", "2025-01-01", false), // 今日
                Arguments.of(false, "2020-01-01", "2025-01-01", false), // 遠い過去
                
                // 異常なケース
                Arguments.of(false, null, "2025-01-01", false), // null
                Arguments.of(false, "", "2025-01-01", false), // 空文字
                Arguments.of(false, "invalid-date", "2025-01-01", false), // 無効な形式
                Arguments.of(false, "2025/01/01", "2025-01-01", false), // 異なる区切り文字
            )
        }
    }
}