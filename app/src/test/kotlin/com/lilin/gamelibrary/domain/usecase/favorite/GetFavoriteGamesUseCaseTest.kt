package com.lilin.gamelibrary.domain.usecase.favorite

import com.lilin.gamelibrary.data.repository.FavoriteGameRepository
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.domain.model.SortOption
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetFavoriteGamesUseCaseTest {
    private lateinit var repository: FavoriteGameRepository
    private lateinit var useCase: GetFavoriteGamesUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetFavoriteGamesUseCase(repository)
    }

    @Test
    fun invoke_withAddedDateDescOrder_returnsCorrectFlow() = runTest {
        val order = SortOption.ADDED_DATE_DESC
        val games = newestGames()

        coEvery {
            repository.getFavoriteGames2(order)
        } returns flowOf(games)

        val result = useCase.invoke(order)
        val actualGames = result.first()

        assertEquals(games, actualGames)
    }

    @Test
    fun invoke_withAddedDateAscOrder_returnsCorrectFlow() = runTest {
        val order = SortOption.ADDED_DATE_ASC
        val games = oldestGames()

        coEvery {
            repository.getFavoriteGames2(order)
        } returns flowOf(games)

        val result = useCase.invoke(order)
        val actualGames = result.first()

        assertEquals(games, actualGames)
    }

    @Test
    fun invoke_withEmptyList_returnsEmptyFlow() = runTest {
        val order = SortOption.ADDED_DATE_DESC
        val emptyGames = emptyList<FavoriteGame>()

        coEvery {
            repository.getFavoriteGames2(order)
        } returns flowOf(emptyGames)

        val result = useCase.invoke(order)
        val actualGames = result.first()

        assertEquals(emptyGames, actualGames)
    }

    private fun newestGames(): List<FavoriteGame> {
        return listOf(
            FavoriteGame(
                id = 3,
                name = "Game 3",
                backgroundImage = "game3.jpg",
                rating = 4.0,
                metacritic = 80,
                released = "2023-03-01",
                addedAt = 3000L,
            ),
            FavoriteGame(
                id = 2,
                name = "Game 2",
                backgroundImage = "game2.jpg",
                rating = 4.0,
                metacritic = 80,
                released = "2023-02-01",
                addedAt = 2000L,
            ),
            FavoriteGame(
                id = 1,
                name = "Game 1",
                backgroundImage = "game1.jpg",
                rating = 4.0,
                metacritic = 80,
                released = "2023-01-01",
                addedAt = 1000L,
            ),
        )
    }

    private fun oldestGames(): List<FavoriteGame> {
        return listOf(
            FavoriteGame(
                id = 1,
                name = "Game 1",
                backgroundImage = "game1.jpg",
                rating = 4.0,
                metacritic = 80,
                released = "2023-01-01",
                addedAt = 1000L,
            ),
            FavoriteGame(
                id = 2,
                name = "Game 2",
                backgroundImage = "game2.jpg",
                rating = 4.0,
                metacritic = 80,
                released = "2023-02-01",
                addedAt = 2000L,
            ),
            FavoriteGame(
                id = 3,
                name = "Game 3",
                backgroundImage = "game3.jpg",
                rating = 4.0,
                metacritic = 80,
                released = "2023-03-01",
                addedAt = 3000L,
            ),
        )
    }
}
