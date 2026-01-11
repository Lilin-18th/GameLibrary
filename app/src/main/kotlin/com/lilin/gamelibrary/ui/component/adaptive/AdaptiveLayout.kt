package com.lilin.gamelibrary.ui.component.adaptive

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

@Composable
fun AdaptiveLayout(
    windowWidthSizeClass: WindowWidthSizeClass,
    compactContent: @Composable () -> Unit,
    mediumContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
) {
    when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            compactContent()
        }

        WindowWidthSizeClass.Medium -> {
            mediumContent()
        }

        WindowWidthSizeClass.Expanded -> {
            expandedContent()
        }
    }
}
