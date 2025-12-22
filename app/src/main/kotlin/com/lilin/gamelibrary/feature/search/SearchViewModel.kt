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

    private var currentPage = INITIAL_PAGE
    private var isLoadingMore = false

    fun search() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isEmpty()) return

        currentPage = INITIAL_PAGE
        isLoadingMore = false

        viewModelScope.launch {
            _searchUiState.value = SearchUiState.Loading

            getGameSearchUseCase(currentPage, PAGE_SIZE, currentQuery)
                .onSuccess { result ->
                    _searchUiState.value = SearchUiState.Success(
                        data = result.games,
                        hasNextPage = result.hasNextPage,
                    )
                }
                .onFailure { throwable ->
                    _searchUiState.value = SearchUiState.Error(throwable)
                }
        }
    }

    fun loadNextPage() {
        val currentState = _searchUiState.value
        if (currentState !is SearchUiState.Success) return
        if (!currentState.hasNextPage || isLoadingMore) return

        isLoadingMore = true
        currentPage++

        val currentQuery = _query.value.trim()

        viewModelScope.launch {
            _searchUiState.value = currentState.copy(isLoadingMore = true)

            getGameSearchUseCase(currentPage, PAGE_SIZE, currentQuery)
                .onSuccess { result ->
                    _searchUiState.value = SearchUiState.Success(
                        data = currentState.data + result.games,
                        hasNextPage = result.hasNextPage,
                        isLoadingMore = false,
                    )
                    isLoadingMore = false
                }
                .onFailure { throwable ->
                    currentPage--
                    _searchUiState.value = currentState.copy(isLoadingMore = false)
                    isLoadingMore = false
                }
        }
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    private companion object {
        const val INITIAL_PAGE = 1
        const val PAGE_SIZE = 50
    }
}
