package com.lilin.gamelibrary.feature.discovery

import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.ui.component.discovery.SectionType

sealed interface DiscoveryUiState {
    data object InitialLoading : DiscoveryUiState
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

sealed interface DiscoveryExpandedUiState {
    data object Loading : DiscoveryExpandedUiState

    data class Success(
        val games: List<Game>,
        val totalCount: Int,
        val selectedSection: SectionType,
    ) : DiscoveryExpandedUiState

    data class Error(
        val throwable: Throwable,
    ) : DiscoveryExpandedUiState
}
