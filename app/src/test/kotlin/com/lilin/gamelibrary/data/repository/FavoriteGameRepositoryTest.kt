package com.lilin.gamelibrary.data.repository

import com.lilin.gamelibrary.data.local.dao.FavoriteGameDao
import com.lilin.gamelibrary.data.local.entity.FavoriteGameEntity
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.domain.model.SortOrder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// mockの誤検知
// https://youtrack.jetbrains.com/projects/KTIJ/issues/KTIJ-34798/K2-False-positive-inspection-Flow-is-constructed-but-not-used-with-Mockito
@Suppress("UnusedFlow")
class FavoriteGameRepositoryTest {
    private lateinit var favoriteGameDao: FavoriteGameDao
    private lateinit var favoriteGameRepository: FavoriteGameRepository

    @BeforeEach
    fun setUp() {
        favoriteGameDao = mockk()
        favoriteGameRepository = FavoriteGameRepository(favoriteGameDao)
    }

    @Test
    fun getFavoriteGames_withNewestFirstOrder_returnsCorrectlyMappedGames() = runTest {
        val entities = listOf(
            createMockFavoriteGameEntity(1, "Game 1", 1700000000L),
            createMockFavoriteGameEntity(2, "Game 2", 1700000001L),
        )
        every { favoriteGameDao.getFavoriteGamesNewestFirst() } returns flowOf(entities)

        val result = favoriteGameRepository.getFavoriteGames(SortOrder.NEWEST_FIRST).toList()

        assertEquals(1, result.size)
        assertEquals(2, result[0].size)
        assertEquals("Game 1", result[0][0].name)
        assertEquals("Game 2", result[0][1].name)
        verify(exactly = 1) { favoriteGameDao.getFavoriteGamesNewestFirst() }
    }

    @Test
    fun getFavoriteGames_withOldestFirstOrder_returnsCorrectlyMappedGames() = runTest {
        val entities = listOf(
            createMockFavoriteGameEntity(3, "Game 3", 1700000000L),
            createMockFavoriteGameEntity(4, "Game 4", 1700000001L),
        )
        every { favoriteGameDao.getFavoriteGamesOldestFirst() } returns flowOf(entities)

        val result = favoriteGameRepository.getFavoriteGames(SortOrder.OLDEST_FIRST).toList()

        assertEquals(1, result.size)
        assertEquals(2, result[0].size)
        assertEquals("Game 3", result[0][0].name)
        assertEquals("Game 4", result[0][1].name)
        verify(exactly = 1) { favoriteGameDao.getFavoriteGamesOldestFirst() }
    }

    @Test
    fun getFavoriteGames_whenDaoReturnsEmptyList_returnsEmptyList() = runTest {
        every { favoriteGameDao.getFavoriteGamesNewestFirst() } returns flowOf(emptyList())

        val result = favoriteGameRepository.getFavoriteGames(SortOrder.NEWEST_FIRST).toList()

        assertEquals(1, result.size)
        assertTrue(result[0].isEmpty())
        verify(exactly = 1) { favoriteGameDao.getFavoriteGamesNewestFirst() }
    }

    @Test
    fun isFavorite_whenGameIsFavorite_returnsTrue() = runTest {
        val gameId = 123
        every { favoriteGameDao.isFavorite(gameId) } returns flowOf(true)

        val result = favoriteGameRepository.isFavorite(gameId).toList()

        assertEquals(1, result.size)
        assertTrue(result[0])
        verify(exactly = 1) { favoriteGameDao.isFavorite(gameId) }
    }

    @Test
    fun isFavorite_whenGameIsNotFavorite_returnsFalse() = runTest {
        val gameId = 456
        every { favoriteGameDao.isFavorite(gameId) } returns flowOf(false)

        val result = favoriteGameRepository.isFavorite(gameId).toList()

        assertEquals(1, result.size)
        assertFalse(result[0])
        verify(exactly = 1) { favoriteGameDao.isFavorite(gameId) }
    }

    @Test
    fun insertFavoriteGame_withValidGame_callsDaoWithCorrectEntity() = runTest {
        val favoriteGame = createMockFavoriteGame()
        coEvery { favoriteGameDao.insertFavoriteGame(any()) } returns Unit

        favoriteGameRepository.insertFavoriteGame(favoriteGame)

        coVerify(exactly = 1) {
            favoriteGameDao.insertFavoriteGame(
                match {
                    it.id == favoriteGame.id && it.name == favoriteGame.name && it.addedAt == favoriteGame.addedAt
                },
            )
        }
    }

    @Test
    fun deleteFavoriteGame_withValidGameId_callsDaoWithCorrectGameId() = runTest {
        val gameId = 789
        coEvery { favoriteGameDao.deleteFavoriteGame(gameId) } returns Unit

        favoriteGameRepository.deleteFavoriteGame(gameId)

        coVerify(exactly = 1) { favoriteGameDao.deleteFavoriteGame(gameId) }
    }

    private fun createMockFavoriteGameEntity(
        id: Int,
        name: String,
        addedAt: Long,
    ): FavoriteGameEntity {
        return FavoriteGameEntity(
            id = id,
            name = name,
            backgroundImage = "https://example.com/image$id.jpg",
            rating = 4.5,
            metacritic = 85,
            released = "2024-01-01",
            addedAt = addedAt,
        )
    }

    private fun createMockFavoriteGame(): FavoriteGame {
        return FavoriteGame(
            id = 1,
            name = "Test Game Name",
            backgroundImage = "https://example.com/image1.jpg",
            rating = 4.5,
            metacritic = 85,
            released = "2024-01-01",
            addedAt = 1700L,
        )
    }
}
