package com.lilin.gamelibrary.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilin.gamelibrary.domain.usecase.GetGameSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getGameSearchUseCase: GetGameSearchUseCase,
) : ViewModel() {
    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.None)
    val searchUiState = _searchUiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    fun search() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isEmpty()) return

        viewModelScope.launch {
            _searchUiState.value = SearchUiState.Loading

            getGameSearchUseCase(1, 50, currentQuery)
                .onSuccess { result ->
                    _searchUiState.value = SearchUiState.Success(result)
                }
                .onFailure { throwable ->
                    _searchUiState.value = SearchUiState.Error(throwable)
                }
        }
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }
}
