package com.lilin.gamelibrary.feature.search

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lilin.gamelibrary.ui.component.ErrorMessage
import com.lilin.gamelibrary.ui.component.LoadingScreen
import com.lilin.gamelibrary.ui.component.SearchTopBar
import com.lilin.gamelibrary.ui.component.search.SearchBottomBar
import com.lilin.gamelibrary.ui.component.search.SearchNoResultState
import com.lilin.gamelibrary.ui.component.search.SearchResultCard
import com.lilin.gamelibrary.ui.component.toErrorMessage
import kotlinx.coroutines.delay
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState()

    var isBottomBarVisible by remember { mutableStateOf(true) }

    val isScrollInProgress = listState.isScrollInProgress

    LaunchedEffect(listState) {
        var previousIndex = listState.firstVisibleItemIndex
        var previousOffset = listState.firstVisibleItemScrollOffset

        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            val isScrollingDown = when {
                index > previousIndex -> true
                index < previousIndex -> false
                else -> offset > previousOffset
            }
            if (listState.isScrollInProgress && isScrollingDown) {
                isBottomBarVisible = false
            } else if (listState.isScrollInProgress && !isScrollingDown) {
                isBottomBarVisible = true
            }

            previousIndex = index
            previousOffset = offset
        }
    }

    LaunchedEffect(isScrollInProgress) {
        if (!isScrollInProgress) {
            delay(500)
            isBottomBarVisible = true
        }
    }

    Scaffold(
        topBar = {
            SearchTopBar(scrollBehavior = scrollBehavior)
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                SearchBottomBar(
                    query = query,
                    onQueryChange = viewModel::onQueryChange,
                    onSearch = viewModel::search,
                    modifier = Modifier.padding(bottom = 10.dp),
                )
            }
        },
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        SearchScreen(
            query = query,
            searchUiState = searchUiState,
            scrollBehavior = scrollBehavior,
            navigateToDetail = { gameId ->
                navigateToDetail(gameId)
            },
            bottomBarPadding = paddingValues.calculateBottomPadding(),
            listState = listState,
            onLoadNextPage = viewModel::loadNextPage,
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    searchUiState: SearchUiState,
    query: String,
    navigateToDetail: (Int) -> Unit,
    onLoadNextPage: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    bottomBarPadding: Dp,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        when (searchUiState) {
            is SearchUiState.None -> {}

            is SearchUiState.Loading -> {
                LoadingScreen()
            }

            is SearchUiState.Success -> {
                if (searchUiState.data.isEmpty()) {
                    SearchNoResultState(
                        query = query,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    val shouldLoadMore by remember(searchUiState) {
                        derivedStateOf {
                            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                            val totalItems = listState.layoutInfo.totalItemsCount

                            lastVisibleItem != null &&
                                lastVisibleItem.index >= totalItems - 3 &&
                                searchUiState.hasNextPage &&
                                !searchUiState.isLoadingMore
                        }
                    }

                    LaunchedEffect(shouldLoadMore, onLoadNextPage) {
                        if (shouldLoadMore) {
                            onLoadNextPage()
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        state = listState,
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            start = 20.dp,
                            bottom = bottomBarPadding,
                            end = 20.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
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

                        if (searchUiState.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                        }

                        if (!searchUiState.isLoadingMore && !searchUiState.hasNextPage) {
                            item {
                                Spacer(modifier = Modifier.height(90.dp))
                            }
                        }
                    }
                }
            }

            is SearchUiState.Error -> {
                FullScreenError(
                    error = searchUiState.throwable.toErrorMessage(),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun FullScreenError(
    error: ErrorMessage,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error,
            )

            Text(
                text = stringResource(error.title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = stringResource(error.subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@VisibleForTesting
@Composable
internal fun SearchScreenSample(
    query: String,
    searchUiState: SearchUiState,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState()

    SearchScreen(
        query = query,
        searchUiState = searchUiState,
        scrollBehavior = scrollBehavior,
        navigateToDetail = {},
        bottomBarPadding = 0.dp,
        listState = listState,
        onLoadNextPage = {},
        modifier = modifier,
    )
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        navigateToDetail = {},
    )
}
