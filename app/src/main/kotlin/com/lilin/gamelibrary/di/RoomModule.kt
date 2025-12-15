package com.lilin.gamelibrary.di

import android.content.Context
import androidx.room.Room
import com.lilin.gamelibrary.data.local.AppDatabase
import com.lilin.gamelibrary.data.local.dao.FavoriteGameDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "game_library_database",
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteGameDao(database: AppDatabase): FavoriteGameDao {
        return database.favoriteGameDao()
    }
}
