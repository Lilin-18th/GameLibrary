package com.lilin.gamelibrary.ui.component.favorite.expanded

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.SortOption
import com.lilin.gamelibrary.ui.theme.FavoriteGradientEnd
import com.lilin.gamelibrary.ui.theme.GameLibraryTheme

@Composable
fun SortSection(
    currentSort: SortOption,
    onSortChange: (SortOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.favorite_sort),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        SortOption.entries.forEach { option ->
            SortOptionItem(
                option = option,
                isSelected = option == currentSort,
                onClick = { onSortChange(option) },
            )
        }
    }
}

@Composable
private fun SortOptionItem(
    option: SortOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = FavoriteGradientEnd,
            ),
        )

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = option.label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SortSectionPreview() {
    GameLibraryTheme {
        SortSection(
            currentSort = SortOption.ADDED_DATE_DESC,
            onSortChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
