package com.lilin.gamelibrary.di

import com.lilin.gamelibrary.domain.repository.GameRepository
import com.lilin.gamelibrary.domain.repository.GameRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GameRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGameRepository(
        impl: GameRepositoryImpl,
    ): GameRepository
}
