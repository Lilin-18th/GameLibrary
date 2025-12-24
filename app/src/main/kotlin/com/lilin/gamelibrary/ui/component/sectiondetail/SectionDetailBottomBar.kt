package com.lilin.gamelibrary.ui.component.sectiondetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.feature.sectiondetail.DisplayMode

@Composable
fun SectionDetailBottomBar(
    currentDisplayMode: DisplayMode,
    onBackClick: () -> Unit,
    onDisplayModeChange: (DisplayMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.surface,
                        ),
                    ),
                ),
        )

        BottomAppBar(
            modifier = Modifier.height(90.dp),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 0.dp,
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 60.dp)
                    .fillMaxSize()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(32.dp),
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(32.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.detail_bottom_bar_back),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        IconButton(
                            onClick = { onDisplayModeChange(DisplayMode.GRID_COLUMN) },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.GridView,
                                contentDescription = "2列グリッド",
                                tint = if (currentDisplayMode == DisplayMode.GRID_COLUMN) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                            )
                        }

                        IconButton(
                            onClick = { onDisplayModeChange(DisplayMode.LIST) },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ViewList,
                                contentDescription = "リスト表示",
                                tint = if (currentDisplayMode == DisplayMode.LIST) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SectionDetailBottomBarListPreview() {
    SectionDetailBottomBar(
        currentDisplayMode = DisplayMode.LIST,
        onBackClick = {},
        onDisplayModeChange = {},
    )
}

@Preview
@Composable
private fun SectionDetailBottomBarGridPreview() {
    SectionDetailBottomBar(
        currentDisplayMode = DisplayMode.GRID_COLUMN,
        onBackClick = {},
        onDisplayModeChange = {},
    )
}
