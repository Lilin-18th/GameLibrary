package com.lilin.gamelibrary.domain.model

data class FavoriteGame(
    val id: Int,
    val name: String,
    val backgroundImage: String?,
    val rating: Double,
    val metacritic: Int?,
    val released: String?,
    val addedAt: Long,
)
