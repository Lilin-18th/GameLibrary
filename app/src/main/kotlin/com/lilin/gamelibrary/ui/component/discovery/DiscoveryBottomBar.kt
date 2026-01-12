package com.lilin.gamelibrary.ui.component.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.NewReleases
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.ui.theme.HighRatedGradientStart
import com.lilin.gamelibrary.ui.theme.HighRatedRippleConfiguration
import com.lilin.gamelibrary.ui.theme.NewReleaseGradientStart
import com.lilin.gamelibrary.ui.theme.NewReleaseRippleConfiguration
import com.lilin.gamelibrary.ui.theme.TrendingGradientStart
import com.lilin.gamelibrary.ui.theme.TrendingRippleConfiguration

@Composable
fun DiscoveryBottomBar(
    onClickTrending: () -> Unit,
    onClickHighRated: () -> Unit,
    onClickNewRelease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSourceTrending = remember { MutableInteractionSource() }
    val interactionSourceHighRated = remember { MutableInteractionSource() }
    val interactionSourceNewRelease = remember { MutableInteractionSource() }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
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
            modifier = Modifier.height(80.dp),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 0.dp,
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(90.dp),
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(90.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    CustomRippleIconButton(
                        interactionSource = interactionSourceTrending,
                        rippleConfiguration = TrendingRippleConfiguration,
                        onClick = onClickTrending,
                        modifier = Modifier
                            .size(80.dp, 44.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(32.dp),
                            ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocalFireDepartment,
                            contentDescription = stringResource(R.string.detail_bottom_bar_back),
                            tint = TrendingGradientStart,
                        )
                    }

                    CustomRippleIconButton(
                        interactionSource = interactionSourceHighRated,
                        rippleConfiguration = HighRatedRippleConfiguration,
                        onClick = onClickHighRated,
                        modifier = Modifier
                            .size(80.dp, 44.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(32.dp),
                            ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Stars,
                            contentDescription = stringResource(R.string.detail_bottom_bar_back),
                            tint = HighRatedGradientStart,
                        )
                    }

                    CustomRippleIconButton(
                        interactionSource = interactionSourceNewRelease,
                        rippleConfiguration = NewReleaseRippleConfiguration,
                        onClick = onClickNewRelease,
                        modifier = Modifier
                            .size(80.dp, 44.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(32.dp),
                            ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.NewReleases,
                            contentDescription = stringResource(R.string.detail_bottom_bar_back),
                            tint = NewReleaseGradientStart,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomRippleIconButton(
    interactionSource: MutableInteractionSource,
    rippleConfiguration: RippleConfiguration,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalRippleConfiguration provides rippleConfiguration) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
            interactionSource = interactionSource,
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun CustomRippleIconButtonPreview() {
    val interactionSource = remember { MutableInteractionSource() }
    val customRippleConfiguration = RippleConfiguration(
        color = TrendingGradientStart,
        rippleAlpha = RippleAlpha(
            draggedAlpha = 0.16f,
            focusedAlpha = 0.12f,
            hoveredAlpha = 0.08f,
            pressedAlpha = 0.12f,
        ),
    )

    CustomRippleIconButton(
        interactionSource = interactionSource,
        rippleConfiguration = customRippleConfiguration,
        onClick = {},
        modifier = Modifier
            .size(80.dp, 44.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(32.dp),
            ),
    ) {
        Icon(
            imageVector = Icons.Rounded.LocalFireDepartment,
            contentDescription = stringResource(R.string.detail_bottom_bar_back),
            tint = TrendingGradientStart,
        )
    }
}

@Preview
@Composable
private fun DiscoveryBottomBarPreview() {
    DiscoveryBottomBar(
        onClickTrending = {},
        onClickHighRated = {},
        onClickNewRelease = {},
    )
}
