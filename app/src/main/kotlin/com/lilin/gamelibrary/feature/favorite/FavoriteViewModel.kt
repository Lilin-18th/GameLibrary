package com.lilin.gamelibrary.feature.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.domain.model.FavoriteStatistics
import com.lilin.gamelibrary.domain.model.SortOption
import com.lilin.gamelibrary.domain.model.SortOrder
import com.lilin.gamelibrary.domain.usecase.favorite.GetFavoriteGamesUseCase
import com.lilin.gamelibrary.domain.usecase.favorite.RemoveFavoriteGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteGamesUseCase: GetFavoriteGamesUseCase,
    private val removeFavoriteGameUseCase: RemoveFavoriteGameUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    private val _expandedUiState =
        MutableStateFlow<FavoriteExpandedUiState>(FavoriteExpandedUiState.Empty)
    val expandedUiState: StateFlow<FavoriteExpandedUiState> = _expandedUiState.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST_FIRST)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    private val _selectedGameId = MutableStateFlow(0)
    val selectedGameId: StateFlow<Int> = _selectedGameId.asStateFlow()

    private val _selectedGameName = MutableStateFlow("")
    val selectedGameName: StateFlow<String> = _selectedGameName.asStateFlow()

    init {
        getFavoriteGames(SortOrder.NEWEST_FIRST)
        loadFavoriteGamesExpanded(SortOption.ADDED_DATE_DESC)
    }

    private fun getFavoriteGames(sortOrder: SortOrder) {
        _uiState.value = FavoriteUiState.Loading

        viewModelScope.launch {
            runCatching {
                getFavoriteGamesUseCase.invoke(sortOrder).collect { games ->
                    _uiState.value = if (games.isEmpty()) {
                        FavoriteUiState.Empty
                    } else {
                        _sortOrder.value = sortOrder
                        FavoriteUiState.Success(games, sortOrder)
                    }
                }
            }.onFailure {
                _uiState.value = FavoriteUiState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun toggleSortOrder() {
        val currentState = _uiState.value
        if (currentState is FavoriteUiState.Success) {
            val newOrder = when (currentState.sortOrder) {
                SortOrder.NEWEST_FIRST -> SortOrder.OLDEST_FIRST
                SortOrder.OLDEST_FIRST -> SortOrder.NEWEST_FIRST
            }
            getFavoriteGames(newOrder)
        }
    }

    fun removeFavoriteGame(gameId: Int) {
        viewModelScope.launch {
            runCatching {
                removeFavoriteGameUseCase(gameId)
            }.onFailure {
                _uiState.value = FavoriteUiState.Error(it.message ?: "Unknown error")
            }
        }
    }

    // Expanded
    private fun loadFavoriteGamesExpanded(sortOption: SortOption) {
        _expandedUiState.value = FavoriteExpandedUiState.Loading

        viewModelScope.launch {
            runCatching {
                getFavoriteGamesUseCase.invoke2(sortOption).collect { games ->
                    if (games.isEmpty()) {
                        _expandedUiState.value = FavoriteExpandedUiState.Empty
                    } else {
                        // 統計計算
                        val statistics = calculateStatistics(games)

                        _expandedUiState.value = FavoriteExpandedUiState.Success(
                            games = games,
                            statistics = statistics,
                            sortOption = sortOption,
                        )
                    }
                }
            }.onFailure {
                _expandedUiState.value =
                    FavoriteExpandedUiState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun showDeleteConfirmDialog(gameId: Int) {
        val gameName = when (val state = _expandedUiState.value) {
            is FavoriteExpandedUiState.Success -> {
                state.games.find { it.id == gameId }?.name ?: ""
            }
            else -> {
                when (val compactState = _uiState.value) {
                    is FavoriteUiState.Success -> {
                        compactState.games.find { it.id == gameId }?.name ?: ""
                    }
                    else -> ""
                }
            }
        }

        _selectedGameId.value = gameId
        _selectedGameName.value = gameName
        _showDeleteDialog.value = true
    }

    fun dismissDeleteDialog() {
        _showDeleteDialog.value = false
        _selectedGameId.value = 0
        _selectedGameName.value = ""
    }

    fun confirmDelete() {
        val gameId = _selectedGameId.value
        if (gameId > 0) {
            viewModelScope.launch {
                removeFavoriteGameUseCase(gameId)
            }
        }
        dismissDeleteDialog()
    }

    fun onSortChange(sortOption: SortOption) {
        loadFavoriteGamesExpanded(sortOption)
    }

    fun calculateStatistics(games: List<FavoriteGame>): FavoriteStatistics {
        if (games.isEmpty()) {
            return FavoriteStatistics(
                totalCount = 0,
                avgRating = 0.0,
                latestAdded = "-",
            )
        }
        val totalCount = games.size
        val avgRating = games.map { it.rating }.average()
        val latestGame = games.maxByOrNull { it.addedAt }
        val latestAdded = latestGame?.let {
            formatRelativeTime(it.addedAt)
        } ?: "-"

        return FavoriteStatistics(
            totalCount = totalCount,
            avgRating = avgRating,
            latestAdded = latestAdded,
        )
    }

    /**
     * タイムスタンプを相対時間に変換
     *
     * @param timestamp Unix timestamp (milliseconds)
     * @return 相対時間文字列（例: "2日前", "1週間前"）
     */
    @Suppress("MagicNumber")
    private fun formatRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            seconds < 60 -> "たった今"
            minutes < 60 -> "${minutes}分前"
            hours < 24 -> "${hours}時間前"
            days < 7 -> "${days}日前"
            days < 30 -> "${days / 7}週間前"
            days < 365 -> "${days / 30}ヶ月前"
            else -> "${days / 365}年前"
        }
    }
}
