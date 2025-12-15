package com.lilin.gamelibrary.feature.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.lilin.gamelibrary.domain.model.Developer
import com.lilin.gamelibrary.domain.model.GameDetail
import com.lilin.gamelibrary.domain.model.Genre
import com.lilin.gamelibrary.domain.model.Publisher
import com.lilin.gamelibrary.domain.model.Screenshot
import com.lilin.gamelibrary.domain.model.Tag
import com.lilin.gamelibrary.domain.usecase.GetGameDetailUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.AddFavoriteGameUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.IsFavoriteGameUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.RemoveFavoriteGameUseCase
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class GameDetailViewModelTest {
    private lateinit var viewModel: GameDetailViewModel

    private lateinit var getGameDetailUseCase: GetGameDetailUseCase
    private lateinit var isFavoriteGameUseCase: IsFavoriteGameUseCase
    private lateinit var addFavoriteGameUseCase: AddFavoriteGameUseCase
    private lateinit var removeFavoriteGameUseCase: RemoveFavoriteGameUseCase

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getGameDetailUseCase = mockk()
        isFavoriteGameUseCase = mockk()
        addFavoriteGameUseCase = mockk()
        removeFavoriteGameUseCase = mockk()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadGameDetail_withValidGameId_returnsSuccessState() =
        runTest {
            val savedStateHandle = SavedStateHandle(mapOf("gameId" to 1))
            every { isFavoriteGameUseCase(1) } returns flowOf(false)
            coEvery { getGameDetailUseCase(1) } returns Result.success(gameDetail)

            viewModel = GameDetailViewModel(
                savedStateHandle = savedStateHandle,
                gameDetailUseCase = getGameDetailUseCase,
                isFavoriteGameUseCase = isFavoriteGameUseCase,
                addFavoriteGameUseCase = addFavoriteGameUseCase,
                removeFavoriteGameUseCase = removeFavoriteGameUseCase,
            )

            viewModel.state.test {
                assertEquals(GameDetailUiState.Loading, awaitItem())
                val successState = awaitItem()
                assertEquals(true, successState is GameDetailUiState.Success)
                assertEquals(gameDetail, (successState as GameDetailUiState.Success).gameDetail)
                assertEquals(false, successState.isFavorite)
            }
        }

    @Test
    fun loadGameDetail_withNetworkError_returnsErrorState() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf("gameId" to 1))
        val exception = kotlin.RuntimeException("Network error")
        coEvery { getGameDetailUseCase(1) } returns Result.failure(exception)

        viewModel = GameDetailViewModel(
            savedStateHandle = savedStateHandle,
            gameDetailUseCase = getGameDetailUseCase,
            isFavoriteGameUseCase = isFavoriteGameUseCase,
            addFavoriteGameUseCase = addFavoriteGameUseCase,
            removeFavoriteGameUseCase = removeFavoriteGameUseCase,
        )

        viewModel.state.test {
            assertEquals(GameDetailUiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertEquals(true, errorState is GameDetailUiState.Error)
            assertEquals(exception, (errorState as GameDetailUiState.Error).throwable)
        }
    }

    @Test
    fun toggleFavorite_whenGameIsNotFavorite_addsToFavorites() =
        runTest {
            val savedStateHandle = SavedStateHandle(mapOf("gameId" to 1))
            every { isFavoriteGameUseCase(1) } returns flowOf(false)
            coEvery { getGameDetailUseCase(1) } returns Result.success(gameDetail)
            coJustRun { addFavoriteGameUseCase(any()) }

            viewModel = GameDetailViewModel(
                savedStateHandle = savedStateHandle,
                gameDetailUseCase = getGameDetailUseCase,
                isFavoriteGameUseCase = isFavoriteGameUseCase,
                addFavoriteGameUseCase = addFavoriteGameUseCase,
                removeFavoriteGameUseCase = removeFavoriteGameUseCase,
            )

            advanceUntilIdle()

            viewModel.toggleFavorite()
            advanceUntilIdle()

            coVerify { addFavoriteGameUseCase(any()) }
        }

    @Test
    fun toggleFavorite_whenGameIsFavorite_removesFromFavorites() =
        runTest {
            val savedStateHandle = SavedStateHandle(mapOf("gameId" to 1))
            every { isFavoriteGameUseCase(1) } returns flowOf(true)
            coEvery { getGameDetailUseCase(1) } returns Result.success(gameDetail)
            coJustRun { removeFavoriteGameUseCase(1) }

            viewModel = GameDetailViewModel(
                savedStateHandle = savedStateHandle,
                gameDetailUseCase = getGameDetailUseCase,
                isFavoriteGameUseCase = isFavoriteGameUseCase,
                addFavoriteGameUseCase = addFavoriteGameUseCase,
                removeFavoriteGameUseCase = removeFavoriteGameUseCase,
            )

            advanceUntilIdle()

            viewModel.toggleFavorite()
            advanceUntilIdle()

            coVerify { removeFavoriteGameUseCase(1) }
        }

    @Test
    fun retryLoadGameDetail_whenCalled_executesGameDetailLoadAgain() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf("gameId" to 1))
        every { isFavoriteGameUseCase(1) } returns flowOf(false)
        coEvery { getGameDetailUseCase(1) } returns Result.success(gameDetail)

        viewModel = GameDetailViewModel(
            savedStateHandle = savedStateHandle,
            gameDetailUseCase = getGameDetailUseCase,
            isFavoriteGameUseCase = isFavoriteGameUseCase,
            addFavoriteGameUseCase = addFavoriteGameUseCase,
            removeFavoriteGameUseCase = removeFavoriteGameUseCase,
        )

        advanceUntilIdle()

        viewModel.retryLoadGameDetail()
        advanceUntilIdle()

        coVerify(exactly = 2) { getGameDetailUseCase(1) }
    }

    companion object {
        private val gameDetail = GameDetail(
            id = 1,
            name = "Test Game",
            backgroundImage = "https://example.com/image.jpg",
            releaseDate = "2024-01-01",
            metacritic = 85,
            rating = 4.5,
            ratingsCount = 1000,
            platformNames = listOf("PC", "PlayStation"),
            genres = listOf(Genre(1, "Action", "action")),
            description = "Test game description",
            shortScreenshots = listOf(Screenshot(1, "https://example.com/screenshot.jpg")),
            developers = listOf(Developer(1, "Test Developer")),
            publishers = listOf(Publisher(1, "Test Publisher")),
            esrbRating = "M",
            playtime = 30,
            tags = listOf(Tag(1, "Singleplayer", "singleplayer")),
        )
    }
}
