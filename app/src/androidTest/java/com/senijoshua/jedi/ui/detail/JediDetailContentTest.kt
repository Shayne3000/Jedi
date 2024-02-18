package com.senijoshua.jedi.ui.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
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
 * Instrumented unit UI test for the JediDetailScreen content.
 */
class JediDetailContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var backIcon: String

    @Before
    fun setUp() {
        composeTestRule.activity.apply {
            backIcon = getString(R.string.back_icon)
        }
    }

    @Test
    fun jediDetailContent_showsLoadingElements_onLoading() {
        val jedi = fakeJediList[0]
        setJediDetailContent(jedi.name, JediDetailScreenUiState.Loading)

        composeTestRule.onNodeWithContentDescription(backIcon).assertIsDisplayed()
        composeTestRule.onNodeWithText(jedi.name).assertIsDisplayed()
        composeTestRule.onNodeWithTag(JEDI_PROGRESS_TAG).assertIsDisplayed()
    }

    @Test
    fun jediDetailContent_showsErrorSnackBar_onLoadError() {
        val errorMessage = "error"
        val jedi = fakeJediList[0]
        setJediDetailContent(jedi.name, JediDetailScreenUiState.Error(errorMessage = errorMessage))

        composeTestRule.onNodeWithContentDescription(backIcon).assertIsDisplayed()
        composeTestRule.onNodeWithText(jedi.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithTag(CONSTRAINT_LAYOUT_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(jedi.gender).assertDoesNotExist()
    }

    @Test
    fun jediDetailContent_showsJediDetails_onLoadSuccess() {
        val jedi = fakeJediList[5]
        setJediDetailContent(jedi.name, JediDetailScreenUiState.Success(jedi))

        composeTestRule.onNodeWithContentDescription(backIcon).assertIsDisplayed()
        composeTestRule.onNodeWithText(jedi.name).assertIsDisplayed()
        composeTestRule.onNodeWithTag(JEDI_PROGRESS_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(CONSTRAINT_LAYOUT_TAG).onChildren().assertCountEquals(5)
        composeTestRule.onNodeWithTag(jedi.gender).assertIsDisplayed()
        composeTestRule.onNodeWithTag(jedi.mass).assertIsDisplayed()
        composeTestRule.onNodeWithTag(jedi.height).assertIsDisplayed()
        composeTestRule.onNodeWithTag(jedi.hairColor).assertIsDisplayed()
        composeTestRule.onNodeWithTag(jedi.skinColor).assertIsDisplayed()
    }

    private fun setJediDetailContent(topBarTitle: String, uiState: JediDetailScreenUiState) {
        composeTestRule.setContent {
            JediTheme {
                JediDetailContent(topBarTitle = topBarTitle, uiState = uiState)
            }
        }
    }
}
