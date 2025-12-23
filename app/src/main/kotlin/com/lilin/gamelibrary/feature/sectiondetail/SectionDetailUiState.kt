package com.lilin.gamelibrary.feature.sectiondetail

import com.lilin.gamelibrary.domain.model.Game

sealed interface SectionDetailUiState {
    data object Loading : SectionDetailUiState

    data class Success(
        val data: List<Game>,
        val totalCount: Int,
    ) : SectionDetailUiState

    data class Error(
        val throwable: Throwable,
    ) : SectionDetailUiState
}
