package com.lilin.gamelibrary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_games")
data class FavoriteGameEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val backgroundImage: String?,
    val rating: Double,
    val metacritic: Int?,
    val released: String?,
    val addedAt: Long,
)
