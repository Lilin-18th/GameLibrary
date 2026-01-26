package com.lilin.gamelibrary.ui.component.favorite

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
import com.lilin.gamelibrary.domain.model.FavoriteGame
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
class FavoriteGameCardScreenShotTest {
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
    fun favoriteGameCard_withImage_screenShot() {
        composeTestRule.captureMultiDevice("FavoriteGameCard_WithImage") {
            FavoriteGameCompactCard(
                game = createMockFavoriteGame(
                    name = "The Legend of Zelda: Breath of the Wild",
                    backgroundImage = "https://example.com/image.jpg",
                    rating = 4.5,
                    metacritic = 97,
                    released = "2017-03-03",
                ),
                onClick = {},
                onClickDelete = {},
            )
        }
    }

    @Test
    fun favoriteGameCard_withoutImage_screenShot() {
        composeTestRule.captureMultiDevice("FavoriteGameCard_WithoutImage") {
            FavoriteGameCompactCard(
                game = createMockFavoriteGame(
                    name = "Stardew Valley",
                    backgroundImage = null,
                    rating = 4.2,
                    metacritic = 89,
                    released = "2016-02-26",
                ),
                onClick = {},
                onClickDelete = {},
            )
        }
    }

    @Test
    fun favoriteGameCard_longGameName_screenShot() {
        composeTestRule.captureMultiDevice("FavoriteGameCard_LongGameName") {
            FavoriteGameCompactCard(
                game = createMockFavoriteGame(
                    name = "The Elder Scrolls V: Skyrim Special Edition Anniversary Edition",
                    backgroundImage = "https://example.com/image.jpg",
                    rating = 4.7,
                    metacritic = 84,
                    released = "2011-11-11",
                ),
                onClick = {},
                onClickDelete = {},
            )
        }
    }

    private fun createMockFavoriteGame(
        id: Int = 1,
        name: String = "Test Game",
        backgroundImage: String? = null,
        rating: Double = 4.0,
        metacritic: Int? = 75,
        released: String? = "2024-01-01",
    ): FavoriteGame {
        return FavoriteGame(
            id = id,
            name = name,
            backgroundImage = backgroundImage,
            rating = rating,
            metacritic = metacritic,
            released = released,
            addedAt = System.currentTimeMillis(),
        )
    }
}
