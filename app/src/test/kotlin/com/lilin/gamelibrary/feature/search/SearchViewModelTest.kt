package com.lilin.gamelibrary.feature.search

import app.cash.turbine.test
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.domain.usecase.GetGameSearchUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
class SearchViewModelTest {
    private lateinit var getGameSearchUseCase: GetGameSearchUseCase
    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getGameSearchUseCase = mockk()
        viewModel = SearchViewModel(getGameSearchUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun search_withValidQuery_returnsSuccessWithGames() =
        runTest {
            val testQuery = "test game"
            val testGames = listOf(
                Game(
                    id = 1,
                    name = "Test Game 1",
                    imageUrl = "https://example.com/image1.jpg",
                    releaseDate = "2024-01-01",
                    rating = 4.5,
                    ratingsCount = 1000,
                    metacritic = 85,
                    isTba = false,
                    addedCount = 15000,
                    platforms = listOf("PC", "PlayStation"),
                ),
                Game(
                    id = 2,
                    name = "Test Game 2",
                    imageUrl = "https://example.com/image2.jpg",
                    releaseDate = "2024-02-01",
                    rating = 4.0,
                    ratingsCount = 800,
                    metacritic = 78,
                    isTba = false,
                    addedCount = 12000,
                    platforms = listOf("PC", "Xbox"),
                ),
            )

            coEvery { getGameSearchUseCase(1, 50, testQuery) } returns Result.success(testGames)

            viewModel.onQueryChange(testQuery)

            viewModel.searchUiState.test {
                assertEquals(SearchUiState.None, awaitItem())

                viewModel.search()

                assertEquals(SearchUiState.Loading, awaitItem())
                val successState = awaitItem()
                assertEquals(true, successState is SearchUiState.Success)
                assertEquals(testGames, (successState as SearchUiState.Success).data)
            }
        }

    @Test
    fun search_withNetworkError_returnsErrorState() = runTest {
        val testQuery = "test game"
        val exception = kotlin.RuntimeException("Network error")

        coEvery { getGameSearchUseCase(1, 50, testQuery) } returns Result.failure(exception)

        viewModel.onQueryChange(testQuery)

        viewModel.searchUiState.test {
            assertEquals(SearchUiState.None, awaitItem())

            viewModel.search()

            assertEquals(SearchUiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertEquals(true, errorState is SearchUiState.Error)
            assertEquals(exception, (errorState as SearchUiState.Error).throwable)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "   ", "\t", "\n"])
    fun search_withBlankQuery_stateRemainsUnchanged(query: String) =
        runTest {
            viewModel.onQueryChange(query)

            viewModel.searchUiState.test {
                assertEquals(SearchUiState.None, awaitItem())
                viewModel.search()
                expectNoEvents()
            }
        }

    @Test
    fun onQueryChange_whenCalled_updatesQueryState() = runTest {
        val testQuery = "test query"

        viewModel.query.test {
            assertEquals("", awaitItem())

            viewModel.onQueryChange(testQuery)

            assertEquals(testQuery, awaitItem())
        }
    }
}
