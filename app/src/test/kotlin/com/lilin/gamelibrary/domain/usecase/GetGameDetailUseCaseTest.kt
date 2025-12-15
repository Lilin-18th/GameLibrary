package com.lilin.gamelibrary.domain.usecase

import com.lilin.gamelibrary.domain.model.Developer
import com.lilin.gamelibrary.domain.model.GameDetail
import com.lilin.gamelibrary.domain.model.Genre
import com.lilin.gamelibrary.domain.model.Publisher
import com.lilin.gamelibrary.domain.model.Screenshot
import com.lilin.gamelibrary.domain.model.Tag
import com.lilin.gamelibrary.domain.repository.GameDetailRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetGameDetailUseCaseTest {
    lateinit var gameDetailRepository: GameDetailRepository
    lateinit var useCase: GetGameDetailUseCase

    @BeforeEach
    fun setUp() {
        gameDetailRepository = mockk()
        useCase = GetGameDetailUseCase(gameDetailRepository)
    }

    @Test
    fun test_GetGameDetailUseCase_success() = runTest {
        val gameId = 1
        val expectedGameDetail = createMockGameDetail(gameId)

        coEvery {
            gameDetailRepository.getGameDetail(gameId)
        } returns Result.success(expectedGameDetail)

        val result = useCase(gameId)

        assertTrue(result.isSuccess)
        assertEquals(expectedGameDetail, result.getOrNull())
        assertEquals("Test Game $gameId", result.getOrNull()?.name)
        coVerify(exactly = 1) {
            gameDetailRepository.getGameDetail(gameId)
        }
    }

    @Test
    fun test_GetGameDetailUseCase_failure() = runTest {
        val exception = kotlin.Exception("Network error")

        coEvery {
            gameDetailRepository.getGameDetail(any())
        } returns Result.failure(exception)

        val result = useCase(1)

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) {
            gameDetailRepository.getGameDetail(1)
        }
    }

    @Test
    fun test_GetGameDetailUseCase_calls_repository_with_correct_game_id() = runTest {
        val gameId = 12345
        val expectedGameDetail = createMockGameDetail(gameId)

        coEvery {
            gameDetailRepository.getGameDetail(gameId)
        } returns Result.success(expectedGameDetail)

        val result = useCase(gameId)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            gameDetailRepository.getGameDetail(gameId)
        }
    }

    @Test
    fun test_GetGameDetailUseCase_handles_repository_failure_gracefully() = runTest {
        val repositoryError = kotlin.RuntimeException("Database connection failed")

        coEvery {
            gameDetailRepository.getGameDetail(any())
        } returns Result.failure(repositoryError)

        val result = useCase(999)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
        assertEquals("Database connection failed", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) {
            gameDetailRepository.getGameDetail(999)
        }
    }

    @Test
    fun test_GetGameDetailUseCase_with_zero_game_id() = runTest {
        val expectedGameDetail = createMockGameDetail(0)

        coEvery {
            gameDetailRepository.getGameDetail(0)
        } returns Result.success(expectedGameDetail)

        val result = useCase(0)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            gameDetailRepository.getGameDetail(0)
        }
    }

    @Test
    fun test_GetGameDetailUseCase_with_negative_game_id() = runTest {
        val expectedGameDetail = createMockGameDetail(-1)

        coEvery {
            gameDetailRepository.getGameDetail(-1)
        } returns Result.success(expectedGameDetail)

        val result = useCase(-1)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            gameDetailRepository.getGameDetail(-1)
        }
    }

    private fun createMockGameDetail(gameId: Int): GameDetail {
        return GameDetail(
            id = gameId,
            name = "Test Game $gameId",
            backgroundImage = "https://example.com/image$gameId.jpg",
            releaseDate = "2024-01-01",
            metacritic = 85,
            rating = 4.5,
            ratingsCount = 1000,
            platformNames = listOf("PC", "PlayStation 5"),
            genres = listOf(
                Genre(id = 1, name = "Action", slug = "action"),
                Genre(id = 2, name = "Adventure", slug = "adventure"),
            ),
            description = "A test game description for game $gameId",
            shortScreenshots = listOf(
                Screenshot(id = 1, imageUrl = "https://example.com/screenshot1.jpg"),
                Screenshot(id = 2, imageUrl = "https://example.com/screenshot2.jpg"),
            ),
            developers = listOf(
                Developer(id = 1, name = "Test Developer"),
            ),
            publishers = listOf(
                Publisher(id = 1, name = "Test Publisher"),
            ),
            esrbRating = "Teen",
            playtime = 120,
            tags = listOf(
                Tag(id = 1, name = "Singleplayer", slug = "singleplayer"),
                Tag(id = 2, name = "Multiplayer", slug = "multiplayer"),
            ),
        )
    }
}
