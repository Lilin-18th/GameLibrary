package com.lilin.gamelibrary.feature.discovery

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
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
import com.lilin.gamelibrary.navigation.TOP_LEVEL_ROUTES
import com.lilin.gamelibrary.ui.component.DiscoveryTopBar
import com.lilin.gamelibrary.ui.component.GameLibraryNavigationBar
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
class DiscoveryScreenShotTest {
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
    fun discoveryScreen_Success_ScreenShot() {
        composeTestRule.captureMultiDevice("DiscoveryScreen_Success") {
            DiscoveryScreenSample(
                trendingState = DiscoveryUiState.Success(data = GAMES),
                highlyRatedState = DiscoveryUiState.Success(data = GAMES),
                newReleasesState = DiscoveryUiState.Success(data = GAMES),
                expandedUiState = DiscoveryExpandedUiState.Success(
                    games = GAMES,
                    selectedSection = SectionType.TRENDING,
                    totalCount = GAMES.size,
                ),
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
