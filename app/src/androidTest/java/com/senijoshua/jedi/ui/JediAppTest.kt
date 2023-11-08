package com.senijoshua.jedi.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.senijoshua.jedi.R
import com.senijoshua.jedi.ui.model.fakeJediList
import com.senijoshua.jedi.ui.detail.CONSTRAINT_LAYOUT_TAG
import com.senijoshua.jedi.ui.list.JEDI_LIST_TAG
import com.senijoshua.jedi.ui.theme.JediTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * End-to-end test that tests the complete app flow of starting the app,
 * loading up a list of Jedis, clicking on one of them, going to the detail screen
 * to view the details of said Jedi and then returning to the previous screen.
 *
 * NB: User flow tests can double as navigation tests too.
 */
@HiltAndroidTest
class JediAppTest {
    // Setup test rules
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Setup and inject dependencies required in the test
    private lateinit var jediListTitle: String
    private lateinit var jediMasters: String
    private lateinit var backIcon: String
    private lateinit var gender: String

    @Before
    fun setUp() {
        composeTestRule.activity.setContent{
            // start the app
            JediTheme {
                JediApp()
            }
        }
        composeTestRule.activity.apply {
            jediListTitle = getString(R.string.jedi_list_title)
            jediMasters = getString(R.string.jedi_list_screen_header)
            backIcon = getString(R.string.back_icon)
            gender = getString(R.string.gender)
        }
    }

    @Test
    fun jediApp_loadsUpJediList_clicksJediItem_showsJediDetails_returnsToHome() {
        // verify that the Jedi List Screen has been displayed
        composeTestRule.onNodeWithText(jediListTitle).assertIsDisplayed()

        composeTestRule.onNodeWithText(jediMasters).assertIsDisplayed()

        // verify that the jedi list loaded up successfully
        composeTestRule.onNodeWithTag(JEDI_LIST_TAG).assertIsDisplayed()

        composeTestRule.onNodeWithText(fakeJediList[0].name).assertIsDisplayed()

        // Click a list item
        composeTestRule.onNodeWithText(fakeJediList[0].name).performClick()

        // Verify that the Jedi Detail Screen was succesfully navigated to and displayed
        composeTestRule.onNode(hasAnyChild(hasText(fakeJediList[0].name))).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription(backIcon).assertIsDisplayed()

        // Verify that the layout containing all the text items is displayed
        composeTestRule.onNodeWithTag(CONSTRAINT_LAYOUT_TAG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(CONSTRAINT_LAYOUT_TAG).onChildren().assertCountEquals(5)

        // Verify that all the text items are displayed
        composeTestRule.onNodeWithTag(fakeJediList[0].gender).assertIsDisplayed()
        composeTestRule.onNodeWithTag(fakeJediList[0].height).assertIsDisplayed()
        composeTestRule.onNodeWithTag(fakeJediList[0].mass).assertIsDisplayed()
        composeTestRule.onNodeWithTag(fakeJediList[0].hairColor).assertIsDisplayed()
        composeTestRule.onNodeWithTag(fakeJediList[0].skinColor).assertIsDisplayed()

        // Go back to the Main screen
        composeTestRule.onNodeWithContentDescription(backIcon).performClick()

        // Verify that the Jedi Home screen is displayed again.
        composeTestRule.onNodeWithText(jediListTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(jediMasters).assertIsDisplayed()
        composeTestRule.onNodeWithTag(JEDI_LIST_TAG).assertIsDisplayed()
    }
}
