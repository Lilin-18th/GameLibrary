package com.lilin.gamelibrary.domain.usecase

import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.domain.repository.GameRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetHighRatedGamesUseCaseTest {
    private lateinit var gameRepository: GameRepository
    private lateinit var useCase: GetHighMetacriticScoreGamesUseCase

    @BeforeEach
    fun setUp() {
        gameRepository = mockk()
        useCase = GetHighMetacriticScoreGamesUseCase(gameRepository)
    }

    @Test
    fun test_GetHighMetacriticScoreGamesUseCase_success() = runTest {
        val mockGames = listOf(createMockGame(1), createMockGame(2))
        coEvery {
            gameRepository.getHighMetacriticScoreGames(any(), any())
        } returns Result.success(mockGames)

        val response = useCase(1, 2)

        assertTrue(response.isSuccess)
        assertEquals(2, response.getOrNull()?.size)
        coVerify(exactly = 1) {
            gameRepository.getHighMetacriticScoreGames(1, 2)
        }
    }

    @Test
    fun test_GetHighMetacriticScoreGamesUseCase_failure() = runTest {
        val exception = Exception("Api Error")
        coEvery {
            gameRepository.getHighMetacriticScoreGames(any(), any())
        } returns Result.failure(exception)

        val response = useCase(1, 5)

        assertTrue(response.isFailure)
        assertEquals("Api Error", response.exceptionOrNull()?.message)
    }

    private fun createMockGame(id: Int): Game {
        return Game(
            id = id,
            name = "Test Game $id",
            imageUrl = "https://example.com/image$id.jpg",
            releaseDate = "2024-01-01",
            rating = 4.5,
            ratingsCount = 1000,
            metacritic = 85,
            isTba = false,
            addedCount = 5000,
            platforms = listOf("PC"),
        )
    }
}
