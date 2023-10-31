package com.senijoshua.jedi.ui.list

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.data.repository.JediRepository
import com.senijoshua.jedi.ui.MainActivity
import com.senijoshua.jedi.ui.components.JEDI_PROGRESS_TAG
import com.senijoshua.jedi.ui.theme.JediTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Integration test for the JediListScreen; testing its interaction with
 * the JediListViewModel.
 */
@HiltAndroidTest
class JediListScreenTest {
    // Setup test rules
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Setup any dependencies for injection and access any needed resources
    @Inject
    lateinit var repository: JediRepository

    // Setup content under test
    @Before
    fun setUp() {
        hiltRule.inject()

        // NB: Call setContent on the specific activity of the rule directly as opposed
        // to doing so on the rule (i.e. composeTestRule.setContent) else an IllegalStateException
        // will be thrown since the activity calls setContent before the rule does.
        composeTestRule.activity.setContent {
            JediTheme {
                JediListScreen(viewModel = JediListViewModel(repository))
            }
        }
    }

    @Test
    fun jediListScreen_showsJediList_onStartupAndLoadSuccess() {
        composeTestRule.onNodeWithTag(JEDI_LIST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithText("error").assertDoesNotExist()
        composeTestRule.onNodeWithTag(JEDI_PROGRESS_TAG).assertDoesNotExist()

        // NB: Cannot test if all items in the list exist as LazyColumn doesn't
        // load all items at once. It loads items when they are about to be on screen.
        // If they aren't visible yet, it doesn't load it.

        // Trying to test for such will make this test flaky as the amount of items shown on
        // screen varies depending on the size of the screen in which the app is executed.
        composeTestRule.onNodeWithText(fakeJediList[0].name).assertExists()
        composeTestRule.onNodeWithText(fakeJediList[1].name).assertExists()
    }
}
