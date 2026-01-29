package com.lilin.gamelibrary.domain.model

import com.lilin.gamelibrary.R

enum class SortType {
    ADDED_DATE,
    TITLE,
    RATING,
    ;
}

enum class SortDirection {
    DESC,
    ASC,
    ;
}

enum class SortOption {
    ADDED_DATE_DESC,
    ADDED_DATE_ASC,
    TITLE_ASC,
    TITLE_DESC,
    RATING_DESC,
    RATING_ASC,
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

fun SortOption.toTypeAndDirection(): Pair<SortType, SortDirection> {
    return when (this) {
        SortOption.ADDED_DATE_DESC -> Pair(SortType.ADDED_DATE, SortDirection.DESC)
        SortOption.ADDED_DATE_ASC -> Pair(SortType.ADDED_DATE, SortDirection.ASC)
        SortOption.TITLE_DESC -> Pair(SortType.TITLE, SortDirection.DESC)
        SortOption.TITLE_ASC -> Pair(SortType.TITLE, SortDirection.ASC)
        SortOption.RATING_DESC -> Pair(SortType.RATING, SortDirection.DESC)
        SortOption.RATING_ASC -> Pair(SortType.RATING, SortDirection.ASC)
    }
}

fun combineSortOption(
    type: SortType,
    direction: SortDirection,
): SortOption {
    return when (type) {
        SortType.ADDED_DATE -> {
            if (direction == SortDirection.ASC) {
                SortOption.ADDED_DATE_ASC
            } else {
                SortOption.ADDED_DATE_DESC
            }
        }

        SortType.TITLE -> {
            if (direction == SortDirection.ASC) {
                SortOption.TITLE_ASC
            } else {
                SortOption.TITLE_DESC
            }
        }

        SortType.RATING -> {
            if (direction == SortDirection.ASC) {
                SortOption.RATING_ASC
            } else {
                SortOption.RATING_DESC
            }
        }
    }
}
