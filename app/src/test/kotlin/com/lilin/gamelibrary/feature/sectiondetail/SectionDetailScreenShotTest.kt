package com.lilin.gamelibrary.feature.sectiondetail

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.lilin.gamelibrary.ui.component.discovery.SectionType
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
class SectionDetailScreenShotTest {
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun sectionDetailScreen_Trending_Success_ScreenShot() {
        composeTestRule.captureMultiDevice("SectionDetailScreen_Trending_Success") {
            SectionDetailScreenSample(
                uiState = SectionDetailUiState.Success(
                    data = SEARCH_RESULTS,
                    totalCount = 2,
                ),
                sectionType = SectionType.TRENDING,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun sectionDetailScreen_High_Rated_Success_ScreenShot() {
        composeTestRule.captureMultiDevice("SectionDetailScreen_High_Rated_Success") {
            SectionDetailScreenSample(
                uiState = SectionDetailUiState.Success(
                    data = SEARCH_RESULTS,
                    totalCount = 2,
                ),
                sectionType = SectionType.HIGH_RATED,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun sectionDetailScreen_New_Release_Success_ScreenShot() {
        composeTestRule.captureMultiDevice("SectionDetailScreen_New_Release_Success") {
            SectionDetailScreenSample(
                uiState = SectionDetailUiState.Success(
                    data = SEARCH_RESULTS,
                    totalCount = 2,
                ),
                sectionType = SectionType.NEW_RELEASE,
            )
        }
    }

    private companion object {
        val SEARCH_RESULTS = listOf(
            Game(
                id = 1,
                name = "Cyberpunk 2077",
                imageUrl = "https://example.com/image.jpg",
                releaseDate = "2020-12-10",
                rating = 4.1,
                ratingsCount = 8765,
                metacritic = 86,
                isTba = false,
                addedCount = 30000,
                platforms = listOf("PC", "PlayStation 5", "Xbox Series X/S"),
            ),
            Game(
                id = 2,
                name = "Deus Ex: Human Revolution",
                imageUrl = null,
                releaseDate = "2011-08-23",
                rating = 4.3,
                ratingsCount = 5432,
                metacritic = 90,
                isTba = false,
                addedCount = 25000,
                platforms = listOf("PC", "PlayStation 3", "Xbox 360"),
            ),
            Game(
                id = 3,
                name = "Shadowrun: Dragonfall",
                imageUrl = "https://example.com/image.jpg",
                releaseDate = "2014-02-27",
                rating = 4.0,
                ratingsCount = 1234,
                metacritic = 85,
                isTba = false,
                addedCount = 15000,
                platforms = listOf("PC", "Mobile"),
            ),
        )

        val WITCHER_RESULTS = listOf(
            Game(
                id = 4,
                name = "The Witcher 3: Wild Hunt",
                imageUrl = "https://example.com/image.jpg",
                releaseDate = "2015-05-19",
                rating = 4.5,
                ratingsCount = 15234,
                metacritic = 92,
                isTba = false,
                addedCount = 50000,
                platforms = listOf("PC", "PlayStation 4", "Xbox One", "Nintendo Switch"),
            ),
            Game(
                id = 5,
                name = "The Witcher 2: Assassins of Kings",
                imageUrl = null,
                releaseDate = "2011-05-17",
                rating = 4.2,
                ratingsCount = 8765,
                metacritic = 88,
                isTba = false,
                addedCount = 35000,
                platforms = listOf("PC", "Xbox 360"),
            ),
            Game(
                id = 6,
                name = "The Witcher",
                imageUrl = "https://example.com/image.jpg",
                releaseDate = "2007-10-26",
                rating = 3.9,
                ratingsCount = 5432,
                metacritic = 81,
                isTba = false,
                addedCount = 20000,
                platforms = listOf("PC"),
            ),
        )
    }
}
