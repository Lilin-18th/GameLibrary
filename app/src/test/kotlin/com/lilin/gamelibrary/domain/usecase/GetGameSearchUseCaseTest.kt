package com.lilin.gamelibrary.domain.usecase

import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.domain.model.SearchResult
import com.lilin.gamelibrary.domain.repository.GameRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
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
    fun invoke_withValidQuery_returnsSearchResultWithGames() = runTest {
        val mockGames = listOf(
            createMockGame(1),
            createMockGame(2),
            createMockGame(3),
        )
        val mockSearchResult = SearchResult(
            games = mockGames,
            currentPage = 1,
            hasNextPage = true,
        )

        coEvery {
            gameRepository.getSearchGameResults(1, 10, "testGame")
        } returns Result.success(mockSearchResult)

        val response = useCase(1, 10, "testGame")

        assertTrue(response.isSuccess)
        val result = response.getOrNull()!!
        assertEquals(3, result.games.size)
        assertEquals(1, result.currentPage)
        assertTrue(result.hasNextPage)
        assertEquals("Test Game 1", result.games[0].name)
        assertEquals(1, result.games[0].id)

        coVerify(exactly = 1) {
            gameRepository.getSearchGameResults(1, 10, "testGame")
        }
    }

    @Test
    fun invoke_withLastPage_returnsSearchResultWithoutNextPage() = runTest {
        val mockGames = listOf(createMockGame(1))
        val mockSearchResult = SearchResult(
            games = mockGames,
            currentPage = 3,
            hasNextPage = false,
        )

        coEvery {
            gameRepository.getSearchGameResults(3, 10, "testGame")
        } returns Result.success(mockSearchResult)

        val response = useCase(3, 10, "testGame")

        assertTrue(response.isSuccess)
        val result = response.getOrNull()!!
        assertEquals(1, result.games.size)
        assertEquals(3, result.currentPage)
        assertFalse(result.hasNextPage)
    }

    @Test
    fun invoke_withEmptyResult_returnsEmptySearchResult() = runTest {
        val mockSearchResult = SearchResult(
            games = emptyList(),
            currentPage = 1,
            hasNextPage = false,
        )

        coEvery {
            gameRepository.getSearchGameResults(1, 10, "nonexistent")
        } returns Result.success(mockSearchResult)

        val response = useCase(1, 10, "nonexistent")

        assertTrue(response.isSuccess)
        val result = response.getOrNull()!!
        assertTrue(result.games.isEmpty())
        assertFalse(result.hasNextPage)
    }

    @Test
    fun invoke_withApiError_returnsFailureResult() = runTest {
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
