package com.lilin.gamelibrary.domain.model

import com.lilin.gamelibrary.R

enum class SortOption {
    ADDED_DATE_DESC,
    ADDED_DATE_ASC,
    TITLE_ASC,
    TITLE_DESC,
    RATING_DESC,
    RATING_ASC
    ;

    val label: Int
        get() = when (this) {
            ADDED_DATE_DESC -> R.string.favorite_added_date_desc
            ADDED_DATE_ASC -> R.string.favorite_added_date_asc
            TITLE_ASC -> R.string.favorite_title_asc
            TITLE_DESC -> R.string.favorite_title_desc
            RATING_DESC -> R.string.favorite_rating_desc
            RATING_ASC -> R.string.favorite_rating_asc
        }
}
