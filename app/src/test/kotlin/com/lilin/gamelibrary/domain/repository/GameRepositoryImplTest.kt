package com.lilin.gamelibrary.domain.repository

import com.lilin.gamelibrary.data.api.ApiServices
import com.lilin.gamelibrary.data.dto.GameDto
import com.lilin.gamelibrary.data.dto.GamesResponse
import com.lilin.gamelibrary.data.dto.PlatformDto
import com.lilin.gamelibrary.data.dto.PlatformInfoDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class GameRepositoryImplTest {
    private lateinit var apiServices: ApiServices
    private lateinit var gameRepository: GameRepositoryImpl

    @BeforeEach
    fun setUp() {
        apiServices = mockk()
        gameRepository = GameRepositoryImpl(apiServices)
    }

    // getTrendingGames テスト
    @Test
    fun getTrendingGames_withValidParameters_returnsSuccessResult() = runTest {
        val mockResponse = createMockGamesResponse(3)

        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-added",
            )
        } returns Response.success(mockResponse)

        val result = gameRepository.getTrendingGames(page = 1, pageSize = 2)

        assertTrue(result.isSuccess)
        assertEquals(3, result.getOrNull()?.size)

        coVerify(exactly = 1) {
            apiServices.getGames(
                page = 1,
                pageSize = 2,
                dates = any(),
                search = any(),
                ordering = "-added",
            )
        }
    }

    @Test
    fun getTrendingGames_withNullBody_returnsEmptyList() = runTest {
        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-added",
            )
        } returns Response.success(null)

        val response = gameRepository.getTrendingGames(page = 1, pageSize = 2)

        assertTrue(response.isSuccess)
        assertTrue(response.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun getTrendingGames_withApiError_returnsFailureResult() = runTest {
        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-added",
            )
        } returns Response.error(404, "Not Found".toResponseBody())

        val response = gameRepository.getTrendingGames(page = 1, pageSize = 2)

        assertTrue(response.isFailure)
        assertTrue(response.exceptionOrNull()?.message?.contains("API Error: 404") == true)
    }

    @Test
    fun getTrendingGames_withNetworkException_returnsFailureResult() = runTest {
        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-added",
            )
        } throws Exception("Network Error")

        val response = gameRepository.getTrendingGames(page = 1, pageSize = 2)

        assertTrue(response.isFailure)
        assertEquals("Network Error", response.exceptionOrNull()?.message)
    }

    // getHighRatedGames テスト
    @Test
    fun getHighMetacriticScoreGames_withValidParameters_returnsSuccessResult() = runTest {
        val mockResponse = createMockGamesResponse(5)

        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-metacritic",
            )
        } returns Response.success(mockResponse)

        val response = gameRepository.getHighMetacriticScoreGames(page = 1, pageSize = 5)

        assertTrue(response.isSuccess)
        assertEquals(5, response.getOrNull()?.size)

        coVerify(exactly = 1) {
            apiServices.getGames(
                page = 1,
                pageSize = 5,
                dates = any(),
                search = any(),
                ordering = "-metacritic",
            )
        }
    }

    @Test
    fun getHighMetacriticScoreGames_withNullBody_returnsEmptyList() = runTest {
        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-metacritic",
            )
        } returns Response.success(null)

        val response = gameRepository.getHighMetacriticScoreGames(page = 1, pageSize = 2)

        assertTrue(response.isSuccess)
        assertTrue(response.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun getHighMetacriticScoreGames_withApiError_returnsFailureResult() = runTest {
        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-metacritic",
            )
        } returns Response.error(404, "Not Found".toResponseBody())

        val response = gameRepository.getHighMetacriticScoreGames(page = 1, pageSize = 2)

        assertTrue(response.isFailure)
        assertTrue(response.exceptionOrNull()?.message?.contains("API Error: 404") == true)
    }

    @Test
    fun getHighMetacriticScoreGames_withTimeoutException_returnsFailureResult() = runTest {
        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-metacritic",
            )
        } throws Exception("Timeout")

        val response = gameRepository.getHighMetacriticScoreGames(page = 1, pageSize = 2)

        assertTrue(response.isFailure)
        assertEquals("Timeout", response.exceptionOrNull()?.message)
    }

    // getNewReleases テスト
    @Test
    fun getNewReleases_withValidParameters_returnsSuccessResult() = runTest {
        val mockResponse = createMockGamesResponse(4)

        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-released",
            )
        } returns Response.success(mockResponse)

        val response = gameRepository.getNewReleases(page = 1, pageSize = 4)

        assertTrue(response.isSuccess)
        assertEquals(4, response.getOrNull()?.size)

        coVerify(exactly = 1) {
            apiServices.getGames(
                page = 1,
                pageSize = 4,
                dates = any(),
                search = any(),
                ordering = "-released",
            )
        }
    }

    @Test
    fun getNewReleases_withNullBody_returnsEmptyList() = runTest {
        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-released",
            )
        } returns Response.success(null)

        val response = gameRepository.getNewReleases(page = 1, pageSize = 2)

        assertTrue(response.isSuccess)
        assertTrue(response.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun getNewReleases_withApiError_returnsFailureResult() = runTest {
        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-released",
            )
        } returns Response.error(404, "Not Found".toResponseBody())

        val response = gameRepository.getNewReleases(page = 1, pageSize = 2)

        assertTrue(response.isFailure)
        assertTrue(response.exceptionOrNull()?.message?.contains("API Error: 404") == true)
    }

    @Test
    fun getNewReleases_withConnectionException_returnsFailureResult() = runTest {
        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = any(),
                ordering = "-released",
            )
        } throws Exception("Connection refused")

        val response = gameRepository.getNewReleases(page = 1, pageSize = 2)

        assertTrue(response.isFailure)
        assertEquals("Connection refused", response.exceptionOrNull()?.message)
    }

    @Test
    fun getSearchGameResults_withValidQuery_returnsSearchResultWithGames() = runTest {
        val mockResponse = createMockGamesResponse(7, hasNext = true)

        coEvery {
            apiServices.getGames(
                page = 1,
                pageSize = 10,
                dates = any(),
                search = "test",
                ordering = "relevance,-metacritic",
            )
        } returns Response.success(mockResponse)

        val response =
            gameRepository.getSearchGameResults(page = 1, pageSize = 10, searchText = "test")

        assertTrue(response.isSuccess)
        val result = response.getOrNull()!!
        assertEquals(7, result.games.size)
        assertEquals(1, result.currentPage)
        assertTrue(result.hasNextPage)

        coVerify(exactly = 1) {
            apiServices.getGames(
                page = 1,
                pageSize = 10,
                dates = any(),
                search = "test",
                ordering = "relevance,-metacritic",
            )
        }
    }

    @Test
    fun getSearchGameResults_withLastPage_returnsSearchResultWithoutNextPage() = runTest {
        val mockResponse = createMockGamesResponse(5, hasNext = false)

        coEvery {
            apiServices.getGames(
                page = 2,
                pageSize = 10,
                dates = any(),
                search = "test",
                ordering = "relevance,-metacritic",
            )
        } returns Response.success(mockResponse)

        val response =
            gameRepository.getSearchGameResults(page = 2, pageSize = 10, searchText = "test")

        assertTrue(response.isSuccess)
        val result = response.getOrNull()!!
        assertEquals(5, result.games.size)
        assertEquals(2, result.currentPage)
        assertFalse(result.hasNextPage)
    }

    @Test
    fun getSearchGameResults_withNullBody_returnsEmptySearchResult() = runTest {
        coEvery {
            apiServices.getGames(
                page = 1,
                pageSize = 10,
                dates = any(),
                search = "test",
                ordering = "relevance,-metacritic",
            )
        } returns Response.success(null)

        val response =
            gameRepository.getSearchGameResults(page = 1, pageSize = 10, searchText = "test")

        assertTrue(response.isSuccess)
        val result = response.getOrNull()!!
        assertTrue(result.games.isEmpty())
        assertEquals(1, result.currentPage)
        assertFalse(result.hasNextPage)
    }

    @Test
    fun getSearchGameResults_withApiError_returnsFailureResult() = runTest {
        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = "test",
                ordering = "relevance,-metacritic",
            )
        } returns Response.error(404, "Not Found".toResponseBody())

        val response =
            gameRepository.getSearchGameResults(page = 1, pageSize = 10, searchText = "test")

        assertTrue(response.isFailure)
        assertTrue(response.exceptionOrNull()?.message?.contains("API Error: 404") == true)
    }

    @Test
    fun getSearchGameResults_withNetworkException_returnsFailureResult() = runTest {
        coEvery {
            apiServices.getGames(
                page = any(),
                pageSize = any(),
                dates = any(),
                search = "test",
                ordering = "relevance,-metacritic",
            )
        } throws Exception("Network Error")

        val response =
            gameRepository.getSearchGameResults(page = 1, pageSize = 10, searchText = "test")

        assertTrue(response.isFailure)
        assertEquals("Network Error", response.exceptionOrNull()?.message)
    }

    private fun createMockGamesResponse(count: Int, hasNext: Boolean = false): GamesResponse {
        val games = (1..count).map { createMockGameDto(it) }
        return GamesResponse(
            count = count,
            next = if (hasNext) "https://api.rawg.io/api/games?page=2" else null,
            previous = null,
            results = games,
        )
    }

    private fun createMockGameDto(id: Int): GameDto {
        return GameDto(
            id = id,
            slug = "test-game-$id",
            name = "Test Game $id",
            released = "2024-01-01",
            tba = false,
            backgroundImage = "https://example.com/image$id.jpg",
            rating = 4.5,
            ratingTop = 5,
            ratings = null,
            ratingsCount = 1000,
            reviewsTextCount = 100,
            added = 5000,
            addedByStatus = null,
            metacritic = 85,
            playtime = 10,
            suggestionsCount = 100,
            esrbRating = null,
            platforms = listOf(
                PlatformInfoDto(
                    platform = PlatformDto(
                        id = 1,
                        name = "PC",
                        slug = "pc",
                    ),
                    releasedAt = "2024-01-01",
                    requirements = null,
                ),
            ),
        )
    }
}
