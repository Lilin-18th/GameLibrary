package com.lilin.gamelibrary.ui.component.discovery

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
class GameSectionsScreenShotTest {
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
    fun trendingGamesSection_ScreenShot() {
        composeTestRule.captureMultiDevice("TrendingGamesSection") {
            TrendingGamesSection(
                games = GAMES,
                onGameClick = {},
            )
        }
    }

    @Test
    fun highRatedGamesSection_ScreenShot() {
        composeTestRule.captureMultiDevice("HighRatedGamesSection") {
            HighRatedGamesSection(
                games = GAMES,
                onGameClick = {},
            )
        }
    }

    @Test
    fun newReleaseGamesSection_ScreenShot() {
        composeTestRule.captureMultiDevice("NewReleaseGamesSection") {
            NewReleaseGamesSection(
                games = GAMES,
                onGameClick = {},
            )
        }
    }

    private companion object {
        val GAMES = listOf(
            Game(
                id = 1,
                name = "The Witcher 3: Wild Hunt",
                imageUrl = "https://example.com/image.jpg",
                releaseDate = "2015-05-19",
                rating = 4.5,
                ratingsCount = 15234,
                metacritic = 92,
                isTba = false,
                addedCount = 50000,
                platforms = listOf("PC", "PS4", "Xbox One"),
            ),
            Game(
                id = 2,
                name = "Super Mario Party",
                imageUrl = null,
                releaseDate = "2018-10-05",
                rating = 3.8,
                ratingsCount = 244,
                metacritic = 76,
                isTba = false,
                addedCount = 30000,
                platforms = listOf("Switch"),
            ),
            Game(
                id = 3,
                name = "Cyberpunk 2077",
                imageUrl = null,
                releaseDate = "2020-12-10",
                rating = 4.2,
                ratingsCount = 8765,
                metacritic = 60,
                isTba = false,
                addedCount = 30000,
                platforms = listOf("PC", "PS5", "Xbox Series X"),
            ),
        )
    }
}
