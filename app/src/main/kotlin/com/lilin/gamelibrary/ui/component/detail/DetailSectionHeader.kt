package com.lilin.gamelibrary.ui.component.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import com.lilin.gamelibrary.ui.theme.DetailDescriptionGradientEnd
import com.lilin.gamelibrary.ui.theme.DetailDescriptionGradientStart
import com.lilin.gamelibrary.ui.theme.DetailRatingGradientEnd
import com.lilin.gamelibrary.ui.theme.DetailRatingGradientStart
import com.lilin.gamelibrary.ui.theme.DetailTagGradientEnd
import com.lilin.gamelibrary.ui.theme.DetailTagGradientStart

@Composable
fun DetailSectionHeader(
    title: String,
    icon: ImageVector,
    gradientColors: Pair<Color, Color>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(gradientColors.first, gradientColors.second),
                    ),
                    shape = RoundedCornerShape(12.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp),
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            modifier = Modifier,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

@Preview
@Composable
private fun DetailSectionHeaderRatingPreview() {
    DetailSectionHeader(
        title = stringResource(R.string.detail_section_rating_title),
        icon = Icons.Filled.Star,
        gradientColors = Pair(DetailRatingGradientStart, DetailRatingGradientEnd),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview
@Composable
private fun DetailSectionHeaderDescriptionPreview() {
    DetailSectionHeader(
        title = stringResource(R.string.detail_section_description_title),
        icon = Icons.Filled.Star,
        gradientColors = Pair(DetailDescriptionGradientStart, DetailDescriptionGradientEnd),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview
@Composable
private fun DetailSectionHeaderTagsPreview() {
    DetailSectionHeader(
        title = stringResource(R.string.detail_section_tags_title),
        icon = Icons.Filled.Star,
        gradientColors = Pair(DetailTagGradientStart, DetailTagGradientEnd),
        modifier = Modifier.fillMaxWidth(),
    )
}
