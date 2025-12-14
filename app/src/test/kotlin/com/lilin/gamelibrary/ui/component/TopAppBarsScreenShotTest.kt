package com.lilin.gamelibrary.ui.component

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
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
@OptIn(ExperimentalMaterial3Api::class)
class TopAppBarsScreenShotTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun discoveryTopBar_ScreenShot() {
        composeTestRule.captureMultiDevice("DiscoveryTopBar") {
            DiscoveryTopBar(
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            )
        }
    }
}
