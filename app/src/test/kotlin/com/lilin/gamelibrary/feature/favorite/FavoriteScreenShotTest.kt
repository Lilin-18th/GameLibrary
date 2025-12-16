package com.lilin.gamelibrary.feature.favorite

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.font.FontWeight
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
import com.lilin.gamelibrary.domain.model.SortOrder
import com.lilin.gamelibrary.navigation.TOP_LEVEL_ROUTES
import com.lilin.gamelibrary.ui.component.GameLibraryNavigationBar
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
class FavoriteScreenShotTest {
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
    fun favoriteScreen_Success_ScreenShot() {
        composeTestRule.captureMultiDevice("FavoriteScreen_Success") {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.favorite_screen_title),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                    )
                },
                bottomBar = {
                    GameLibraryNavigationBar(
                        topLevelRoute = TOP_LEVEL_ROUTES,
                        currentDestination = null,
                        onNavigateToRoute = {},
                    )
                },
                contentWindowInsets = WindowInsets.navigationBars,
                modifier = Modifier,
            ) { paddingValues ->
                FavoriteScreenSample(
                    uiState = FavoriteUiState.Success(
                        games = FAVORITE_GAMES,
                        sortOrder = SortOrder.NEWEST_FIRST,
                    ),
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun favoriteScreen_Empty_ScreenShot() {
        composeTestRule.captureMultiDevice("FavoriteScreen_Empty") {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.favorite_screen_title),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                    )
                },
                bottomBar = {
                    GameLibraryNavigationBar(
                        topLevelRoute = TOP_LEVEL_ROUTES,
                        currentDestination = null,
                        onNavigateToRoute = {},
                    )
                },
                contentWindowInsets = WindowInsets.navigationBars,
                modifier = Modifier,
            ) { paddingValues ->
                FavoriteScreenSample(
                    uiState = FavoriteUiState.Empty,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun favoriteScreen_SuccessWithOldestFirst_ScreenShot() {
        composeTestRule.captureMultiDevice("FavoriteScreen_SuccessWithOldestFirst") {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.favorite_screen_title),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                    )
                },
                bottomBar = {
                    GameLibraryNavigationBar(
                        topLevelRoute = TOP_LEVEL_ROUTES,
                        currentDestination = null,
                        onNavigateToRoute = {},
                    )
                },
                contentWindowInsets = WindowInsets.navigationBars,
                modifier = Modifier,
            ) { paddingValues ->
                FavoriteScreenSample(
                    uiState = FavoriteUiState.Success(
                        games = FAVORITE_GAMES,
                        sortOrder = SortOrder.OLDEST_FIRST,
                    ),
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }

    private companion object {
        val FAVORITE_GAMES = listOf(
            FavoriteGame(
                id = 1,
                name = "The Witcher 3: Wild Hunt",
                backgroundImage = "https://example.com/image.jpg",
                rating = 4.5,
                metacritic = 92,
                released = "2015-05-19",
                addedAt = 1703980800000L, // 2023-12-31
            ),
            FavoriteGame(
                id = 2,
                name = "Cyberpunk 2077",
                backgroundImage = "https://example.com/image.jpg",
                rating = 4.1,
                metacritic = 86,
                released = "2020-12-10",
                addedAt = 1703894400000L, // 2023-12-30
            ),
            FavoriteGame(
                id = 3,
                name = "Stardew Valley",
                backgroundImage = null,
                rating = 4.2,
                metacritic = 89,
                released = "2016-02-26",
                addedAt = 1703808000000L, // 2023-12-29
            ),
            FavoriteGame(
                id = 4,
                name = "Super Mario Party",
                backgroundImage = null,
                rating = 3.8,
                metacritic = 76,
                released = "2018-10-05",
                addedAt = 1703721600000L, // 2023-12-28
            ),
        )
    }
}
