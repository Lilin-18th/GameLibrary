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
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.SortDirection
import com.lilin.gamelibrary.domain.model.SortOption
import com.lilin.gamelibrary.domain.model.SortType
import com.lilin.gamelibrary.domain.model.combineSortOption
import com.lilin.gamelibrary.domain.model.toTypeAndDirection
import com.lilin.gamelibrary.ui.theme.BadgeColor
import com.lilin.gamelibrary.ui.theme.FavoriteGradientStart

@Composable
fun FavoriteSortFabMenu(
    currentSortOption: SortOption,
    isExpanded: Boolean,
    onClickFab: () -> Unit,
    onSortChange: (SortOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (currentType, currentDirection) = currentSortOption.toTypeAndDirection()

    Box(modifier = modifier) {
        if (isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onClickFab() },
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
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    SortType.entries.forEach { sortType ->
                        item {
                            FavoriteSortMenuItem(
                                sortType = sortType,
                                currentDirection = if (currentType == sortType) {
                                    currentDirection
                                } else {
                                    SortDirection.DESC
                                },
                                isSelected = currentType == sortType,
                                onClick = {
                                    val newDirection = if (currentType == sortType) {
                                        if (currentDirection == SortDirection.DESC) {
                                            SortDirection.ASC
                                        } else {
                                            SortDirection.DESC
                                        }
                                    } else {
                                        SortDirection.DESC
                                    }
                                    onSortChange(combineSortOption(sortType, newDirection))
                                },
                            )
                        }
                    }
                }
            }

            BadgedBox(
                badge = {
                    if (!isExpanded) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = BadgeColor,
                                    shape = CircleShape,
                                )
                                .padding(2.dp),
                        ) {
                            Icon(
                                imageVector = if (currentDirection == SortDirection.ASC) {
                                    Icons.Default.ArrowUpward
                                } else {
                                    Icons.Default.ArrowDownward
                                },
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White,
                            )
                        }
                    }
                },
            ) {
                FloatingActionButton(
                    onClick = onClickFab,
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
}

@Composable
private fun FavoriteSortMenuItem(
    sortType: SortType,
    currentDirection: SortDirection,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = FavoriteGradientStart,
        tonalElevation = if (isSelected) 8.dp else 6.dp,
        modifier = modifier
            .widthIn(min = 140.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedContent(
                targetState = sortType,
                transitionSpec = {
                    fadeIn(tween(150)) togetherWith fadeOut(tween(150))
                },
                label = "sort_text_animation",
            ) { sortType ->
                Text(
                    text = when (sortType) {
                        SortType.ADDED_DATE -> stringResource(R.string.favorite_sort_added_date)
                        SortType.TITLE -> stringResource(R.string.favorite_sort_title)
                        SortType.RATING -> stringResource(R.string.favorite_sort_rating)
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            AnimatedContent(
                targetState = currentDirection,
                transitionSpec = {
                    fadeIn(tween(150)) + scaleIn(tween(150)) togetherWith
                        fadeOut(tween(150)) + scaleOut(tween(150))
                },
                label = "sort_icon_animation",
            ) { direction ->
                if (isSelected) {
                    Icon(
                        imageVector = when (direction) {
                            SortDirection.ASC -> Icons.Default.ArrowUpward
                            SortDirection.DESC -> Icons.Default.ArrowDownward
                        },
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.White,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Expanded - Added Date DESC")
@Composable
private fun FavoriteSortFabMenuExpandedPreview() {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp),
    ) {
        FavoriteSortMenuItem(
            sortType = SortType.ADDED_DATE,
            currentDirection = SortDirection.DESC,
            isSelected = true,
            onClick = {},
        )
        FavoriteSortMenuItem(
            sortType = SortType.TITLE,
            currentDirection = SortDirection.DESC,
            isSelected = false,
            onClick = {},
        )
        FavoriteSortMenuItem(
            sortType = SortType.RATING,
            currentDirection = SortDirection.DESC,
            isSelected = false,
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

@Preview(showBackground = true, name = "Not Expanded")
@Composable
private fun FavoriteSortFabMenuExpanded1Preview() {
    FavoriteSortFabMenu(
        currentSortOption = SortOption.ADDED_DATE_DESC,
        isExpanded = false,
        onClickFab = {},
        onSortChange = {},
    )
}
