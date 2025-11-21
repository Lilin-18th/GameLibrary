package com.lilin.gamelibrary.feature.discovery

import com.lilin.gamelibrary.domain.model.Game

sealed interface DiscoveryUiState {
    data object Loading : DiscoveryUiState

    data class Success(
        val data: List<Game>,
    ) : DiscoveryUiState

    data class Error(
        val throwable: Throwable,
    ) : DiscoveryUiState

    data class ReLoading(
        val currentData: List<Game>,
    ) : DiscoveryUiState

    data class ReLoadingError(
        val currentData: List<Game>,
        val throwable: Throwable,
    ) : DiscoveryUiState
}
