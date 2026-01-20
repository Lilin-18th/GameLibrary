package com.lilin.gamelibrary.feature.search

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.feature.discovery.findActivity
import com.lilin.gamelibrary.ui.component.ErrorMessage
import com.lilin.gamelibrary.ui.component.LoadingScreen
import com.lilin.gamelibrary.ui.component.SearchTopBar
import com.lilin.gamelibrary.ui.component.search.SearchBottomBar
import com.lilin.gamelibrary.ui.component.search.SearchNoResultState
import com.lilin.gamelibrary.ui.component.search.SearchResultCard
import com.lilin.gamelibrary.ui.component.search.SearchResultGridCard
import com.lilin.gamelibrary.ui.component.toErrorMessage
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Serializable
object SearchScreen

fun NavGraphBuilder.navigateSearchScreen(
    isAtLeastMedium: Boolean,
    navigateToDetail: (Int) -> Unit,
) {
    composable<SearchScreen> {
        SearchScreen(
            isAtLeastMedium = isAtLeastMedium,
            navigateToDetail = { gameId ->
                navigateToDetail(gameId)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    isAtLeastMedium: Boolean,
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
            isAtLeastMedium = isAtLeastMedium,
            query = query,
            searchUiState = searchUiState,
            scrollBehavior = scrollBehavior,
            navigateToDetail = { gameId ->
                navigateToDetail(gameId)
            },
            bottomBarPadding = paddingValues.calculateBottomPadding(),
            listState = listState,
            onLoadNextPage = viewModel::loadNextPage,
            search = viewModel::search,
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    isAtLeastMedium: Boolean,
    searchUiState: SearchUiState,
    query: String,
    navigateToDetail: (Int) -> Unit,
    onLoadNextPage: () -> Unit,
    search: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    bottomBarPadding: Dp,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        when (searchUiState) {
            is SearchUiState.None -> {
                SearchEmptyState(
                    modifier = Modifier.fillMaxSize(),
                )
            }

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
                    SearchResultsHeader(
                        query = query,
                        count = searchUiState.data.size,
                        onRefresh = search,
                    )

                    if (isAtLeastMedium) {
                        SearchResultsExpandedGrid(
                            games = searchUiState.data,
                            isLoadingMore = searchUiState.isLoadingMore,
                            hasNextPage = searchUiState.hasNextPage,
                            scrollBehavior = scrollBehavior,
                            bottomBarPadding = bottomBarPadding,
                            onLoadNextPage = onLoadNextPage,
                            navigateToDetail = navigateToDetail,
                        )
                    } else {
                        SearchResultsCompactList(
                            games = searchUiState.data,
                            isLoadingMore = searchUiState.isLoadingMore,
                            hasNextPage = searchUiState.hasNextPage,
                            listState = listState,
                            scrollBehavior = scrollBehavior,
                            bottomBarPadding = bottomBarPadding,
                            onLoadNextPage = onLoadNextPage,
                            navigateToDetail = navigateToDetail,
                        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultsCompactList(
    games: List<Game>,
    isLoadingMore: Boolean,
    hasNextPage: Boolean,
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    bottomBarPadding: Dp,
    onLoadNextPage: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shouldLoadMore by remember(games, isLoadingMore, hasNextPage) {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount

            lastVisibleItem != null &&
                lastVisibleItem.index >= totalItems - 3 &&
                hasNextPage &&
                !isLoadingMore
        }
    }

    LaunchedEffect(shouldLoadMore, onLoadNextPage) {
        if (shouldLoadMore) {
            onLoadNextPage()
        }
    }

    LazyColumn(
        modifier = modifier
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
            items = games,
            key = { it.id },
        ) { game ->
            SearchResultCard(
                game = game,
                onClickItem = {
                    navigateToDetail(game.id)
                },
            )
        }

        if (isLoadingMore) {
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

        if (!isLoadingMore && !hasNextPage) {
            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultsExpandedGrid(
    games: List<Game>,
    isLoadingMore: Boolean,
    hasNextPage: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    bottomBarPadding: Dp,
    onLoadNextPage: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()

    val shouldLoadMore by remember(games, isLoadingMore, hasNextPage) {
        derivedStateOf {
            val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = gridState.layoutInfo.totalItemsCount

            lastVisibleItem != null &&
                lastVisibleItem.index >= totalItems - 3 &&
                hasNextPage &&
                !isLoadingMore
        }
    }

    LaunchedEffect(shouldLoadMore, onLoadNextPage) {
        if (shouldLoadMore) {
            onLoadNextPage()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        state = gridState,
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 16.dp,
            bottom = bottomBarPadding,
            end = 16.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = games,
            key = { it.id },
        ) { game ->
            SearchResultGridCard(
                game = game,
                onClickItem = {
                    navigateToDetail(game.id)
                },
            )
        }

        if (isLoadingMore) {
            item(span = { GridItemSpan(3) }) {
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

        if (!isLoadingMore && !hasNextPage) {
            item(span = { GridItemSpan(3) }) {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}

@Composable
private fun SearchResultsHeader(
    query: String,
    count: Int,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.search_result_header, query, count),
        )
        IconButton(onClick = { onRefresh() }) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.search_result_reload),
            )
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

@Composable
fun SearchEmptyState(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF7C4DFF),
            )

            Text(
                text = stringResource(R.string.search_empty_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = stringResource(R.string.search_empty_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@VisibleForTesting
@Composable
internal fun SearchScreenSample(
    query: String,
    searchUiState: SearchUiState,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState()

    val context = LocalContext.current
    val activity = context.findActivity()
    val windowWidthSize = calculateWindowSizeClass(activity).widthSizeClass

    val isAtLeastMedium = when (windowWidthSize) {
        WindowWidthSizeClass.Compact -> {
            false
        }

        else -> true
    }

    SearchScreen(
        isAtLeastMedium = isAtLeastMedium,
        query = query,
        searchUiState = searchUiState,
        scrollBehavior = scrollBehavior,
        navigateToDetail = {},
        bottomBarPadding = 0.dp,
        listState = listState,
        onLoadNextPage = {},
        search = {},
        modifier = modifier,
    )
}

@Preview(
    showBackground = true,
    name = "Search UiState None",
)
@Composable
private fun SearchScreenNoneStatePreview() {
    SearchScreenSample(
        query = "",
        searchUiState = SearchUiState.None,
    )
}

@Preview(
    showBackground = true,
    name = "Search UiState Loading",
)
@Composable
private fun SearchScreenLoadingStatePreview() {
    SearchScreenSample(
        query = "",
        searchUiState = SearchUiState.Loading,
    )
}

@Preview(
    showBackground = true,
    name = "Search UiState Success",
)
@Composable
private fun SearchScreenSuccessStatePreview() {
    val sampleGames = listOf(
        Game(
            id = 1,
            name = "The Witcher 3: Wild Hunt",
            imageUrl = null,
            releaseDate = "2015-05-19",
            rating = 4.5,
            ratingsCount = 15234,
            metacritic = 92,
            isTba = false,
            addedCount = 50000,
            platforms = listOf("PC", "PS4", "Xbox One"),
        ),
        Game(
            id = 2,
            name = "Cyberpunk 2077",
            imageUrl = null,
            releaseDate = "2020-12-10",
            rating = 4.2,
            ratingsCount = 8765,
            metacritic = 86,
            isTba = false,
            addedCount = 30000,
            platforms = listOf("PC", "PS5", "Xbox Series X"),
        ),
    )

    SearchScreenSample(
        query = "Sample",
        searchUiState = SearchUiState.Success(
            data = sampleGames,
        ),
    )
}

@Preview(
    showBackground = true,
    name = "Search UiState Error",
)
@Composable
private fun SearchScreenErrorStatePreview() {
    SearchScreenSample(
        query = "",
        searchUiState = SearchUiState.Error(
            throwable = Throwable("Sample error"),
        ),
    )
}
