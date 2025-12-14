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
    fun trendingGameCard_ValidImageUrl_ShowsInterceptedImage() {
        composeTestRule.captureMultiDevice("TrendingGameCard_Image") {
            TrendingGameCard(game = GAME_INFO, onClick = {})
        }
    }

    @Test
    fun trendingGameCard_NullImageUrl_ShowsPlaceholder() {
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

    @Test
    fun highRatedGameCard_ValidImageUrl_ShowsInterceptedImage() {
        composeTestRule.captureMultiDevice("TrendingGameCard_Image") {
            HighRatedGameCard(game = GAME_INFO, onClick = {})
        }
    }

    @Test
    fun highRatedGameCard_NullImageUrl_ShowsPlaceholder() {
        val game = GAME_INFO.copy(
            name = "Kirby's Air Rider",
            releaseDate = "2025-11-20",
            imageUrl = null,
            platforms = listOf("Switch2"),
        )
        composeTestRule.captureMultiDevice("TrendingGameCard_NullImage") {
            HighRatedGameCard(game = game, onClick = {})
        }
    }

    @Test
    fun highRatedGameCard_MetacriticScoreOver90_RatingGold() {
        val game = GAME_INFO.copy(metacritic = 90)
        composeTestRule.captureMultiDevice("HighRatedGameCard_Metacritic90") {
            HighRatedGameCard(game = game, onClick = {})
        }
    }

    @Test
    fun highRatedGameCard_MetacriticScoreOver70_RatingSilver() {
        val game = GAME_INFO.copy(metacritic = 70)
        composeTestRule.captureMultiDevice("HighRatedGameCard_Metacritic70") {
            HighRatedGameCard(game = game, onClick = {})
        }
    }

    @Test
    fun highRatedGameCard_MetacriticScoreOver50_RatingBronze() {
        val game = GAME_INFO.copy(metacritic = 50)
        composeTestRule.captureMultiDevice("HighRatedGameCard_Metacritic50") {
            HighRatedGameCard(game = game, onClick = {})
        }
    }

    @Test
    fun newReleaseGameCard_ValidImageUrl_ShowsInterceptedImage() {
        composeTestRule.captureMultiDevice("NewReleaseGameCard_Image") {
            NewReleaseGameCard(game = GAME_INFO, onClick = {})
        }
    }

    @Test
    fun newReleaseGameCard_NullImageUrl_ShowsPlaceholder() {
        val game = GAME_INFO.copy(
            name = "Kirby's Air Rider",
            releaseDate = "2025-11-20",
            imageUrl = null,
            platforms = listOf("Switch2"),
        )
        composeTestRule.captureMultiDevice("NewReleaseGameCard_NullImage") {
            NewReleaseGameCard(game = game, onClick = {})
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
