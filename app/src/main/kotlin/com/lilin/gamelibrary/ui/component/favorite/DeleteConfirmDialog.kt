package com.lilin.gamelibrary.ui.component.favorite

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.ui.theme.GameLibraryTheme

@Composable
fun DeleteConfirmDialog(
    gameName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.favorite_alert_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        text = {
            Text(
                text = stringResource(R.string.favorite_alert_dialog_message, gameName),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                ),
            ) {
                Text(text = stringResource(R.string.favorite_alert_dialog_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.favorite_alert_dialog_cancel))
            }
        },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun DeleteConfirmDialogPreview() {
    GameLibraryTheme {
        DeleteConfirmDialog(
            gameName = "Grand Theft Auto V",
            onDismiss = {},
            onConfirm = {},
        )
    }
}

@Preview
@Composable
private fun DeleteConfirmDialogLongNamePreview() {
    GameLibraryTheme {
        DeleteConfirmDialog(
            gameName = "The Legend of Zelda: Breath of the Wild - Complete Edition",
            onDismiss = {},
            onConfirm = {},
        )
    }
}
