package com.lilin.gamelibrary.ui.util

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
