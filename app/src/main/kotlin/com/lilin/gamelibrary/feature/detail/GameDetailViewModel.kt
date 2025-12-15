package com.lilin.gamelibrary.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.domain.model.GameDetail
import com.lilin.gamelibrary.domain.usecase.GetGameDetailUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.AddFavoriteGameUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.IsFavoriteGameUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.RemoveFavoriteGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gameDetailUseCase: GetGameDetailUseCase,
    private val isFavoriteGameUseCase: IsFavoriteGameUseCase,
    private val addFavoriteGameUseCase: AddFavoriteGameUseCase,
    private val removeFavoriteGameUseCase: RemoveFavoriteGameUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<GameDetailUiState>(GameDetailUiState.Loading)
    val state = _state.asStateFlow()

    val gameId = checkNotNull(savedStateHandle.get<Int>("gameId"))

    init {
        loadGameDetail()
    }

    private fun loadGameDetail() {
        _state.value = GameDetailUiState.Loading
        viewModelScope.launch {
            gameDetailUseCase(gameId)
                .onSuccess { gameDetail ->
                    observeFavoriteState(gameDetail)
                }
                .onFailure { throwable ->
                    _state.value = GameDetailUiState.Error(throwable)
                }
        }
    }

    private fun observeFavoriteState(gameDetail: GameDetail) {
        viewModelScope.launch {
            isFavoriteGameUseCase(gameId).collect { isFavorite ->
                _state.value = GameDetailUiState.Success(
                    gameDetail = gameDetail,
                    isFavorite = isFavorite,
                )
            }
        }
    }

    private fun GameDetail.toFavoriteGame(): FavoriteGame {
        return FavoriteGame(
            id = id,
            name = name,
            backgroundImage = backgroundImage,
            rating = rating,
            metacritic = metacritic,
            released = releaseDate,
            addedAt = System.currentTimeMillis(),
        )
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is GameDetailUiState.Success) {
                if (currentState.isFavorite) {
                    // お気に入りから削除
                    removeFavoriteGameUseCase(gameId)
                } else {
                    // お気に入りに追加
                    val favoriteGame = currentState.gameDetail.toFavoriteGame()
                    addFavoriteGameUseCase(favoriteGame)
                }
            }
        }
    }

    fun retryLoadGameDetail() {
        loadGameDetail()
    }
}
