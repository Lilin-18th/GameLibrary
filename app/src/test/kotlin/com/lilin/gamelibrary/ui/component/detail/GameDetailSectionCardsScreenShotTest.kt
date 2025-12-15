package com.lilin.gamelibrary.ui.component.detail

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ColorImage
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.asImage
import coil3.test.FakeImageLoaderEngine
import com.lilin.gamelibrary.R
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
class GameDetailSectionCardsScreenShotTest {
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
    fun gameBackgroundImage_withImage_screenShot() {
        composeTestRule.captureMultiDevice("GameBackgroundImage_WithImage") {
            GameBackgroundImage(
                imageUrl = "https://example.com/image.jpg",
                contentDescription = "The Witcher 3: Wild Hunt",
                metacriticScore = 92,
            )
        }
    }

    @Test
    fun gameBackgroundImage_withoutImage_screenShot() {
        composeTestRule.captureMultiDevice("GameBackgroundImage_WithoutImage") {
            GameBackgroundImage(
                imageUrl = null,
                contentDescription = "The Witcher 3: Wild Hunt",
                metacriticScore = 60,
            )
        }
    }

    @Test
    fun gameBasicInfo_complete_screenShot() {
        composeTestRule.captureMultiDevice("GameBasicInfo_Complete") {
            GameBasicInfo(
                gameName = "The Last of Us Part II",
                developers = listOf("Naughty Dog", "Sony Interactive Entertainment"),
                releaseYear = "2020",
            )
        }
    }

    @Test
    fun gameBasicInfo_noDeveloper_screenShot() {
        composeTestRule.captureMultiDevice("GameBasicInfo_NoDeveloper") {
            GameBasicInfo(
                gameName = "Unknown Game",
                developers = emptyList(),
                releaseYear = "2024",
            )
        }
    }

    @Test
    fun gameInfoCard_complete_screenShot() {
        composeTestRule.captureMultiDevice("GameInfoCard_Complete") {
            GameInfoCard(
                rating = 4.5,
                ratingsCount = 15234,
                platforms = listOf("PC", "PlayStation 4", "Xbox One"),
                genres = listOf("Action", "Adventure", "RPG"),
                releaseDate = "2020-06-19",
                esrbRating = "M",
            )
        }
    }

    @Test
    fun gameInfoCard_minimal_screenShot() {
        composeTestRule.captureMultiDevice("GameInfoCard_Minimal") {
            GameInfoCard(
                rating = 3.2,
                ratingsCount = 150,
                platforms = listOf("Nintendo Switch"),
                genres = listOf("Puzzle"),
                releaseDate = null,
                esrbRating = null,
            )
        }
    }

    @Test
    fun gameDescription_collapsed_screenShot() {
        composeTestRule.captureMultiDevice("GameDescription_Collapsed") {
            GameDescription(
                description = "Five years after their dangerous journey across the post-pandemic United States, Ellie and Joel have settled down in Jackson, Wyoming. Living amongst a thriving community of survivors has allowed them to live in relative safety and stability, despite the constant threat of the infected and other, more desperate survivors. When a violent event disrupts that peace, Ellie embarks on a relentless journey to carry out justice and find closure. As she hunts those responsible one by one, she is confronted with the devastating physical and emotional repercussions of her actions.",
            )
        }
    }

    @Test
    fun gameDescription_short_screenShot() {
        composeTestRule.captureMultiDevice("GameDescription_Short") {
            GameDescription(
                description = "A short game description that fits within a few lines.",
            )
        }
    }

    @Test
    fun gameTags_multiple_screenShot() {
        composeTestRule.captureMultiDevice("GameTags_Multiple") {
            GameTags(
                tags = listOf(
                    "Action",
                    "Adventure",
                    "RPG",
                    "Open World",
                    "Fantasy",
                    "Singleplayer",
                    "Story Rich",
                    "Third Person",
                ),
            )
        }
    }

    @Test
    fun gameTags_few_screenShot() {
        composeTestRule.captureMultiDevice("GameTags_Few") {
            GameTags(
                tags = listOf("Puzzle", "Indie"),
            )
        }
    }

    @Test
    fun gameDetailComponents_combined_screenShot() {
        composeTestRule.captureMultiDevice("GameDetailComponents_Combined") {
            Column {
                GameBackgroundImage(
                    imageUrl = "https://example.com/image.jpg",
                    contentDescription = "Cyberpunk 2077",
                    metacriticScore = 86,
                )

                GameBasicInfo(
                    gameName = "Cyberpunk 2077",
                    developers = listOf("CD Projekt RED"),
                    releaseYear = "2020",
                )

                GameInfoCard(
                    rating = 4.1,
                    ratingsCount = 8765,
                    platforms = listOf("PC", "PlayStation 5", "Xbox Series X/S"),
                    genres = listOf("Action", "RPG"),
                    releaseDate = "2020-12-10",
                    esrbRating = "M",
                    modifier = Modifier.padding(vertical = 8.dp),
                )

                GameDescription(
                    description = "Cyberpunk 2077 is an open-world, action-adventure story set in Night City, a megalopolis obsessed with power, glamour and body modification.",
                )

                GameTags(
                    tags = listOf("Action", "RPG", "Sci-fi", "Open World", "Cyberpunk"),
                )
            }
        }
    }
}
