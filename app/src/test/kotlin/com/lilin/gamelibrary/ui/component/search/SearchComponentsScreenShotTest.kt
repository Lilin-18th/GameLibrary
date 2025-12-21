package com.lilin.gamelibrary.ui.component.search

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
class SearchComponentsScreenShotTest {
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
    fun searchField_empty_screenShot() {
        composeTestRule.captureMultiDevice("SearchField_Empty") {
            SearchBottomBar(
                query = "",
                onQueryChange = {},
                onSearch = {},
            )
        }
    }

    @Test
    fun searchField_withQuery_screenShot() {
        composeTestRule.captureMultiDevice("SearchField_WithQuery") {
            SearchBottomBar(
                query = "The Witcher",
                onQueryChange = {},
                onSearch = {},
            )
        }
    }

    @Test
    fun searchResultCard_withImage_screenShot() {
        composeTestRule.captureMultiDevice("SearchResultCard_WithImage") {
            SearchResultCard(
                game = createMockGame(
                    name = "The Witcher 3: Wild Hunt",
                    imageUrl = "https://example.com/image.jpg",
                    rating = 4.5,
                    metacritic = 95,
                    releaseDate = "2015-05-19",
                    platforms = listOf("PC", "PlayStation 4", "Xbox One"),
                ),
                onClickItem = {},
            )
        }
    }

    @Test
    fun searchResultCard_withoutImage_screenShot() {
        composeTestRule.captureMultiDevice("SearchResultCard_WithoutImage") {
            SearchResultCard(
                game = createMockGame(
                    name = "Stardew Valley",
                    imageUrl = null,
                    rating = 4.2,
                    metacritic = 89,
                    releaseDate = "2016-02-26",
                    platforms = listOf("PC", "Nintendo Switch", "Mobile"),
                ),
                onClickItem = {},
            )
        }
    }

    @Test
    fun searchResultCard_longGameName_screenShot() {
        composeTestRule.captureMultiDevice("SearchResultCard_LongGameName") {
            SearchResultCard(
                game = createMockGame(
                    name = "The Elder Scrolls V: Skyrim Special Edition Anniversary Edition",
                    imageUrl = "https://example.com/image.jpg",
                    rating = 4.7,
                    metacritic = 84,
                    releaseDate = "2011-11-11",
                    platforms = listOf("PC", "PlayStation", "Xbox", "Nintendo Switch"),
                ),
                onClickItem = {},
            )
        }
    }

    @Test
    fun searchComponents_combined_screenShot() {
        composeTestRule.captureMultiDevice("SearchComponents_Combined") {
            Column {
                SearchBottomBar(
                    query = "cyberpunk",
                    onQueryChange = {},
                    onSearch = {},
                )

                SearchResultCard(
                    game = createMockGame(
                        name = "Cyberpunk 2077",
                        imageUrl = "https://example.com/image.jpg",
                        rating = 4.1,
                        metacritic = 86,
                        releaseDate = "2020-12-10",
                        platforms = listOf("PC", "PlayStation", "Xbox"),
                    ),
                    onClickItem = {},
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )

                SearchResultCard(
                    game = createMockGame(
                        id = 2,
                        name = "Deus Ex: Human Revolution",
                        imageUrl = null,
                        rating = 4.3,
                        metacritic = 90,
                        releaseDate = "2011-08-23",
                        platforms = listOf("PC", "PlayStation 3", "Xbox 360"),
                    ),
                    onClickItem = {},
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )

                SearchResultCard(
                    game = createMockGame(
                        id = 3,
                        name = "Shadowrun: Dragonfall",
                        imageUrl = "https://example.com/image.jpg",
                        rating = 4.0,
                        metacritic = 85,
                        releaseDate = "2014-02-27",
                        platforms = listOf("PC", "Mobile"),
                    ),
                    onClickItem = {},
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        }
    }

    private fun createMockGame(
        id: Int = 1,
        name: String = "Test Game",
        imageUrl: String? = null,
        rating: Double = 4.0,
        metacritic: Int = 75,
        releaseDate: String? = "2024-01-01",
        platforms: List<String>? = listOf("PC"),
    ): Game {
        return Game(
            id = id,
            name = name,
            imageUrl = imageUrl,
            releaseDate = releaseDate,
            rating = rating,
            ratingsCount = 1000,
            metacritic = metacritic,
            isTba = false,
            addedCount = 5000,
            platforms = platforms,
        )
    }
}
