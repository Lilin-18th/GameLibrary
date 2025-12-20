package com.lilin.gamelibrary.ui.component

import androidx.annotation.StringRes
import com.lilin.gamelibrary.R
import java.io.IOException

data class ErrorMessage(
    @param:StringRes val title: Int,
    @param:StringRes val subtitle: Int,
)

fun Throwable.toErrorMessage(): ErrorMessage {
    return when {
        this is IOException -> ErrorMessage(
            title = R.string.common_network_error_title,
            subtitle = R.string.common_network_error_subtitle,
        )

        message?.contains("timeout", ignoreCase = true) == true -> ErrorMessage(
            title = R.string.common_timeout_error_title,
            subtitle = R.string.common_timeout_error_subtitle,
        )

        else -> ErrorMessage(
            title = R.string.common_unknown_error_title,
            subtitle = R.string.common_unknown_error_subtitle,
        )
    }
}
