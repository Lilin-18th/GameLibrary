package com.lilin.gamelibrary.ui.component.discovery

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lilin.gamelibrary.util.captureMultiDevice
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class GameSectionHeaderScreenShotTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun gameSectionHeader_ScreenShot() {
        composeTestRule.captureMultiDevice("GameSectionHeader") {
            GameSectionHeader(
                title = "Popular",
            )
        }
    }
}
