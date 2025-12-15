package com.lilin.gamelibrary.di

import com.lilin.gamelibrary.data.repository.FavoriteGameRepository
import com.lilin.gamelibrary.domain.repository.GameDetailRepository
import com.lilin.gamelibrary.domain.repository.GameRepository
import com.lilin.gamelibrary.domain.usecase.GetGameDetailUseCase
import com.lilin.gamelibrary.domain.usecase.GetGameSearchUseCase
import com.lilin.gamelibrary.domain.usecase.GetHighMetacriticScoreGamesUseCase
import com.lilin.gamelibrary.domain.usecase.GetNewReleasesUseCase
import com.lilin.gamelibrary.domain.usecase.GetTrendingGamesUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.AddFavoriteGameUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.GetFavoriteGamesUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.IsFavoriteGameUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.RemoveFavoriteGameUseCase
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

    @Provides
    @ViewModelScoped
    fun provideGetGameSearchUseCase(repository: GameRepository): GetGameSearchUseCase {
        return GetGameSearchUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetFavoriteGamesUseCase(repository: FavoriteGameRepository): GetFavoriteGamesUseCase {
        return GetFavoriteGamesUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideAddFavoriteGameUseCase(repository: FavoriteGameRepository): AddFavoriteGameUseCase {
        return AddFavoriteGameUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideRemoveFavoriteGameUseCase(repository: FavoriteGameRepository): RemoveFavoriteGameUseCase {
        return RemoveFavoriteGameUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideIsFavoriteGameUseCase(repository: FavoriteGameRepository): IsFavoriteGameUseCase {
        return IsFavoriteGameUseCase(repository)
    }
}
