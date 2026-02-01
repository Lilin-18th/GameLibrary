package com.lilin.gamelibrary.feature.favorite

import app.cash.turbine.test
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.domain.model.SortOption
import com.lilin.gamelibrary.domain.usecase.favorite.GetFavoriteGamesUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.RemoveFavoriteGameUseCase
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
class FavoriteViewModelTest {
    private lateinit var getFavoriteGamesUseCase: GetFavoriteGamesUseCase
    private lateinit var removeFavoriteGameUseCase: RemoveFavoriteGameUseCase
    private lateinit var viewModel: FavoriteViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getFavoriteGamesUseCase = mockk()
        removeFavoriteGameUseCase = mockk()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun toggleSortOrder_withAddedDateDescOrder_updatesToAddedDateAsc() =
        runTest {
            val testGames = listOf(
                FavoriteGame(
                    id = 1,
                    name = "Test Game 1",
                    backgroundImage = "https://example.com/image1.jpg",
                    rating = 4.5,
                    metacritic = 85,
                    released = "2024-01-01",
                    addedAt = System.currentTimeMillis() - 1000,
                ),
                FavoriteGame(
                    id = 2,
                    name = "Test Game 2",
                    backgroundImage = "https://example.com/image2.jpg",
                    rating = 4.0,
                    metacritic = 78,
                    released = "2024-02-01",
                    addedAt = System.currentTimeMillis(),
                ),
            )

            every { getFavoriteGamesUseCase.invoke(SortOption.ADDED_DATE_DESC) } returns flowOf(testGames)
            every { getFavoriteGamesUseCase.invoke(SortOption.ADDED_DATE_ASC) } returns flowOf(testGames.reversed())

            viewModel = FavoriteViewModel(getFavoriteGamesUseCase, removeFavoriteGameUseCase)

            viewModel.uiState.test {
                assertEquals(FavoriteUiState.Loading, awaitItem())

                advanceUntilIdle()
                val initialSuccessState = awaitItem()
                assertEquals(true, initialSuccessState is FavoriteUiState.Success)
                assertEquals(
                    SortOption.ADDED_DATE_DESC,
                    (initialSuccessState as FavoriteUiState.Success).sortOption,
                )
                assertEquals(testGames, initialSuccessState.games)

                viewModel.toggleSortOrder(SortOption.ADDED_DATE_ASC)

                assertEquals(FavoriteUiState.Loading, awaitItem())

                advanceUntilIdle()

                val toggledSuccessState = awaitItem()
                assertEquals(true, toggledSuccessState is FavoriteUiState.Success)
                assertEquals(
                    SortOption.ADDED_DATE_ASC,
                    (toggledSuccessState as FavoriteUiState.Success).sortOption,
                )
                assertEquals(testGames.reversed(), toggledSuccessState.games)
            }
        }

    @Test
    fun toggleSortOrder_withAddedDateAscOrder_updatesToAddedDateDesc() =
        runTest {
            val testGames = listOf(
                FavoriteGame(
                    id = 1,
                    name = "Test Game 1",
                    backgroundImage = "https://example.com/image1.jpg",
                    rating = 4.5,
                    metacritic = 85,
                    released = "2024-01-01",
                    addedAt = System.currentTimeMillis() - 1000,
                ),
            )

            every { getFavoriteGamesUseCase.invoke(SortOption.ADDED_DATE_DESC) } returns flowOf(testGames)
            every { getFavoriteGamesUseCase.invoke(SortOption.ADDED_DATE_ASC) } returns flowOf(testGames)

            viewModel = FavoriteViewModel(getFavoriteGamesUseCase, removeFavoriteGameUseCase)
            advanceUntilIdle()

            viewModel.toggleSortOrder(SortOption.ADDED_DATE_ASC)
            advanceUntilIdle()

            viewModel.uiState.test {
                val currentState = awaitItem()
                assertEquals(true, currentState is FavoriteUiState.Success)
                assertEquals(
                    SortOption.ADDED_DATE_ASC,
                    (currentState as FavoriteUiState.Success).sortOption,
                )

                viewModel.toggleSortOrder(SortOption.ADDED_DATE_DESC)

                assertEquals(FavoriteUiState.Loading, awaitItem())

                advanceUntilIdle()

                val toggledSuccessState = awaitItem()
                assertEquals(true, toggledSuccessState is FavoriteUiState.Success)
                assertEquals(
                    SortOption.ADDED_DATE_DESC,
                    (toggledSuccessState as FavoriteUiState.Success).sortOption,
                )
            }
        }

    @Test
    fun toggleSortOrder_withEmptyState_transitionsToLoadingThenEmpty() =
        runTest {
            every { getFavoriteGamesUseCase.invoke(SortOption.ADDED_DATE_DESC) } returns flowOf(emptyList())
            every { getFavoriteGamesUseCase.invoke(SortOption.ADDED_DATE_ASC) } returns flowOf(emptyList())

            viewModel = FavoriteViewModel(getFavoriteGamesUseCase, removeFavoriteGameUseCase)

            viewModel.uiState.test {
                assertEquals(FavoriteUiState.Loading, awaitItem())

                advanceUntilIdle()

                assertEquals(FavoriteUiState.Empty, awaitItem())

                viewModel.toggleSortOrder(SortOption.ADDED_DATE_ASC)

                assertEquals(FavoriteUiState.Loading, awaitItem())

                advanceUntilIdle()

                assertEquals(FavoriteUiState.Empty, awaitItem())
            }
        }
}
