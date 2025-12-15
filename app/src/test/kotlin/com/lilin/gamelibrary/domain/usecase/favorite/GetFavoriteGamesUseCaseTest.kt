package com.lilin.gamelibrary.domain.usecase.favorite

import com.lilin.gamelibrary.data.repository.FavoriteGameRepository
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.domain.model.SortOrder
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
    fun `invoke with NEWEST_FIRST returns correct flow`() = runTest {
        val order = SortOrder.NEWEST_FIRST
        val games = newestGames()

        coEvery {
            repository.getFavoriteGames(order)
        } returns flowOf(games)

        val result = useCase.invoke(order)
        val actualGames = result.first()

        assertEquals(games, actualGames)
    }

    @Test
    fun `invoke with OLDEST_FIRST returns correct flow`() = runTest {
        val order = SortOrder.OLDEST_FIRST
        val games = oldestGames()

        coEvery {
            repository.getFavoriteGames(order)
        } returns flowOf(games)

        val result = useCase.invoke(order)
        val actualGames = result.first()

        assertEquals(games, actualGames)
    }

    @Test
    fun `invoke with empty list returns empty flow`() = runTest {
        val order = SortOrder.NEWEST_FIRST
        val emptyGames = emptyList<FavoriteGame>()

        coEvery {
            repository.getFavoriteGames(order)
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
