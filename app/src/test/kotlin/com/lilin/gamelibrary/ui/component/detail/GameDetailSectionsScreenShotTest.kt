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
class GameDetailSectionsScreenShotTest {
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
    fun gameBackgroundImageSection_withImage_screenShot() {
        composeTestRule.captureMultiDevice("GameBackgroundImageSection_WithImage") {
            GameBackgroundImageSection(
                gameDetail = createMockGameDetail(
                    backgroundImage = "https://example.com/image.jpg",
                    metacriticScore = 92,
                ),
            )
        }
    }

    @Test
    fun gameBackgroundImageSection_withoutImage_screenShot() {
        composeTestRule.captureMultiDevice("GameBackgroundImageSection_WithoutImage") {
            GameBackgroundImageSection(
                gameDetail = createMockGameDetail(
                    backgroundImage = null,
                    metacriticScore = 60,
                ),
            )
        }
    }

    @Test
    fun gameBasicInfoSection_complete_screenShot() {
        composeTestRule.captureMultiDevice("GameBasicInfoSection_Complete") {
            GameBasicInfoSection(
                gameDetail = createMockGameDetail(
                    name = "The Last of Us Part II",
                    developers = listOf(
                        Developer(1, "Naughty Dog"),
                        Developer(2, "Sony Interactive Entertainment"),
                    ),
                    releaseDate = "2020-06-19",
                ),
            )
        }
    }

    @Test
    fun gameBasicInfoSection_minimal_screenShot() {
        composeTestRule.captureMultiDevice("GameBasicInfoSection_Minimal") {
            GameBasicInfoSection(
                gameDetail = createMockGameDetail(
                    name = "Unknown Game",
                    developers = emptyList(),
                    releaseDate = null,
                ),
            )
        }
    }

    @Test
    fun gameRatingSummarySection_complete_screenShot() {
        composeTestRule.captureMultiDevice("GameRatingSummarySection_Complete") {
            GameRatingSummarySection(
                gameDetail = createMockGameDetail(
                    rating = 4.5,
                    ratingsCount = 15234,
                    platformNames = listOf("PC", "PlayStation 4", "Xbox One"),
                    genres = listOf(
                        Genre(1, "Action", "action"),
                        Genre(2, "Adventure", "adventure"),
                        Genre(3, "RPG", "rpg"),
                    ),
                    releaseDate = "2020-06-19",
                    esrbRating = "M",
                ),
            )
        }
    }

    @Test
    fun gameRatingSummarySection_minimal_screenShot() {
        composeTestRule.captureMultiDevice("GameRatingSummarySection_Minimal") {
            GameRatingSummarySection(
                gameDetail = createMockGameDetail(
                    rating = 3.2,
                    ratingsCount = 150,
                    platformNames = listOf("Nintendo Switch"),
                    genres = listOf(Genre(1, "Puzzle", "puzzle")),
                    releaseDate = null,
                    esrbRating = null,
                ),
            )
        }
    }

    @Test
    fun gameDescriptionSection_withDescription_screenShot() {
        composeTestRule.captureMultiDevice("GameDescriptionSection_WithDescription") {
            GameDescriptionSection(
                gameDetail = createMockGameDetail(
                    description = "Five years after their dangerous journey across the post-pandemic United States, Ellie and Joel have settled down in Jackson, Wyoming. Living amongst a thriving community of survivors has allowed them to live in relative safety and stability, despite the constant threat of the infected and other, more desperate survivors. When a violent event disrupts that peace, Ellie embarks on a relentless journey to carry out justice and find closure. As she hunts those responsible one by one, she is confronted with the devastating physical and emotional repercussions of her actions.",
                ),
            )
        }
    }

    @Test
    fun gameDescriptionSection_shortDescription_screenShot() {
        composeTestRule.captureMultiDevice("GameDescriptionSection_ShortDescription") {
            GameDescriptionSection(
                gameDetail = createMockGameDetail(
                    description = "A short game description that fits within a few lines.",
                ),
            )
        }
    }

    @Test
    fun gameTagsSection_multipleTags_screenShot() {
        composeTestRule.captureMultiDevice("GameTagsSection_MultipleTags") {
            GameTagsSection(
                gameDetail = createMockGameDetail(
                    tags = listOf(
                        Tag(1, "Action", "action"),
                        Tag(2, "Adventure", "adventure"),
                        Tag(3, "RPG", "rpg"),
                        Tag(4, "Open World", "open-world"),
                        Tag(5, "Fantasy", "fantasy"),
                        Tag(6, "Singleplayer", "singleplayer"),
                        Tag(7, "Story Rich", "story-rich"),
                        Tag(8, "Third Person", "third-person"),
                    ),
                ),
            )
        }
    }

    @Test
    fun gameTagsSection_fewTags_screenShot() {
        composeTestRule.captureMultiDevice("GameTagsSection_FewTags") {
            GameTagsSection(
                gameDetail = createMockGameDetail(
                    tags = listOf(
                        Tag(1, "Puzzle", "puzzle"),
                        Tag(2, "Indie", "indie"),
                    ),
                ),
            )
        }
    }

    @Test
    fun gameDetailSections_combined_screenShot() {
        composeTestRule.captureMultiDevice("GameDetailSections_Combined") {
            val gameDetail = createMockGameDetail(
                name = "Cyberpunk 2077",
                backgroundImage = "https://example.com/image.jpg",
                metacriticScore = 86,
                developers = listOf(Developer(1, "CD Projekt RED")),
                releaseDate = "2020-12-10",
                rating = 4.1,
                ratingsCount = 8765,
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
                    Tag(5, "Cyberpunk", "cyberpunk"),
                ),
            )

            Column {
                GameBackgroundImageSection(gameDetail = gameDetail)
                GameBasicInfoSection(gameDetail = gameDetail)
                GameRatingSummarySection(
                    gameDetail = gameDetail,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                GameDescriptionSection(gameDetail = gameDetail)
                GameTagsSection(gameDetail = gameDetail)
            }
        }
    }

    private fun createMockGameDetail(
        name: String = "Test Game",
        backgroundImage: String? = null,
        metacriticScore: Int = 75,
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
            metacritic = metacriticScore,
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
