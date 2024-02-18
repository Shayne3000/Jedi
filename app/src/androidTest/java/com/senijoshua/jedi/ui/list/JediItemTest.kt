package com.senijoshua.jedi.ui.list

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.ui.theme.JediTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented Unit test for a smaller, self-composed unit of the content of the JediListScreen,
 * the JediItem
 */
class JediItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testJedi = fakeJediList[0]

    @Before
    fun setUp() {
        composeTestRule.setContent {
            JediTheme {
                JediItem(jedi = testJedi)
            }
        }
    }

    @Test
    fun jediItem_showsData_onJediDataInjection() {
        composeTestRule.onNodeWithTag(JEDI_ITEM_CARD_TAG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(JEDI_ITEM_COLUMN_IN_CARD_TAG).assertIsDisplayed().assertHasClickAction()

        composeTestRule.onNodeWithText(testJedi.name).assertIsDisplayed()

        composeTestRule.onNode(hasText(testJedi.gender), useUnmergedTree = true).assertIsDisplayed()
    }
}
