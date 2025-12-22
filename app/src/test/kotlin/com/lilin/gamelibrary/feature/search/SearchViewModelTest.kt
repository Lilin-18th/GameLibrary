package com.lilin.gamelibrary.feature.search

import app.cash.turbine.test
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.domain.model.SearchResult
import com.lilin.gamelibrary.domain.usecase.GetGameSearchUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
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
    fun search_withValidQuery_returnsSuccessWithGames() = runTest {
        val testQuery = "test game"
        val testGames = listOf(
            createMockGame(1),
            createMockGame(2),
        )
        val testSearchResult = SearchResult(
            games = testGames,
            currentPage = 1,
            hasNextPage = true,
        )

        coEvery {
            getGameSearchUseCase(1, 50, testQuery)
        } returns Result.success(testSearchResult)

        viewModel.onQueryChange(testQuery)

        viewModel.searchUiState.test {
            assertEquals(SearchUiState.None, awaitItem())

            viewModel.search()

            assertEquals(SearchUiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is SearchUiState.Success)
            assertEquals(testGames, (successState as SearchUiState.Success).data)
            assertFalse(successState.isLoadingMore)
            assertTrue(successState.hasNextPage)
        }
    }

    @Test
    fun search_withLastPage_returnsSuccessWithoutNextPage() = runTest {
        val testQuery = "test game"
        val testGames = listOf(createMockGame(1))
        val testSearchResult = SearchResult(
            games = testGames,
            currentPage = 1,
            hasNextPage = false,
        )

        coEvery {
            getGameSearchUseCase(1, 50, testQuery)
        } returns Result.success(testSearchResult)

        viewModel.onQueryChange(testQuery)

        viewModel.searchUiState.test {
            assertEquals(SearchUiState.None, awaitItem())

            viewModel.search()

            assertEquals(SearchUiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is SearchUiState.Success)
            assertFalse((successState as SearchUiState.Success).hasNextPage)
        }
    }

    @Test
    fun search_withNetworkError_returnsErrorState() = runTest {
        val testQuery = "test game"
        val exception = kotlin.RuntimeException("Network error")

        coEvery {
            getGameSearchUseCase(1, 50, testQuery)
        } returns Result.failure(exception)

        viewModel.onQueryChange(testQuery)

        viewModel.searchUiState.test {
            assertEquals(SearchUiState.None, awaitItem())

            viewModel.search()

            assertEquals(SearchUiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is SearchUiState.Error)
            assertEquals(exception, (errorState as SearchUiState.Error).throwable)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "   ", "\t", "\n"])
    fun search_withBlankQuery_stateRemainsUnchanged(query: String) = runTest {
        viewModel.onQueryChange(query)

        viewModel.searchUiState.test {
            assertEquals(SearchUiState.None, awaitItem())
            viewModel.search()
            expectNoEvents()
        }
    }

    @Test
    fun loadNextPage_withValidState_loadsAdditionalGames() = runTest {
        val testQuery = "test game"
        val firstPageGames = listOf(createMockGame(1), createMockGame(2))
        val secondPageGames = listOf(createMockGame(3), createMockGame(4))

        val firstSearchResult = SearchResult(
            games = firstPageGames,
            currentPage = 1,
            hasNextPage = true,
        )
        val secondSearchResult = SearchResult(
            games = secondPageGames,
            currentPage = 2,
            hasNextPage = false,
        )

        coEvery {
            getGameSearchUseCase(1, 50, testQuery)
        } returns Result.success(firstSearchResult)

        coEvery {
            getGameSearchUseCase(2, 50, testQuery)
        } returns Result.success(secondSearchResult)

        viewModel.onQueryChange(testQuery)
        viewModel.search()
        advanceUntilIdle()

        viewModel.searchUiState.test {
            val initialState = awaitItem() as SearchUiState.Success
            assertEquals(2, initialState.data.size)
            assertTrue(initialState.hasNextPage)

            viewModel.loadNextPage()

            val loadingMoreState = awaitItem() as SearchUiState.Success
            assertTrue(loadingMoreState.isLoadingMore)
            assertEquals(2, loadingMoreState.data.size)

            val finalState = awaitItem() as SearchUiState.Success
            assertFalse(finalState.isLoadingMore)
            assertFalse(finalState.hasNextPage)
            assertEquals(4, finalState.data.size)
            assertEquals(1, finalState.data[0].id)
            assertEquals(4, finalState.data[3].id)
        }

        coVerify(exactly = 1) { getGameSearchUseCase(1, 50, testQuery) }
        coVerify(exactly = 1) { getGameSearchUseCase(2, 50, testQuery) }
    }

    @Test
    fun loadNextPage_withoutNextPage_doesNotLoadMore() = runTest {
        val testQuery = "test game"
        val testGames = listOf(createMockGame(1))
        val testSearchResult = SearchResult(
            games = testGames,
            currentPage = 1,
            hasNextPage = false,
        )

        coEvery {
            getGameSearchUseCase(1, 50, testQuery)
        } returns Result.success(testSearchResult)

        viewModel.onQueryChange(testQuery)
        viewModel.search()
        advanceUntilIdle()

        viewModel.searchUiState.test {
            val initialState = awaitItem() as SearchUiState.Success
            assertFalse(initialState.hasNextPage)

            viewModel.loadNextPage()

            expectNoEvents()
        }

        coVerify(exactly = 1) { getGameSearchUseCase(1, 50, testQuery) }
        coVerify(exactly = 0) { getGameSearchUseCase(2, 50, testQuery) }
    }

    @Test
    fun loadNextPage_withLoadingMoreInProgress_preventsMultipleCalls() = runTest {
        val testQuery = "test game"
        val firstPageGames = listOf(createMockGame(1))
        val secondPageGames = listOf(createMockGame(2))

        val firstSearchResult = SearchResult(
            games = firstPageGames,
            currentPage = 1,
            hasNextPage = true,
        )
        val secondSearchResult = SearchResult(
            games = secondPageGames,
            currentPage = 2,
            hasNextPage = true,
        )

        coEvery {
            getGameSearchUseCase(1, 50, testQuery)
        } returns Result.success(firstSearchResult)

        coEvery {
            getGameSearchUseCase(2, 50, testQuery)
        } returns Result.success(secondSearchResult)

        viewModel.onQueryChange(testQuery)
        viewModel.search()
        advanceUntilIdle()

        viewModel.loadNextPage()
        viewModel.loadNextPage()
        viewModel.loadNextPage()
        advanceUntilIdle()

        coVerify(exactly = 1) { getGameSearchUseCase(1, 50, testQuery) }
        coVerify(exactly = 1) { getGameSearchUseCase(2, 50, testQuery) }
    }

    @Test
    fun loadNextPage_withError_rollsBackPageNumber() = runTest {
        val testQuery = "test game"
        val firstPageGames = listOf(createMockGame(1))
        val firstSearchResult = SearchResult(
            games = firstPageGames,
            currentPage = 1,
            hasNextPage = true,
        )
        val exception = RuntimeException("Network error")

        coEvery {
            getGameSearchUseCase(1, 50, testQuery)
        } returns Result.success(firstSearchResult)

        coEvery {
            getGameSearchUseCase(2, 50, testQuery)
        } returns Result.failure(exception)

        viewModel.onQueryChange(testQuery)
        viewModel.search()
        advanceUntilIdle()

        viewModel.searchUiState.test {
            val initialState = awaitItem() as SearchUiState.Success
            assertEquals(1, initialState.data.size)

            viewModel.loadNextPage()

            val loadingMoreState = awaitItem() as SearchUiState.Success
            assertTrue(loadingMoreState.isLoadingMore)

            val errorRecoveryState = awaitItem() as SearchUiState.Success
            assertFalse(errorRecoveryState.isLoadingMore)
            assertEquals(1, errorRecoveryState.data.size)
        }

        coVerify(exactly = 1) { getGameSearchUseCase(2, 50, testQuery) }
    }

    @Test
    fun search_newSearch_resetsPage() = runTest {
        val firstQuery = "first query"
        val secondQuery = "second query"
        val firstGames = listOf(createMockGame(1))
        val secondGames = listOf(createMockGame(2))
        val firstSearchResult = SearchResult(
            games = firstGames,
            currentPage = 1,
            hasNextPage = true,
        )
        val secondSearchResult = SearchResult(
            games = secondGames,
            currentPage = 1,
            hasNextPage = false,
        )

        coEvery {
            getGameSearchUseCase(1, 50, firstQuery)
        } returns Result.success(firstSearchResult)

        coEvery {
            getGameSearchUseCase(1, 50, secondQuery)
        } returns Result.success(secondSearchResult)

        viewModel.onQueryChange(firstQuery)
        viewModel.search()
        advanceUntilIdle()

        viewModel.onQueryChange(secondQuery)
        viewModel.search()
        advanceUntilIdle()

        viewModel.searchUiState.test {
            val finalState = awaitItem() as SearchUiState.Success
            assertEquals(1, finalState.data.size)
            assertEquals(2, finalState.data[0].id)
        }

        coVerify(exactly = 1) { getGameSearchUseCase(1, 50, firstQuery) }
        coVerify(exactly = 1) { getGameSearchUseCase(1, 50, secondQuery) }
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
