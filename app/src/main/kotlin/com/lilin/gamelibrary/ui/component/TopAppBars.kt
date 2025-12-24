@file:OptIn(ExperimentalMaterial3Api::class)

package com.lilin.gamelibrary.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.ui.component.discovery.SectionType

@Composable
fun DiscoveryTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.discover_screen_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

@Composable
fun GameDetailTopAppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

@Composable
fun SectionDetailTopAppBar(
    gameCount: Int,
    sectionType: SectionType,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = stringResource(sectionType.titleId),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(sectionType.subTitleId),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.section_detail_total_count, gameCount),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = sectionType.gradientStart,
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

@Composable
fun SearchTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.search_screen_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
    )
}

@Preview
@Composable
private fun DiscoveryTopBarPreview() {
    DiscoveryTopBar(
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    )
}

@Preview
@Composable
private fun SectionDetailTopAppBarTrendingPreview() {
    SectionDetailTopAppBar(
        gameCount = 4,
        sectionType = SectionType.TRENDING,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    )
}

@Preview
@Composable
private fun SectionDetailTopAppBarHighRatedPreview() {
    SectionDetailTopAppBar(
        gameCount = 4,
        sectionType = SectionType.HIGH_RATED,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    )
}

@Preview
@Composable
private fun SectionDetailTopAppBarNewReleasePreview() {
    SectionDetailTopAppBar(
        gameCount = 4,
        sectionType = SectionType.NEW_RELEASE,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    )
}
