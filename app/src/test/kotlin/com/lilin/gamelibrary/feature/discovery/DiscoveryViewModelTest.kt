package com.lilin.gamelibrary.feature.discovery

import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.domain.usecase.GetHighMetacriticScoreGamesUseCase
import com.lilin.gamelibrary.domain.usecase.GetNewReleasesUseCase
import com.lilin.gamelibrary.domain.usecase.GetTrendingGamesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoveryViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockGetTrendingGamesUseCase: GetTrendingGamesUseCase
    private lateinit var mockGetHighRatedGamesUseCase: GetHighMetacriticScoreGamesUseCase
    private lateinit var mockGetNewReleasesUseCase: GetNewReleasesUseCase
    private lateinit var viewModel: DiscoveryViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockGetTrendingGamesUseCase = mockk()
        mockGetHighRatedGamesUseCase = mockk()
        mockGetNewReleasesUseCase = mockk()
    }

    @Test
    fun retryTrendingGames_withSuccessfulUseCase_updatesStateFromLoadingToSuccess() =
        runTest {
            coEvery {
                mockGetTrendingGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetHighRatedGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetNewReleasesUseCase(any(), any())
            } returns Result.success(mockGamesList)

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            viewModel.retryTrendingGames()

            assertEquals(DiscoveryUiState.Loading, viewModel.trendingState.value)
            testScheduler.advanceUntilIdle()
            assertEquals(
                DiscoveryUiState.Success(mockGamesList),
                viewModel.trendingState.value,
            )

            coVerify(exactly = 2) { mockGetTrendingGamesUseCase(page = 1, pageSize = 4) }
        }

    @Test
    fun retryTrendingGames_withFailedUseCase_updatesStateToError() = runTest {
        val exception = kotlin.Exception("Network error")
        coEvery {
            mockGetTrendingGamesUseCase(
                any(),
                any(),
            )
        } returns Result.failure(exception)
        coEvery {
            mockGetHighRatedGamesUseCase(
                any(),
                any(),
            )
        } returns Result.success(listOf())
        coEvery {
            mockGetNewReleasesUseCase(
                any(),
                any(),
            )
        } returns Result.success(listOf())

        viewModel = DiscoveryViewModel(
            getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
            getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
            getNewReleasesUseCase = mockGetNewReleasesUseCase,
        )

        testScheduler.advanceUntilIdle()

        viewModel.retryTrendingGames()
        testScheduler.advanceUntilIdle()

        assertEquals(DiscoveryUiState.Error(exception), viewModel.trendingState.value)
        coVerify(exactly = 2) { mockGetTrendingGamesUseCase(page = 1, pageSize = 4) }
    }

    @Test
    fun retryHighlyRatedGames_withSuccessfulUseCase_updatesStateFromLoadingToSuccess() =
        runTest {
            coEvery {
                mockGetTrendingGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetHighRatedGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetNewReleasesUseCase(any(), any())
            } returns Result.success(mockGamesList)

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            viewModel.retryHighlyRatedGames()
            assertEquals(DiscoveryUiState.Loading, viewModel.highlyRatedState.value)
            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Success(mockGamesList), viewModel.highlyRatedState.value)
            coVerify(exactly = 2) { mockGetHighRatedGamesUseCase(page = 1, pageSize = 4) }
        }

    @Test
    fun retryHighlyRatedGames_withFailedUseCase_updatesStateToError() = runTest {
        val exception = kotlin.Exception("Network error")
        coEvery {
            mockGetTrendingGamesUseCase(
                any(),
                any(),
            )
        } returns Result.success(listOf())
        coEvery {
            mockGetHighRatedGamesUseCase(
                any(),
                any(),
            )
        } returns Result.failure(exception)
        coEvery {
            mockGetNewReleasesUseCase(
                any(),
                any(),
            )
        } returns Result.success(listOf())

        viewModel = DiscoveryViewModel(
            getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
            getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
            getNewReleasesUseCase = mockGetNewReleasesUseCase,
        )

        viewModel.retryHighlyRatedGames()
        assertEquals(DiscoveryUiState.Loading, viewModel.highlyRatedState.value)
        testScheduler.advanceUntilIdle()
        assertEquals(DiscoveryUiState.Error(exception), viewModel.highlyRatedState.value)
        coVerify(exactly = 2) { mockGetHighRatedGamesUseCase(page = 1, pageSize = 4) }
    }

    @Test
    fun retryNewReleases_withSuccessfulUseCase_updatesStateFromLoadingToSuccess() =
        runTest {
            coEvery {
                mockGetHighRatedGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetTrendingGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetNewReleasesUseCase(any(), any())
            } returns Result.success(mockGamesList)

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            viewModel.retryNewReleases()
            assertEquals(DiscoveryUiState.Loading, viewModel.newReleasesState.value)
            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Success(mockGamesList), viewModel.newReleasesState.value)
            coVerify(exactly = 2) { mockGetNewReleasesUseCase(page = 1, pageSize = 4) }
        }

    @Test
    fun retryHighlyRatedGames_withFailedUseCaseDuplicate_updatesStateToError() = runTest {
        val exception = kotlin.Exception("Network error")
        coEvery {
            mockGetTrendingGamesUseCase(
                any(),
                any(),
            )
        } returns Result.success(listOf())
        coEvery {
            mockGetHighRatedGamesUseCase(
                any(),
                any(),
            )
        } returns Result.failure(exception)
        coEvery {
            mockGetNewReleasesUseCase(
                any(),
                any(),
            )
        } returns Result.success(listOf())

        viewModel = DiscoveryViewModel(
            getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
            getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
            getNewReleasesUseCase = mockGetNewReleasesUseCase,
        )

        viewModel.retryHighlyRatedGames()
        assertEquals(DiscoveryUiState.Loading, viewModel.highlyRatedState.value)
        testScheduler.advanceUntilIdle()
        assertEquals(DiscoveryUiState.Error(exception), viewModel.highlyRatedState.value)
        coVerify(exactly = 2) { mockGetHighRatedGamesUseCase(page = 1, pageSize = 4) }
    }

    @Test
    fun reloadTrendingGames_withSuccessState_updatesFromReLoadingToSuccess() =
        runTest {
            coEvery {
                mockGetHighRatedGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetTrendingGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetNewReleasesUseCase(any(), any())
            } returns Result.success(mockGamesList)

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Success(mockGamesList), viewModel.trendingState.value)

            viewModel.reloadTrendingGames()

            assertEquals(DiscoveryUiState.ReLoading(mockGamesList), viewModel.trendingState.value)
            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Success(mockGamesList), viewModel.trendingState.value)
            coVerify(exactly = 2) { mockGetTrendingGamesUseCase(page = 1, pageSize = 4) }
        }

    @Test
    fun reloadTrendingGames_withSuccessStateAndFailedUseCase_updatesToReLoadingError() =
        runTest {
            val exception = kotlin.Exception("Network error")
            coEvery {
                mockGetTrendingGamesUseCase(
                    any(),
                    any(),
                )
            } returnsMany listOf(
                Result.success(mockGamesList),
                Result.failure(exception),
            )
            coEvery {
                mockGetHighRatedGamesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())
            coEvery {
                mockGetNewReleasesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Success(mockGamesList), viewModel.trendingState.value)

            viewModel.reloadTrendingGames()
            assertEquals(DiscoveryUiState.ReLoading(mockGamesList), viewModel.trendingState.value)

            testScheduler.advanceUntilIdle()
            assertEquals(
                DiscoveryUiState.ReLoadingError(mockGamesList, exception),
                viewModel.trendingState.value,
            )
            coVerify(exactly = 2) { mockGetTrendingGamesUseCase(page = 1, pageSize = 4) }
        }

    @Test
    fun reloadTrendingGames_withErrorState_doesNotCallUseCaseAgain() =
        runTest {
            val exception = kotlin.Exception("Network error")
            coEvery {
                mockGetTrendingGamesUseCase(
                    any(),
                    any(),
                )
            } returns Result.failure(exception)
            coEvery {
                mockGetHighRatedGamesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())
            coEvery {
                mockGetNewReleasesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Error(exception), viewModel.trendingState.value)

            viewModel.reloadTrendingGames()
            coVerify(exactly = 1) { mockGetTrendingGamesUseCase(page = 1, pageSize = 4) }
        }

    @Test
    fun reloadHighlyRatedGames_withSuccessState_updatesFromReLoadingToSuccess() =
        runTest {
            coEvery {
                mockGetHighRatedGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetTrendingGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetNewReleasesUseCase(any(), any())
            } returns Result.success(mockGamesList)

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Success(mockGamesList), viewModel.highlyRatedState.value)

            viewModel.reloadHighlyRatedGames()

            assertEquals(
                DiscoveryUiState.ReLoading(mockGamesList),
                viewModel.highlyRatedState.value,
            )
            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Success(mockGamesList), viewModel.highlyRatedState.value)
            coVerify(exactly = 2) { mockGetHighRatedGamesUseCase(page = 1, pageSize = 4) }
        }

    @Test
    fun reloadHighlyRatedGames_withSuccessStateAndFailedUseCase_updatesToReLoadingError() =
        runTest {
            val exception = kotlin.Exception("Network error")
            coEvery {
                mockGetTrendingGamesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())
            coEvery {
                mockGetHighRatedGamesUseCase(
                    any(),
                    any(),
                )
            } returnsMany listOf(
                Result.success(mockGamesList),
                Result.failure(exception),
            )
            coEvery {
                mockGetNewReleasesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Success(mockGamesList), viewModel.highlyRatedState.value)

            viewModel.reloadHighlyRatedGames()
            assertEquals(
                DiscoveryUiState.ReLoading(mockGamesList),
                viewModel.highlyRatedState.value,
            )

            testScheduler.advanceUntilIdle()
            assertEquals(
                DiscoveryUiState.ReLoadingError(mockGamesList, exception),
                viewModel.highlyRatedState.value,
            )
            coVerify(exactly = 2) { mockGetHighRatedGamesUseCase(page = 1, pageSize = 4) }
        }

    @Test
    fun reloadHighlyRatedGames_withErrorState_doesNotCallUseCaseAgain() =
        runTest {
            val exception = kotlin.Exception("Network error")
            coEvery {
                mockGetTrendingGamesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())
            coEvery {
                mockGetHighRatedGamesUseCase(
                    any(),
                    any(),
                )
            } returns Result.failure(exception)
            coEvery {
                mockGetNewReleasesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Error(exception), viewModel.highlyRatedState.value)

            viewModel.reloadHighlyRatedGames()
            coVerify(exactly = 1) { mockGetHighRatedGamesUseCase(page = 1, pageSize = 4) }
        }

    @Test
    fun reloadNewReleases_withSuccessState_updatesFromReLoadingToSuccess() =
        runTest {
            coEvery {
                mockGetHighRatedGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetTrendingGamesUseCase(any(), any())
            } returns Result.success(mockGamesList)
            coEvery {
                mockGetNewReleasesUseCase(any(), any())
            } returns Result.success(mockGamesList)

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Success(mockGamesList), viewModel.newReleasesState.value)

            viewModel.reloadNewReleaseGame()

            assertEquals(
                DiscoveryUiState.ReLoading(mockGamesList),
                viewModel.newReleasesState.value,
            )
            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Success(mockGamesList), viewModel.newReleasesState.value)
            coVerify(exactly = 2) { mockGetNewReleasesUseCase(page = 1, pageSize = 4) }
        }

    @Test
    fun reloadNewReleases_withSuccessStateAndFailedUseCase_updatesToReLoadingError() =
        runTest {
            val exception = kotlin.Exception("Network error")
            coEvery {
                mockGetTrendingGamesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())
            coEvery {
                mockGetHighRatedGamesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())
            coEvery {
                mockGetNewReleasesUseCase(
                    any(),
                    any(),
                )
            } returnsMany listOf(
                Result.success(mockGamesList),
                Result.failure(exception),
            )

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Success(mockGamesList), viewModel.newReleasesState.value)

            viewModel.reloadNewReleaseGame()
            assertEquals(
                DiscoveryUiState.ReLoading(mockGamesList),
                viewModel.newReleasesState.value,
            )

            testScheduler.advanceUntilIdle()
            assertEquals(
                DiscoveryUiState.ReLoadingError(mockGamesList, exception),
                viewModel.newReleasesState.value,
            )
            coVerify(exactly = 2) { mockGetNewReleasesUseCase(page = 1, pageSize = 4) }
        }

    @Test
    fun reloadNewReleases_withErrorState_doesNotCallUseCaseAgain() =
        runTest {
            val exception = kotlin.Exception("Network error")
            coEvery {
                mockGetTrendingGamesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())
            coEvery {
                mockGetHighRatedGamesUseCase(
                    any(),
                    any(),
                )
            } returns Result.success(listOf())
            coEvery {
                mockGetNewReleasesUseCase(
                    any(),
                    any(),
                )
            } returns Result.failure(exception)

            viewModel = DiscoveryViewModel(
                getTrendingGamesUseCase = mockGetTrendingGamesUseCase,
                getHighRatedGamesUseCase = mockGetHighRatedGamesUseCase,
                getNewReleasesUseCase = mockGetNewReleasesUseCase,
            )

            testScheduler.advanceUntilIdle()
            assertEquals(DiscoveryUiState.Error(exception), viewModel.newReleasesState.value)

            viewModel.reloadNewReleaseGame()
            coVerify(exactly = 1) { mockGetNewReleasesUseCase(page = 1, pageSize = 4) }
        }

    companion object {
        private val mockGamesList = listOf<Game>()
    }
}
