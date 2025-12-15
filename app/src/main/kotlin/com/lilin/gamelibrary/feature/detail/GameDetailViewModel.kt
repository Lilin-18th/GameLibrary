package com.lilin.gamelibrary.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilin.gamelibrary.domain.usecase.GetGameDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gameDetailUseCase: GetGameDetailUseCase,
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
                .onSuccess { result ->
                    _state.value = GameDetailUiState.Success(result)
                }
                .onFailure { throwable ->
                    _state.value = GameDetailUiState.Error(throwable)
                }
        }
    }

    fun retryLoadGameDetail() {
        loadGameDetail()
    }
}
