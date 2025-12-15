package com.lilin.gamelibrary.feature.detail

import com.lilin.gamelibrary.domain.model.GameDetail

sealed interface GameDetailUiState {
    data object Loading : GameDetailUiState

    data class Success(
        val gameDetail: GameDetail,
        val isFavorite: Boolean,
    ) : GameDetailUiState

    data class Error(
        val throwable: Throwable,
    ) : GameDetailUiState
}
