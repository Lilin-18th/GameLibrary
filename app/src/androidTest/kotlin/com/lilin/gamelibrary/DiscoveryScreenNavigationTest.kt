@file:OptIn(ExperimentalTestApi::class)

package com.lilin.gamelibrary

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressKey
import com.lilin.gamelibrary.ui.MainActivity
import com.lilin.gamelibrary.util.scrollDownUntilNodeExists
import com.lilin.gamelibrary.util.scrollRightUntilNodeExists
import com.lilin.gamelibrary.util.scrollUpUntilNodeExists
import com.lilin.gamelibrary.util.waitUntilNodeExists
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DiscoveryScreenNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun navigateToNewReleaseGamesFromDiscoveryScreen() {
        composeTestRule
            .waitUntilNodeExists("DiscoveryTopBar")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("DiscoveryLazyColumn")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("TrendingGames")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("HighRatedGames")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("NewReleaseGames")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("NewReleaseGamesSectionLazyRow")
            .performScrollTo()

        // LazyRowを右方向にスクロールしてNewReleaseSeeMoreCardを探してクリック（詳細画面へ遷移）
        composeTestRule
            .scrollRightUntilNodeExists(
                scrollableTag = "NewReleaseGamesSectionLazyRow",
                targetTag = "NewReleaseSeeMoreCard",
            )
            .performClick()

        composeTestRule
            .waitUntilNodeExists("SectionDetailBottomBar")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("SectionDetailLazyVerticalGrid")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("ListMode")
            .performClick()

        composeTestRule
            .waitUntilNodeExists("SectionDetailLazyColumn")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("GridMode")
            .performClick()

        composeTestRule
            .waitUntilNodeExists("SectionDetailLazyVerticalGrid")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("BackKey")
            .performClick()

        composeTestRule
            .waitUntilNodeExists("Search")
            .performClick()

        composeTestRule
            .waitUntilNodeExists("EmptyView")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("SearchBottomBar")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("SearchTextField")
            .apply {
                performTextInput("a")
                performKeyInput {
                    pressKey(Key.Enter)
                }
            }

        composeTestRule
            .waitUntilNodeExists("SearchResultsCompactList")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("Favorite")
            .performClick()

        composeTestRule
            .waitUntilNodeExists("FavoriteEmptyView")
            .performClick()

        composeTestRule
            .waitUntilNodeExists("Home")
            .performClick()

        composeTestRule
            .waitUntilNodeExists("DiscoveryTopBar")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("DiscoveryLazyColumn")
            .assertIsDisplayed()

        composeTestRule
            .scrollUpUntilNodeExists(
                scrollableTag = "DiscoveryLazyColumn",
                targetTag = "TrendingGamesSectionLazyRow",
            )
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("TrendingGameCard")

        composeTestRule
            .onAllNodesWithTag("TrendingGameCard")[0]
            .performClick()

        composeTestRule
            .waitUntilNodeExists("GameDetailTopBar")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("GameDetailBottomBar")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("GameDetailSuccessContent")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("GameBackgroundImageSection")
            .assertIsDisplayed()

        composeTestRule
            .scrollDownUntilNodeExists(
                scrollableTag = "GameDetailSuccessContent",
                targetTag = "GameBasicInfoSection",
            )
            .assertIsDisplayed()

        composeTestRule
            .scrollDownUntilNodeExists(
                scrollableTag = "GameDetailSuccessContent",
                targetTag = "GameRatingSummarySection",
            )
            .assertIsDisplayed()

        composeTestRule
            .scrollDownUntilNodeExists(
                scrollableTag = "GameDetailSuccessContent",
                targetTag = "GameDescriptionSection",
            )
            .assertIsDisplayed()

        composeTestRule
            .scrollDownUntilNodeExists(
                scrollableTag = "GameDetailSuccessContent",
                targetTag = "GameTagsSection",
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("FavoriteButton")
            .performClick()

        composeTestRule
            .onNodeWithTag("BackKey")
            .performClick()

        composeTestRule
            .waitUntilNodeExists("Favorite")
            .performClick()

        composeTestRule
            .waitUntilNodeExists("FavoriteGameCompactCard")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("DeleteButton")
            .performClick()

        composeTestRule
            .waitUntilNodeExists("DeleteDialog")
            .assertIsDisplayed()

        composeTestRule
            .waitUntilNodeExists("ConfirmDeleteButton")
            .performClick()

        composeTestRule
            .waitUntilNodeExists("FavoriteEmptyView")
            .performClick()
    }
}
