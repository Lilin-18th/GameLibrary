package com.lilin.gamelibrary.feature.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilin.gamelibrary.domain.model.SortOrder
import com.lilin.gamelibrary.domain.usecase.favorite.GetFavoriteGamesUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.RemoveFavoriteGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteGamesUseCase: GetFavoriteGamesUseCase,
    private val removeFavoriteGameUseCase: RemoveFavoriteGameUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST_FIRST)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    init {
        getFavoriteGames(SortOrder.NEWEST_FIRST)
    }

    private fun getFavoriteGames(sortOrder: SortOrder) {
        _uiState.value = FavoriteUiState.Loading

        viewModelScope.launch {
            runCatching {
                getFavoriteGamesUseCase(sortOrder).collect { games ->
                    _uiState.value = if (games.isEmpty()) {
                        FavoriteUiState.Empty
                    } else {
                        _sortOrder.value = sortOrder
                        FavoriteUiState.Success(games, sortOrder)
                    }
                }
            }.onFailure {
                _uiState.value = FavoriteUiState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun toggleSortOrder() {
        val currentState = _uiState.value
        if (currentState is FavoriteUiState.Success) {
            val newOrder = when (currentState.sortOrder) {
                SortOrder.NEWEST_FIRST -> SortOrder.OLDEST_FIRST
                SortOrder.OLDEST_FIRST -> SortOrder.NEWEST_FIRST
            }
            getFavoriteGames(newOrder)
        }
    }

    fun removeFavoriteGame(gameId: Int) {
        viewModelScope.launch {
            runCatching {
                removeFavoriteGameUseCase(gameId)
            }.onFailure {
                _uiState.value = FavoriteUiState.Error(it.message ?: "Unknown error")
            }
        }
    }
}
