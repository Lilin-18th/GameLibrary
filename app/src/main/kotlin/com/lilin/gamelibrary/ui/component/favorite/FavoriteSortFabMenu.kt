package com.lilin.gamelibrary.ui.component.favorite

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.SortOrder
import com.lilin.gamelibrary.ui.theme.FavoriteGradientStart

@Composable
fun FavoriteSortFabMenu(
    sortOrder: SortOrder,
    onSortClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        if (isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { isExpanded = false },
                        )
                    },
            )
        }

        Column(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(tween(200)) + slideInVertically(tween(200)) { it / 2 },
                exit = fadeOut(tween(200)) + slideOutVertically(tween(200)) { it / 2 },
            ) {
                FavoriteSortMenuItem(
                    sortOrder = sortOrder,
                    onClick = onSortClick,
                )
            }

            FloatingActionButton(
                onClick = { isExpanded = !isExpanded },
                containerColor = FavoriteGradientStart,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = "Sort Menu",
                )
            }
        }
    }
}

@Composable
private fun FavoriteSortMenuItem(
    sortOrder: SortOrder,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = FavoriteGradientStart,
        tonalElevation = 6.dp,
        modifier = Modifier.widthIn(min = 140.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedContent(
                targetState = sortOrder,
                transitionSpec = {
                    fadeIn(tween(150)) + scaleIn(tween(150)) togetherWith
                        fadeOut(tween(150)) + scaleOut(tween(150))
                },
                label = "sort_icon_animation",
            ) { order ->
                Icon(
                    imageVector = when (order) {
                        SortOrder.NEWEST_FIRST -> Icons.Default.ArrowUpward
                        SortOrder.OLDEST_FIRST -> Icons.Default.ArrowDownward
                    },
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White,
                )
            }

            AnimatedContent(
                targetState = sortOrder,
                transitionSpec = {
                    fadeIn(tween(150)) togetherWith fadeOut(tween(150))
                },
                label = "sort_text_animation",
            ) { order ->
                Text(
                    text = when (order) {
                        SortOrder.NEWEST_FIRST -> stringResource(R.string.favorite_sort_newest)
                        SortOrder.OLDEST_FIRST -> stringResource(R.string.favorite_sort_oldest)
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteSortFabMenuClosedPreview() {
    FavoriteSortFabMenu(
        sortOrder = SortOrder.NEWEST_FIRST,
        onSortClick = {},
    )
}

@Preview(showBackground = true, name = "Expanded - Newest First")
@Composable
private fun FavoriteSortFabMenuExpandedNewestPreview() {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FavoriteSortMenuItem(
            sortOrder = SortOrder.NEWEST_FIRST,
            onClick = {},
        )
        FloatingActionButton(
            onClick = {},
            containerColor = FavoriteGradientStart,
            contentColor = Color.White,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true, name = "Expanded - Oldest First")
@Composable
private fun FavoriteSortFabMenuExpandedOldestPreview() {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FavoriteSortMenuItem(
            sortOrder = SortOrder.OLDEST_FIRST,
            onClick = {},
        )
        FloatingActionButton(
            onClick = {},
            containerColor = FavoriteGradientStart,
            contentColor = Color.White,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = null,
            )
        }
    }
}
