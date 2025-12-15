package com.lilin.gamelibrary.di

import com.lilin.gamelibrary.domain.repository.GameDetailRepository
import com.lilin.gamelibrary.domain.repository.GameRepository
import com.lilin.gamelibrary.domain.usecase.GetGameDetailUseCase
import com.lilin.gamelibrary.domain.usecase.GetHighMetacriticScoreGamesUseCase
import com.lilin.gamelibrary.domain.usecase.GetNewReleasesUseCase
import com.lilin.gamelibrary.domain.usecase.GetTrendingGamesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object GameUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetHighRatedGamesUseCase(repository: GameRepository): GetHighMetacriticScoreGamesUseCase {
        return GetHighMetacriticScoreGamesUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetTrendingGamesUseCase(repository: GameRepository): GetTrendingGamesUseCase {
        return GetTrendingGamesUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetNewReleasesUseCase(repository: GameRepository): GetNewReleasesUseCase {
        return GetNewReleasesUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetGameDetailsUseCase(repository: GameDetailRepository): GetGameDetailUseCase {
        return GetGameDetailUseCase(repository)
    }
}
