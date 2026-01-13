package com.lilin.gamelibrary.feature.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.domain.usecase.GetHighMetacriticScoreGamesUseCase
import com.lilin.gamelibrary.domain.usecase.GetNewReleasesUseCase
import com.lilin.gamelibrary.domain.usecase.GetTrendingGamesUseCase
import com.lilin.gamelibrary.ui.component.discovery.SectionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val getTrendingGamesUseCase: GetTrendingGamesUseCase,
    private val getHighRatedGamesUseCase: GetHighMetacriticScoreGamesUseCase,
    private val getNewReleasesUseCase: GetNewReleasesUseCase,
) : ViewModel() {
    private val _trendingState = MutableStateFlow<DiscoveryUiState>(DiscoveryUiState.InitialLoading)
    val trendingState = _trendingState.asStateFlow()

    private val _highlyRatedState = MutableStateFlow<DiscoveryUiState>(DiscoveryUiState.InitialLoading)
    val highlyRatedState = _highlyRatedState.asStateFlow()

    private val _newReleasesState = MutableStateFlow<DiscoveryUiState>(DiscoveryUiState.InitialLoading)
    val newReleasesState = _newReleasesState.asStateFlow()

    private val _expandedUiState =
        MutableStateFlow<DiscoveryExpandedUiState>(DiscoveryExpandedUiState.Loading)
    val expandedUiState = _expandedUiState.asStateFlow()

    fun loadSectionExpanded(sectionType: SectionType) {
        viewModelScope.launch {
            _expandedUiState.value = DiscoveryExpandedUiState.Loading
            val result = when (sectionType) {
                SectionType.TRENDING -> getTrendingGamesUseCase(page = 1, pageSize = 30)
                SectionType.HIGH_RATED -> getHighRatedGamesUseCase(page = 1, pageSize = 30)
                SectionType.NEW_RELEASE -> getNewReleasesUseCase(page = 1, pageSize = 30)
            }

            result.fold(
                onSuccess = { games ->
                    _expandedUiState.value = DiscoveryExpandedUiState.Success(
                        games = games,
                        totalCount = games.size,
                        selectedSection = sectionType,
                    )
                },
                onFailure = { throwable ->
                    _expandedUiState.value = DiscoveryExpandedUiState.Error(throwable)
                },
            )
        }
    }

    fun retryTrendingGames() {
        _trendingState.value = DiscoveryUiState.Loading
        loadTrendingGames()
    }

    fun retryHighlyRatedGames() {
        _highlyRatedState.value = DiscoveryUiState.Loading
        loadHighlyRatedGames()
    }

    fun retryNewReleases() {
        _newReleasesState.value = DiscoveryUiState.Loading
        loadNewReleaseGames()
    }

    fun reloadTrendingGames() {
        val currentState = _trendingState.value

        if (currentState is DiscoveryUiState.Success) {
            _trendingState.value = DiscoveryUiState.ReLoading(currentState.data)
            reloadTrending(currentState.data)
        }
    }

    fun reloadHighlyRatedGames() {
        val currentState = _highlyRatedState.value

        if (currentState is DiscoveryUiState.Success) {
            _highlyRatedState.value = DiscoveryUiState.ReLoading(currentState.data)
            reloadHighlyRated(currentState.data)
        }
    }

    fun reloadNewReleaseGame() {
        val currentState = _newReleasesState.value

        if (currentState is DiscoveryUiState.Success) {
            _newReleasesState.value = DiscoveryUiState.ReLoading(currentState.data)
            reloadNewRelease(currentState.data)
        }
    }

    fun loadAllSections() {
        _trendingState.value = DiscoveryUiState.InitialLoading
        _highlyRatedState.value = DiscoveryUiState.InitialLoading
        _newReleasesState.value = DiscoveryUiState.InitialLoading

        loadTrendingGames()
        loadHighlyRatedGames()
        loadNewReleaseGames()
    }

    private fun loadTrendingGames() {
        viewModelScope.launch {
            getTrendingGamesUseCase(page = 1, pageSize = 4)
                .onSuccess { result ->
                    _trendingState.value = DiscoveryUiState.Success(result)
                }
                .onFailure { throwable ->
                    _trendingState.value = DiscoveryUiState.Error(throwable)
                }
        }
    }

    private fun loadHighlyRatedGames() {
        viewModelScope.launch {
            getHighRatedGamesUseCase(page = 1, pageSize = 4)
                .onSuccess { result ->
                    _highlyRatedState.value = DiscoveryUiState.Success(result)
                }
                .onFailure { throwable ->
                    _highlyRatedState.value = DiscoveryUiState.Error(throwable)
                }
        }
    }

    private fun loadNewReleaseGames() {
        viewModelScope.launch {
            getNewReleasesUseCase(page = 1, pageSize = 4)
                .onSuccess { result ->
                    _newReleasesState.value = DiscoveryUiState.Success(result)
                }
                .onFailure { throwable ->
                    _newReleasesState.value = DiscoveryUiState.Error(throwable)
                }
        }
    }

    private fun reloadTrending(currentData: List<Game>) {
        viewModelScope.launch {
            getTrendingGamesUseCase(page = 1, pageSize = 4)
                .onSuccess { result ->
                    _trendingState.value = DiscoveryUiState.Success(result)
                }
                .onFailure { throwable ->
                    _trendingState.value = DiscoveryUiState.ReLoadingError(currentData, throwable)
                }
        }
    }

    private fun reloadHighlyRated(currentData: List<Game>) {
        viewModelScope.launch {
            getHighRatedGamesUseCase(page = 1, pageSize = 4)
                .onSuccess { result ->
                    _highlyRatedState.value = DiscoveryUiState.Success(result)
                }
                .onFailure { throwable ->
                    _highlyRatedState.value =
                        DiscoveryUiState.ReLoadingError(currentData, throwable)
                }
        }
    }

    private fun reloadNewRelease(currentData: List<Game>) {
        viewModelScope.launch {
            getNewReleasesUseCase(page = 1, pageSize = 4)
                .onSuccess { result ->
                    _newReleasesState.value = DiscoveryUiState.Success(result)
                }
                .onFailure { throwable ->
                    _newReleasesState.value =
                        DiscoveryUiState.ReLoadingError(currentData, throwable)
                }
        }
    }
}
