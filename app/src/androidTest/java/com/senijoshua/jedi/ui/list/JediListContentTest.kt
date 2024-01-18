package com.senijoshua.jedi.ui.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.senijoshua.jedi.R
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.ui.components.JEDI_PROGRESS_TAG
import com.senijoshua.jedi.ui.theme.JediTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented UI unit test for the JediListScreen content.
 */
class JediListContentTest {
    // set compose rule
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // set up any dependencies like string resources used in the content
    private lateinit var jediListTitle: String
    private lateinit var jediMasters: String

    @Before
    fun setUp() {
        composeTestRule.activity.apply {
            // access string resources used in the content for subsequent assertions
            jediListTitle = getString(R.string.jedi_list_title)
            jediMasters = getString(R.string.jedi_list_screen_header)
        }
    }

    // Test cases for testing the UI screen's behaviour in every screen state
    @Test
    fun jediListContent_showsLoadingStateElements_OnLoading() {
        setupScreenContentUnderTest(
            JediListScreenUiState(isLoadingJedis = true)
        )

        composeTestRule.onNodeWithText(jediListTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(jediMasters).assertIsDisplayed()
        composeTestRule.onNode(
            hasProgressBarRangeInfo(
                ProgressBarRangeInfo(
                    current = 0.0F,
                    range = 0.0F..0.0F,
                    steps = 0
                )
            )
        ).assertIsDisplayed()
        composeTestRule.onNode(hasText(fakeJediList[0].name)).assertDoesNotExist()
    }

    @Test
    fun jediListContent_showsJediList_onSuccessfulLoad() {
        setupScreenContentUnderTest(
            JediListScreenUiState(isLoadingJedis = false, jedis = fakeJediList)
        )

        composeTestRule.onNodeWithTag(JEDI_LIST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeJediList[0].name).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeJediList[0].gender).assertIsDisplayed()
        composeTestRule.onNode(
            hasTestTag(JEDI_PROGRESS_TAG)
        ).assertDoesNotExist()
    }

    @Test
    fun jediListContent_showsErrorSnackBar_onLoadFailure() {
        val errorMessage = "Error!"

        setupScreenContentUnderTest(
            JediListScreenUiState(isLoadingJedis = false, errorMessage)
        )

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithTag(JEDI_LIST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(JEDI_PROGRESS_TAG).assertDoesNotExist()
    }

    private fun setupScreenContentUnderTest(testUiState: JediListScreenUiState) {
        composeTestRule.setContent {
            JediTheme {
                JediListContent(uiState = testUiState)
            }
        }
    }
}
