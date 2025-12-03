package com.lilin.gamelibrary.di

import com.lilin.gamelibrary.domain.provider.DateTimeProvider
import com.lilin.gamelibrary.domain.provider.DefaultDateTimeProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DateTimeModule {

    @Provides
    @Singleton
    fun provideDateTimeProvider(): DateTimeProvider {
        return DefaultDateTimeProvider()
    }
}
