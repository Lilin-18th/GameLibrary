package com.lilin.gamelibrary.domain.usecase.favorite

import com.lilin.gamelibrary.data.repository.FavoriteGameRepository
import com.lilin.gamelibrary.domain.model.FavoriteGame
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddFavoriteGameUseCaseTest {

    private lateinit var repository: FavoriteGameRepository
    private lateinit var useCase: AddFavoriteGameUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = AddFavoriteGameUseCase(repository)
    }

    @Test
    fun invoke_withValidGame_callsRepositoryInsertFavoriteGame() = runTest {
        val game = FavoriteGame(
            id = 1,
            name = "Test Game",
            backgroundImage = "test.jpg",
            rating = 4.5,
            metacritic = 85,
            released = "2023-01-01",
            addedAt = 1000L,
        )

        coJustRun { repository.insertFavoriteGame(game) }

        useCase(game)

        coVerify(exactly = 1) { repository.insertFavoriteGame(game) }
    }

    @Test
    fun invoke_withNullBackgroundImage_callsRepositoryInsertFavoriteGame() = runTest {
        val game = FavoriteGame(
            id = 2,
            name = "Test Game 2",
            backgroundImage = null,
            rating = 4.0,
            metacritic = null,
            released = null,
            addedAt = 2000L,
        )

        coJustRun { repository.insertFavoriteGame(game) }

        useCase(game)

        coVerify(exactly = 1) { repository.insertFavoriteGame(game) }
    }
}
