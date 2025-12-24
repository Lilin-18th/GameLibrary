package com.lilin.gamelibrary.feature.sectiondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lilin.gamelibrary.domain.usecase.GetHighMetacriticScoreGamesUseCase
import com.lilin.gamelibrary.domain.usecase.GetNewReleasesUseCase
import com.lilin.gamelibrary.domain.usecase.GetTrendingGamesUseCase
import com.lilin.gamelibrary.ui.component.discovery.SectionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SectionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTrendingGamesUseCase: GetTrendingGamesUseCase,
    private val getHighRatedGamesUseCase: GetHighMetacriticScoreGamesUseCase,
    private val getNewReleasesUseCase: GetNewReleasesUseCase,
) : ViewModel() {
    private val args = savedStateHandle.toRoute<SectionDetailScreen>()
    val sectionType = SectionType.valueOf(args.sectionTypeName)

    private val _uiState = MutableStateFlow<SectionDetailUiState>(SectionDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _displayMode = MutableStateFlow(DisplayMode.GRID_COLUMN)
    val displayMode: StateFlow<DisplayMode> = _displayMode.asStateFlow()

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _uiState.value = SectionDetailUiState.Loading

            when (sectionType) {
                SectionType.TRENDING -> {
                    getTrendingGamesUseCase(page = 1, pageSize = 50)
                        .onSuccess { result ->
                            _uiState.value = SectionDetailUiState.Success(
                                data = result,
                                totalCount = result.size,
                            )
                        }
                        .onFailure { throwable ->
                            _uiState.value = SectionDetailUiState.Error(throwable)
                        }
                }

                SectionType.HIGH_RATED -> {
                    getHighRatedGamesUseCase(page = 1, pageSize = 50)
                        .onSuccess { result ->
                            _uiState.value = SectionDetailUiState.Success(
                                data = result,
                                totalCount = result.size,
                            )
                        }
                        .onFailure { throwable ->
                            _uiState.value = SectionDetailUiState.Error(throwable)
                        }
                }

                SectionType.NEW_RELEASE -> {
                    getNewReleasesUseCase(page = 1, pageSize = 50)
                        .onSuccess { result ->
                            _uiState.value = SectionDetailUiState.Success(
                                data = result,
                                totalCount = result.size,
                            )
                        }
                        .onFailure { throwable ->
                            _uiState.value = SectionDetailUiState.Error(throwable)
                        }
                }
            }
        }
    }

    fun retry() {
        loadGames()
    }

    fun changeDisplayMode(mode: DisplayMode) {
        _displayMode.value = mode
    }
}
