package com.lilin.gamelibrary.domain.usecase.favorite

import com.lilin.gamelibrary.data.repository.FavoriteGameRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IsFavoriteGameUseCaseTest {
    private lateinit var repository: FavoriteGameRepository
    private lateinit var useCase: IsFavoriteGameUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = IsFavoriteGameUseCase(repository)
    }

    @Test
    fun invoke_withFavoriteGame_returnsTrue() = runTest {
        val gameId = 1

        coEvery {
            repository.isFavorite(gameId)
        } returns flowOf(true)

        val result = useCase.invoke(gameId)
        val isFavorite = result.first()

        assertEquals(true, isFavorite)
    }

    @Test
    fun invoke_withNonFavoriteGame_returnsFalse() = runTest {
        val gameId = 2

        coEvery {
            repository.isFavorite(gameId)
        } returns flowOf(false)

        val result = useCase.invoke(gameId)
        val isFavorite = result.first()

        assertEquals(false, isFavorite)
    }
}
