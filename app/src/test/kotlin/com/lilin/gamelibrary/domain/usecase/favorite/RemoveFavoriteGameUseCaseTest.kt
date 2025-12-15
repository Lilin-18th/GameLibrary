package com.lilin.gamelibrary.domain.usecase.favorite

import com.lilin.gamelibrary.data.repository.FavoriteGameRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoveFavoriteGameUseCaseTest {
    private lateinit var repository: FavoriteGameRepository
    private lateinit var useCase: RemoveFavoriteGameUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = RemoveFavoriteGameUseCase(repository)
    }

    @Test
    fun `invoke calls repository deleteFavoriteGame`() = runTest {
        val gameId = 1

        coJustRun { repository.deleteFavoriteGame(gameId) }

        useCase(gameId)

        coVerify(exactly = 1) { repository.deleteFavoriteGame(gameId) }
    }
}
