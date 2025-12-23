package com.lilin.gamelibrary.ui.component.sectiondetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.ui.component.discovery.SectionType

@Composable
fun SectionDetailHeader(
    sectionType: SectionType,
    gameCount: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        sectionType.gradientStart.copy(alpha = 0.3f),
                        sectionType.gradientEnd.copy(alpha = 0.2f),
                        Color.Transparent,
                    ),
                ),
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = sectionType.gradientStart.copy(alpha = 0.4f),
                        spotColor = sectionType.gradientStart.copy(alpha = 0.4f),
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                sectionType.gradientStart,
                                sectionType.gradientEnd,
                            ),
                        ),
                        shape = RoundedCornerShape(16.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = sectionType.vectorImage,
                    contentDescription = stringResource(sectionType.titleId),
                    modifier = Modifier.size(36.dp),
                    tint = Color.White,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(sectionType.titleId),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = stringResource(sectionType.subTitleId),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = stringResource(R.string.section_detail_total_count, gameCount),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = sectionType.gradientStart,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SectionDetailHeaderTrendingPreview() {
    SectionDetailHeader(
        sectionType = SectionType.TRENDING,
        gameCount = 50,
    )
}

@Preview(showBackground = true)
@Composable
private fun SectionDetailHeaderHighRatedPreview() {
    SectionDetailHeader(
        sectionType = SectionType.HIGH_RATED,
        gameCount = 50,
    )
}

@Preview(showBackground = true)
@Composable
private fun SectionDetailHeaderNewReleasePreview() {
    SectionDetailHeader(
        sectionType = SectionType.NEW_RELEASE,
        gameCount = 50,
    )
}
