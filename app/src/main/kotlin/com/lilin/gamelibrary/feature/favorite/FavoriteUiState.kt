package com.lilin.gamelibrary.feature.favorite

import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.domain.model.SortOrder

sealed interface FavoriteUiState {
    data object Empty : FavoriteUiState

    data object Loading : FavoriteUiState

    data class Success(
        val games: List<FavoriteGame>,
        val sortOrder: SortOrder,
    ) : FavoriteUiState

    data class Error(val message: String) : FavoriteUiState
}
