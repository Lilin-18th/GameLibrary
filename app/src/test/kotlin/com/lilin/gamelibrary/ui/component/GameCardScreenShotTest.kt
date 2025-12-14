package com.lilin.gamelibrary.ui.component

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ColorImage
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.asImage
import coil3.test.FakeImageLoaderEngine
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.util.captureMultiDevice
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class GameCardScreenShotTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @OptIn(DelicateCoilApi::class)
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val drawable = context.getDrawable(R.drawable.ic_test_image)!!
        val engine = FakeImageLoaderEngine.Builder()
            .intercept(
                "https://example.com/image.jpg",
                drawable.asImage(),
            )
            .default(ColorImage(Color.Blue.toArgb()))
            .build()

        val imageLoader = ImageLoader.Builder(context)
            .components { add(engine) }
            .build()

        SingletonImageLoader.setUnsafe(imageLoader)
    }

    @Test
    fun validImageUrl_showsInterceptedImage() {
        composeTestRule.captureMultiDevice("TrendingGameCard_Image") {
            TrendingGameCard(game = GAME_INFO, onClick = {})
        }
    }

    @Test
    fun nullImageUrl_showsPlaceholderOrNoImage() {
        val game = GAME_INFO.copy(
            name = "Kirby's Air Rider",
            releaseDate = "2025-11-20",
            imageUrl = null,
            platforms = listOf("Switch2"),
        )
        composeTestRule.captureMultiDevice("TrendingGameCard_NullImage") {
            TrendingGameCard(game = game, onClick = {})
        }
    }

    private companion object {
        val GAME_INFO = Game(
            id = 1,
            name = "League of Legends",
            imageUrl = "https://example.com/image.jpg",
            releaseDate = "2018-12-31",
            rating = 4.5,
            ratingsCount = 1523,
            metacritic = 80,
            isTba = false,
            addedCount = 5000,
            platforms = listOf("Switch", "PS5", "PC"),
        )
    }
}
