package com.lilin.gamelibrary.ui.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.RippleConfiguration

val TrendingRippleConfiguration = RippleConfiguration(
    color = TrendingGradientStart,
    rippleAlpha = RippleAlpha(
        draggedAlpha = 0.16f,
        focusedAlpha = 0.12f,
        hoveredAlpha = 0.08f,
        pressedAlpha = 0.12f,
    ),
)

val HighRatedRippleConfiguration = RippleConfiguration(
    color = HighRatedGradientStart,
    rippleAlpha = RippleAlpha(
        draggedAlpha = 0.16f,
        focusedAlpha = 0.12f,
        hoveredAlpha = 0.08f,
        pressedAlpha = 0.12f,
    ),
)

val NewReleaseRippleConfiguration = RippleConfiguration(
    color = NewReleaseGradientStart,
    rippleAlpha = RippleAlpha(
        draggedAlpha = 0.16f,
        focusedAlpha = 0.12f,
        hoveredAlpha = 0.08f,
        pressedAlpha = 0.12f,
    ),
)
