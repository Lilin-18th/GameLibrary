package com.lilin.gamelibrary.domain.repository

import com.lilin.gamelibrary.data.api.ApiServices
import com.lilin.gamelibrary.data.dto.GameDetailResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class GameDetailRepositoryImplTest {
    private lateinit var apiServices: ApiServices
    private lateinit var gameDetailRepository: GameDetailRepository

    @BeforeEach
    fun setUp() {
        apiServices = mockk()
        gameDetailRepository = GameDetailRepositoryImpl(apiServices)
    }

    @Test
    fun `getGameDetail success response`() = runTest {
        val mockResponse = createGameDetailResponse()

        coEvery {
            apiServices.getGameDetail(any())
        } returns Response.success(mockResponse)

        val result = gameDetailRepository.getGameDetail(1)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { apiServices.getGameDetail(1) }
    }

    @Test
    fun `getGameDetail api response error`() = runTest {
        coEvery {
            apiServices.getGameDetail(any())
        } returns Response.error(404, "Not Found".toResponseBody())

        val result = gameDetailRepository.getGameDetail(1)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("API Error: 404") == true)
        coVerify(exactly = 1) { apiServices.getGameDetail(1) }
    }

    @Test
    fun `getGameDetail throws exception`() = runTest {
        coEvery {
            apiServices.getGameDetail(any())
        } throws kotlin.Exception("Network Error")

        val result = gameDetailRepository.getGameDetail(1)

        assertTrue(result.isFailure)
        assertEquals("Network Error", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { apiServices.getGameDetail(1) }
    }

    private fun createGameDetailResponse(): GameDetailResponse {
        return GameDetailResponse(
            id = 1,
            name = "Game Name",
            backgroundImage = null,
            released = "2023-01-01",
            metacritic = 80,
            rating = 4.5,
            ratingsCount = 5,
            descriptionRaw = "Game Description",
        )
    }
}
