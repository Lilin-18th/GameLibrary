package com.lilin.gamelibrary.ui.component.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.ui.theme.DetailRatingGradientEnd
import com.lilin.gamelibrary.ui.theme.DetailRatingGradientStart
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun GameBackgroundImageSectionLoading(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val imageHeight = max(280.dp, min(screenHeight * 0.4f, 400.dp))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight)
            .shimmer(shimmer),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(6.dp),
                ),
        )
    }
}

@Composable
fun GameBasicInfoSectionLoading(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(shimmer),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(28.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp),
                    ),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(20.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp),
                    ),
            )
        }
    }
}

@Composable
fun GameRatingSummarySectionLoading(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        DetailSectionHeader(
            title = stringResource(R.string.detail_section_rating_title),
            icon = Icons.Rounded.Stars,
            gradientColors = DetailRatingGradientStart to DetailRatingGradientEnd,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(shimmer),
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp),
                    ),
            )
        }
    }
}

@Composable
fun GameDetailLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(R.string.detail_loading_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
private fun GameBackgroundImageSectionLoadingPreview() {
    GameBackgroundImageSectionLoading(
        shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window),
    )
}

@Preview
@Composable
private fun GameBasicInfoSectionLoadingPreview() {
    GameBasicInfoSectionLoading(
        shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window),
    )
}

@Preview
@Composable
private fun GameRatingSummarySectionLoadingPreview() {
    GameRatingSummarySectionLoading(
        shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window),
    )
}

@Preview
@Composable
private fun GameDetailLoadingIndicatorPreview() {
    GameDetailLoadingIndicator()
}
