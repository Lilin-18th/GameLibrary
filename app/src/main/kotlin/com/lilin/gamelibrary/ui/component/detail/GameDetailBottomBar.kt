package com.lilin.gamelibrary.ui.component.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.ui.component.BottomBarTemplate

@Composable
fun GameDetailBottomBar(
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    isSuccessState: Boolean,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
) {
    BottomBarTemplate(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag("BackKey"),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.detail_bottom_bar_back),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onFavoriteClick,
                enabled = isSuccessState,
                modifier = Modifier.testTag("FavoriteButton"),
            ) {
                Icon(
                    imageVector = if (isFavorite) {
                        Icons.Rounded.Favorite
                    } else {
                        Icons.Rounded.FavoriteBorder
                    },
                    contentDescription = stringResource(R.string.detail_bottom_bar_favorite),
                    tint = if (isSuccessState) {
                        if (isFavorite) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    },
                )
            }

            IconButton(
                onClick = onShareClick,
                enabled = isSuccessState,
                modifier = Modifier.testTag("ShareButton"),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Share,
                    contentDescription = stringResource(R.string.detail_bottom_bar_share),
                    tint = if (isSuccessState) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameDetailBottomBarPreview() {
    GameDetailBottomBar(
        onBackClick = {},
        onFavoriteClick = {},
        onShareClick = {},
        isSuccessState = false,
        isFavorite = false,
    )
}

@Preview
@Composable
private fun GameDetailBottomBarFavoritePreview() {
    GameDetailBottomBar(
        onBackClick = {},
        onFavoriteClick = {},
        onShareClick = {},
        isSuccessState = true,
        isFavorite = true,
    )
}
