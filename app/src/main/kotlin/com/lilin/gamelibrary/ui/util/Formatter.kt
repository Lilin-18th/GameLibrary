package com.lilin.gamelibrary.ui.util

import java.util.Locale

/**
 * 数値をフォーマット（例：1234 → 1.2k）
 */
fun formatCount(count: Int): String {
    return when {
        count >= 1000 -> {
            String.format(Locale.ROOT, "%.1f k", count / 1000.0)
        }

        else -> count.toString()
    }
}

fun Double.formatAvgRating(): String {
    return if (this > 0) {
        "%.1f".format(this)
    } else {
        "-"
    }
}

fun Double.formatAvgRatingWithStar(): String {
    return if (this > 0) {
        "★ ${"%.1f".format(this)}"
    } else {
        "-"
    }
}
