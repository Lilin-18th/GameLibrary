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
    fun gameSectionHeader_Trending() {
        composeTestRule.captureMultiDevice("GameSectionHeader_Trend") {
            GameSectionHeader(
                sectionType = SectionType.TRENDING
            )
        }
    }

    @Test
    fun gameSectionHeader_HighRated() {
        composeTestRule.captureMultiDevice("GameSectionHeader_HighRate") {
            GameSectionHeader(
                sectionType = SectionType.HIGH_RATED
            )
        }
    }

    @Test
    fun gameSectionHeader_NewRelease() {
        composeTestRule.captureMultiDevice("GameSectionHeader_NewRelease") {
            GameSectionHeader(
                sectionType = SectionType.NEW_RELEASE
            )
        }
    }
}
