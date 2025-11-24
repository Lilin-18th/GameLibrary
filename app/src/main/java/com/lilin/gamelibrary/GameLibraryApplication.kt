package com.lilin.gamelibrary

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GameLibraryApplication : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        SingletonImageLoader.setSafe { newImageLoader(it) }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
}
