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

class GetGameSearchUseCaseTest {
    private lateinit var gameRepository: GameRepository
    private lateinit var useCase: GetGameSearchUseCase

    @BeforeEach
    fun setUp() {
        gameRepository = mockk()
        useCase = GetGameSearchUseCase(gameRepository)
    }

    @Test
    fun invoke_withValidQuery_returnsListOfGames() =
        runTest {
            val mockResponse = listOf(
                createMockGame(1),
                createMockGame(2),
                createMockGame(3),
            )

            coEvery {
                gameRepository.getSearchGameResults(1, 10, "testGame")
            } returns Result.success(mockResponse)

            val response = useCase(1, 10, "testGame")
            assertTrue(response.isSuccess)
            assertEquals(3, response.getOrNull()?.size)
            assertEquals("Test Game 1", response.getOrNull()?.get(0)?.name)
            assertEquals(1, response.getOrNull()?.get(0)?.id)
            coVerify(exactly = 1) {
                gameRepository.getSearchGameResults(1, 10, "testGame")
            }
        }

    @Test
    fun invoke_withApiError_returnsFailureResult() =
        runTest {
            val exception = kotlin.Exception("Api Error")
            coEvery {
                gameRepository.getSearchGameResults(1, 10, "testGame")
            } returns Result.failure(exception)

            val response = useCase(1, 10, "testGame")

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
