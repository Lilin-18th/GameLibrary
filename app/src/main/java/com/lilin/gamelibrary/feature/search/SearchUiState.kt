package com.lilin.gamelibrary.feature.search

import com.lilin.gamelibrary.domain.model.Game

sealed interface SearchUiState {
    data object None : SearchUiState

    data object Loading : SearchUiState

    data class Success(
        val data: List<Game>,
    ) : SearchUiState

    data class Error(
        val throwable: Throwable,
    ) : SearchUiState
}
