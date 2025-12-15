package com.lilin.gamelibrary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lilin.gamelibrary.data.local.dao.FavoriteGameDao
import com.lilin.gamelibrary.data.local.entity.FavoriteGameEntity

@Database(
    entities = [FavoriteGameEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteGameDao(): FavoriteGameDao
}
