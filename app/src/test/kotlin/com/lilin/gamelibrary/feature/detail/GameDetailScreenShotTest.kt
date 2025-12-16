package com.lilin.gamelibrary.feature.detail

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
import com.lilin.gamelibrary.domain.model.Developer
import com.lilin.gamelibrary.domain.model.GameDetail
import com.lilin.gamelibrary.domain.model.Genre
import com.lilin.gamelibrary.domain.model.Publisher
import com.lilin.gamelibrary.domain.model.Screenshot
import com.lilin.gamelibrary.domain.model.Tag
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
class GameDetailScreenShotTest {
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
    fun gameDetailScreen_success_screenShot() {
        composeTestRule.captureMultiDevice("GameDetailScreen_Success") {
            GameDetailScreenSample(
                uiState = GameDetailUiState.Success(
                    gameDetail = createMockGameDetail(
                        name = "The Witcher 3: Wild Hunt",
                        backgroundImage = "https://example.com/image.jpg",
                        metacritic = 95,
                        developers = listOf(Developer(1, "CD Projekt RED")),
                        releaseDate = "2015-05-19",
                        rating = 4.8,
                        ratingsCount = 25000,
                        platformNames = listOf("PC", "PlayStation 4", "Xbox One", "Nintendo Switch"),
                        genres = listOf(
                            Genre(1, "Action", "action"),
                            Genre(2, "Adventure", "adventure"),
                            Genre(3, "RPG", "rpg"),
                        ),
                        esrbRating = "M",
                        description = "The Witcher 3: Wild Hunt is a story-driven, next-generation open world role-playing game set in a visually stunning fantasy universe full of meaningful choices and impactful consequences. In The Witcher you play as the professional monster hunter, Geralt of Rivia, tasked with finding the Child of Prophecy in a vast open world rich with merchant cities, viking pirate islands, dangerous mountain passes, and forgotten caverns to explore.",
                        tags = listOf(
                            Tag(1, "Action", "action"),
                            Tag(2, "Adventure", "adventure"),
                            Tag(3, "RPG", "rpg"),
                            Tag(4, "Open World", "open-world"),
                            Tag(5, "Fantasy", "fantasy"),
                        ),
                    ),
                    isFavorite = false,
                ),
            )
        }
    }

    @Test
    fun gameDetailScreen_successWithFavorite_screenShot() {
        composeTestRule.captureMultiDevice("GameDetailScreen_SuccessWithFavorite") {
            GameDetailScreenSample(
                uiState = GameDetailUiState.Success(
                    gameDetail = createMockGameDetail(
                        name = "Cyberpunk 2077",
                        metacritic = 86,
                        developers = listOf(Developer(1, "CD Projekt RED")),
                        releaseDate = "2020-12-10",
                        rating = 4.1,
                        ratingsCount = 15000,
                        platformNames = listOf("PC", "PlayStation 5", "Xbox Series X/S"),
                        genres = listOf(
                            Genre(1, "Action", "action"),
                            Genre(2, "RPG", "rpg"),
                        ),
                        esrbRating = "M",
                        description = "Cyberpunk 2077 is an open-world, action-adventure story set in Night City, a megalopolis obsessed with power, glamour and body modification.",
                        tags = listOf(
                            Tag(1, "Action", "action"),
                            Tag(2, "RPG", "rpg"),
                            Tag(3, "Sci-fi", "sci-fi"),
                            Tag(4, "Open World", "open-world"),
                        ),
                    ),
                    isFavorite = true,
                ),
            )
        }
    }

    private fun createMockGameDetail(
        name: String = "Test Game",
        backgroundImage: String? = null,
        metacritic: Int = 75,
        developers: List<Developer> = listOf(Developer(1, "Test Developer")),
        releaseDate: String? = "2024-01-01",
        rating: Double = 4.0,
        ratingsCount: Int = 1000,
        platformNames: List<String> = listOf("PC"),
        genres: List<Genre> = listOf(Genre(1, "Action", "action")),
        esrbRating: String? = "E",
        description: String? = "Test description",
        tags: List<Tag> = listOf(Tag(1, "Test", "test")),
    ): GameDetail {
        return GameDetail(
            id = 1,
            name = name,
            backgroundImage = backgroundImage,
            releaseDate = releaseDate,
            metacritic = metacritic,
            rating = rating,
            ratingsCount = ratingsCount,
            platformNames = platformNames,
            genres = genres,
            description = description,
            shortScreenshots = listOf(Screenshot(1, "https://example.com/screenshot.jpg")),
            developers = developers,
            publishers = listOf(Publisher(1, "Test Publisher")),
            esrbRating = esrbRating,
            playtime = 30,
            tags = tags,
        )
    }
}
