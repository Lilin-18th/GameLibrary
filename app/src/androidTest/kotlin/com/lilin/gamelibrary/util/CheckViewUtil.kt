package com.lilin.gamelibrary.util

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeUp

/**
 * 指定したtestTagのNodeが表示されるまで5秒まつ
 *
 * @param testTag test対象のタグ
 * @param timeoutMillis 表示されるまでの待機時間
 * @return 指定したテストタグを持つComposeNodeに対するSemanticsNodeInteractionオブジェクト
 */
fun ComposeContentTestRule.waitUntilNodeExists(
    testTag: String,
    timeoutMillis: Long = 5_000L,
): SemanticsNodeInteraction {
    waitUntil(timeoutMillis) {
        onAllNodesWithTag(testTag).fetchSemanticsNodes().isNotEmpty()
    }

    return onNodeWithTag(testTag)
}

/**
 * スクロール可能なコンテナ内で、指定したtestTagのNodeが見つかるまで横スクロール（右方向）を繰り返す
 *
 * @param scrollableTag スクロール可能なコンテナのtestTag（例: LazyRow）
 * @param targetTag 探したい要素のtestTag
 * @param maxScrollAttempts 最大スクロール試行回数（デフォルト: 10回）
 * @return 指定したテストタグを持つComposeNodeに対するSemanticsNodeInteractionオブジェクト
 * @throws AssertionError 指定回数スクロールしても要素が見つからなかった場合
 */
fun ComposeContentTestRule.scrollRightUntilNodeExists(
    scrollableTag: String,
    targetTag: String,
    maxScrollAttempts: Int = 10,
): SemanticsNodeInteraction {
    var attempts = 0

    while (attempts < maxScrollAttempts) {
        val nodes = onAllNodesWithTag(targetTag).fetchSemanticsNodes()
        if (nodes.isNotEmpty()) {
            return onNodeWithTag(targetTag)
        }

        onNodeWithTag(scrollableTag).performTouchInput {
            swipeLeft()
        }

        attempts++
        waitForIdle()
    }

    throw AssertionError(
        "Tag '$targetTag' not found in '$scrollableTag' after $maxScrollAttempts attempts",
    )
}

/**
 * スクロール可能なコンテナ内で、指定したtestTagのNodeが見つかるまで横スクロール（左方向）を繰り返す
 *
 * @param scrollableTag スクロール可能なコンテナのtestTag（例: LazyRow）
 * @param targetTag 探したい要素のtestTag
 * @param maxScrollAttempts 最大スクロール試行回数（デフォルト: 10回）
 * @return 指定したテストタグを持つComposeNodeに対するSemanticsNodeInteractionオブジェクト
 * @throws AssertionError 指定回数スクロールしても要素が見つからなかった場合
 */
@Suppress("Unused")
fun ComposeContentTestRule.scrollLeftUntilNodeExists(
    scrollableTag: String,
    targetTag: String,
    maxScrollAttempts: Int = 10,
): SemanticsNodeInteraction {
    var attempts = 0

    while (attempts < maxScrollAttempts) {
        val nodes = onAllNodesWithTag(targetTag).fetchSemanticsNodes()
        if (nodes.isNotEmpty()) {
            return onNodeWithTag(targetTag)
        }

        onNodeWithTag(scrollableTag).performTouchInput {
            swipeRight()
        }

        attempts++
        waitForIdle()
    }

    throw AssertionError(
        "Tag '$targetTag' not found in '$scrollableTag' (left) after $maxScrollAttempts tries",
    )
}

/**
 * スクロール可能なコンテナ内で、指定したtestTagのNodeが見つかるまで縦スクロール（上方向）を繰り返す
 *
 * @param scrollableTag スクロール可能なコンテナのtestTag（例: LazyColumn）
 * @param targetTag 探したい要素のtestTag
 * @param maxScrollAttempts 最大スクロール試行回数（デフォルト: 10回）
 * @return 指定したテストタグを持つComposeNodeに対するSemanticsNodeInteractionオブジェクト
 * @throws AssertionError 指定回数スクロールしても要素が見つからなかった場合
 */
fun ComposeContentTestRule.scrollUpUntilNodeExists(
    scrollableTag: String,
    targetTag: String,
    maxScrollAttempts: Int = 10,
): SemanticsNodeInteraction {
    var attempts = 0

    while (attempts < maxScrollAttempts) {
        val nodes = onAllNodesWithTag(targetTag).fetchSemanticsNodes()
        if (nodes.isNotEmpty()) {
            return onNodeWithTag(targetTag)
        }

        onNodeWithTag(scrollableTag).performTouchInput {
            swipeDown()
        }

        attempts++
        waitForIdle()
    }

    throw AssertionError(
        "Tag '$targetTag' not found in '$scrollableTag' (up) after $maxScrollAttempts tries",
    )
}

/**
 * スクロール可能なコンテナ内で、指定したtestTagのNodeが見つかるまで縦スクロール（下方向）を繰り返す
 *
 * @param scrollableTag スクロール可能なコンテナのtestTag（例: LazyColumn）
 * @param targetTag 探したい要素のtestTag
 * @param maxScrollAttempts 最大スクロール試行回数（デフォルト: 10回）
 * @return 指定したテストタグを持つComposeNodeに対するSemanticsNodeInteractionオブジェクト
 * @throws AssertionError 指定回数スクロールしても要素が見つからなかった場合
 */
fun ComposeContentTestRule.scrollDownUntilNodeExists(
    scrollableTag: String,
    targetTag: String,
    maxScrollAttempts: Int = 10,
): SemanticsNodeInteraction {
    var attempts = 0

    while (attempts < maxScrollAttempts) {
        val nodes = onAllNodesWithTag(targetTag).fetchSemanticsNodes()
        if (nodes.isNotEmpty()) {
            return onNodeWithTag(targetTag)
        }

        onNodeWithTag(scrollableTag).performTouchInput {
            swipeUp()
        }

        attempts++
        waitForIdle()
    }

    throw AssertionError(
        "Tag '$targetTag' not found in '$scrollableTag' (down) after $maxScrollAttempts tries",
    )
}
