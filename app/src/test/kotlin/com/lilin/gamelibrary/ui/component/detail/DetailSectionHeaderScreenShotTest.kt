package com.lilin.gamelibrary.ui.component.detail

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.ui.theme.DetailDescriptionGradientEnd
import com.lilin.gamelibrary.ui.theme.DetailDescriptionGradientStart
import com.lilin.gamelibrary.ui.theme.DetailRatingGradientEnd
import com.lilin.gamelibrary.ui.theme.DetailRatingGradientStart
import com.lilin.gamelibrary.ui.theme.DetailTagGradientEnd
import com.lilin.gamelibrary.ui.theme.DetailTagGradientStart
import com.lilin.gamelibrary.util.captureMultiDevice
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class DetailSectionHeaderScreenShotTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun detailSectionHeader_Rating_screenShot() {
        composeTestRule.captureMultiDevice("DetailSectionHeader_Rating") {
            DetailSectionHeader(
                title = stringResource(R.string.detail_section_rating_title),
                icon = Icons.Filled.Star,
                gradientColors = Pair(DetailRatingGradientStart, DetailRatingGradientEnd),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    @Test
    fun detailSectionHeader_Description_screenShot() {
        composeTestRule.captureMultiDevice("DetailSectionHeader_Description") {
            DetailSectionHeader(
                title = stringResource(R.string.detail_section_description_title),
                icon = Icons.Filled.Star,
                gradientColors = Pair(DetailDescriptionGradientStart, DetailDescriptionGradientEnd),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    @Test
    fun detailSectionHeader_Tags_screenShot() {
        composeTestRule.captureMultiDevice("DetailSectionHeader_Tags") {
            DetailSectionHeader(
                title = stringResource(R.string.detail_section_tags_title),
                icon = Icons.Filled.Star,
                gradientColors = Pair(DetailTagGradientStart, DetailTagGradientEnd),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
