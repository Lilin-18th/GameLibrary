package com.lilin.gamelibrary.ui.component

import java.io.IOException

data class ErrorMessage(
    val title: String,
    val subtitle: String,
)

fun Throwable.toErrorMessage(): ErrorMessage {
    return when {
        this is IOException -> ErrorMessage(
            title = "ネットワークエラー",
            subtitle = "インターネット接続を確認してください",
        )

        message?.contains("timeout", ignoreCase = true) == true -> ErrorMessage(
            title = "接続がタイムアウトしました",
            subtitle = "もう一度試すか、しばらく待ってください",
        )

        else -> ErrorMessage(
            title = "データの取得に失敗しました",
            subtitle = "しばらくしてからもう一度お試しください",
        )
    }
}
