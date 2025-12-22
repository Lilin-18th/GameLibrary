package com.lilin.gamelibrary.domain.model

data class SearchResult(
    val games: List<Game>,
    val currentPage: Int,
    val hasNextPage: Boolean,
)
