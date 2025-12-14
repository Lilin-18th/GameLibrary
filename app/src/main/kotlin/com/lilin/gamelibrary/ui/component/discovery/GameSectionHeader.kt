package com.lilin.gamelibrary.ui.component.discovery

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GameSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    GameSectionHeader(
        title = "Popular",
    )
}
