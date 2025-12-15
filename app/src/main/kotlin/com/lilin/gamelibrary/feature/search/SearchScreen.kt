package com.lilin.gamelibrary.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lilin.gamelibrary.ui.component.SearchTopBar
import com.lilin.gamelibrary.ui.component.search.SearchField
import com.lilin.gamelibrary.ui.component.search.SearchResultCard
import kotlinx.serialization.Serializable

@Serializable
object SearchScreen

fun NavGraphBuilder.navigateSearchScreen(
    navigateToDetail: (Int) -> Unit,
) {
    composable<SearchScreen> {
        SearchScreen(
            navigateToDetail = { gameId ->
                navigateToDetail(gameId)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val searchUiState by viewModel.searchUiState.collectAsState()
    val query by viewModel.query.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            SearchTopBar(scrollBehavior = scrollBehavior)
        },
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = modifier,
    ) {
        SearchScreen(
            query = query,
            searchUiState = searchUiState,
            scrollBehavior = scrollBehavior,
            onQueryChange = viewModel::onQueryChange,
            onSearch = viewModel::search,
            navigateToDetail = { gameId ->
                navigateToDetail(gameId)
            },
            modifier = Modifier.padding(it),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    searchUiState: SearchUiState,
    query: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        SearchField(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
        )

        when (searchUiState) {
            is SearchUiState.None -> {}

            is SearchUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            is SearchUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = searchUiState.data,
                        key = { it.id },
                    ) { game ->
                        SearchResultCard(
                            game = game,
                            onClickItem = {
                                navigateToDetail(game.id)
                            },
                        )
                    }
                }
            }

            is SearchUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "エラーが発生しました",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error,
                        )

                        Text(
                            text = searchUiState.throwable.message ?: "不明なエラーが発生しました",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        navigateToDetail = {},
    )
}
