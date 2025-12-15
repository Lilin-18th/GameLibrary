package com.lilin.gamelibrary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lilin.gamelibrary.data.local.entity.FavoriteGameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteGameDao {
    // 新しい順
    @Query("SELECT * FROM favorite_games ORDER BY addedAt DESC")
    fun getFavoriteGamesNewestFirst(): Flow<List<FavoriteGameEntity>>

    // 古い順
    @Query("SELECT * FROM favorite_games ORDER BY addedAt ASC")
    fun getFavoriteGamesOldestFirst(): Flow<List<FavoriteGameEntity>>

    // お気に入り判定用
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_games WHERE id = :gameId)")
    fun isFavorite(gameId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteGame(game: FavoriteGameEntity)

    @Query("DELETE FROM favorite_games WHERE id = :gameId")
    suspend fun deleteFavoriteGame(gameId: Int)
}
