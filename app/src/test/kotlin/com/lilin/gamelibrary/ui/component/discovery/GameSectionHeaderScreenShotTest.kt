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
                isSuccessState = true,
                sectionType = SectionType.TRENDING,
                onReload = {},
            )
        }
    }

    @Test
    fun gameSectionHeader_HighRated() {
        composeTestRule.captureMultiDevice("GameSectionHeader_HighRate") {
            GameSectionHeader(
                isSuccessState = true,
                sectionType = SectionType.HIGH_RATED,
                onReload = {},
            )
        }
    }

    @Test
    fun gameSectionHeader_NewRelease() {
        composeTestRule.captureMultiDevice("GameSectionHeader_NewRelease") {
            GameSectionHeader(
                isSuccessState = true,
                sectionType = SectionType.NEW_RELEASE,
                onReload = {},
            )
        }
    }

    @Test
    fun gameSectionHeader_Trending_isSuccessState_false() {
        composeTestRule.captureMultiDevice("GameSectionHeader_Trend_false") {
            GameSectionHeader(
                isSuccessState = false,
                sectionType = SectionType.TRENDING,
                onReload = {},
            )
        }
    }

    @Test
    fun gameSectionHeader_HighRated_isSuccessState_false() {
        composeTestRule.captureMultiDevice("GameSectionHeader_HighRate_false") {
            GameSectionHeader(
                isSuccessState = false,
                sectionType = SectionType.HIGH_RATED,
                onReload = {},
            )
        }
    }

    @Test
    fun gameSectionHeader_NewRelease_isSuccessState_false() {
        composeTestRule.captureMultiDevice("GameSectionHeader_NewRelease_false") {
            GameSectionHeader(
                isSuccessState = false,
                sectionType = SectionType.NEW_RELEASE,
                onReload = {},
            )
        }
    }
}
