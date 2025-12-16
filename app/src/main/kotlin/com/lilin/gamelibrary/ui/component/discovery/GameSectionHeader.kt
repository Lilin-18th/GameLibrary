package com.lilin.gamelibrary.ui.component.discovery

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.NewReleases
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.ui.theme.HighRatedGradientEnd
import com.lilin.gamelibrary.ui.theme.HighRatedGradientStart
import com.lilin.gamelibrary.ui.theme.NewReleaseGradientEnd
import com.lilin.gamelibrary.ui.theme.NewReleaseGradientStart
import com.lilin.gamelibrary.ui.theme.TrendingGradientEnd
import com.lilin.gamelibrary.ui.theme.TrendingGradientStart

@Composable
fun GameSectionHeader(
    sectionType: SectionType,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(sectionType.gradientStart, sectionType.gradientEnd),
                    ),
                    shape = RoundedCornerShape(12.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = sectionType.vectorImage,
                contentDescription = stringResource(sectionType.titleId),
                modifier = Modifier.size(28.dp),
                tint = Color.White,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(sectionType.titleId),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
            )

            Text(
                text = stringResource(sectionType.subTitleId),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

enum class SectionType(
    @param:StringRes val titleId: Int,
    @param:StringRes val subTitleId: Int,
    val vectorImage: ImageVector,
    val gradientStart: Color,
    val gradientEnd: Color,
) {
    TRENDING(
        titleId = R.string.discovery_trending_section_title,
        subTitleId = R.string.discover_section_trending_sub_title,
        vectorImage = Icons.Rounded.LocalFireDepartment,
        gradientStart = TrendingGradientStart,
        gradientEnd = TrendingGradientEnd,
    ),
    HIGH_RATED(
        titleId = R.string.discovery_metacritic_section_title,
        subTitleId = R.string.discover_section_high_rated_sub_title,
        vectorImage = Icons.Rounded.Stars,
        gradientStart = HighRatedGradientStart,
        gradientEnd = HighRatedGradientEnd,
    ),
    NEW_RELEASE(
        titleId = R.string.discovery_new_release_section_title,
        subTitleId = R.string.discover_section_new_release_sub_title,
        vectorImage = Icons.Rounded.NewReleases,
        gradientStart = NewReleaseGradientStart,
        gradientEnd = NewReleaseGradientEnd,
    ),
}

@Preview(
    name = "Trending",
    showBackground = true,
)
@Composable
private fun GameSectionHeaderTrendingPreview() {
    GameSectionHeader(
        sectionType = SectionType.TRENDING,
    )
}

@Preview(
    name = "High Rated",
    showBackground = true,
)
@Composable
private fun GameSectionHeaderHighRatedPreview() {
    GameSectionHeader(
        sectionType = SectionType.HIGH_RATED,
    )
}

@Preview(
    name = "New Release",
    showBackground = true,
)
@Composable
private fun GameSectionHeaderNewReleasePreview() {
    GameSectionHeader(
        sectionType = SectionType.NEW_RELEASE,
    )
}
