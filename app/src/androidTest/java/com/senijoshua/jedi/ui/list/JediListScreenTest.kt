package com.senijoshua.jedi.ui.list

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.senijoshua.jedi.R
import com.senijoshua.jedi.ui.model.fakeJediList
import com.senijoshua.jedi.data.repository.FakeJediRepositoryImpl
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
    // Setup hilt rule to manage component's state
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // Setup Compose test rule for the Main activity
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Setup dependencies for injection and any needed resources

    // NB: Ideally, there's no need to inject this with Hilt as we can use
    // the FakeJediRepositoryImpl implementation of JediRepository directly
    // as done in the JediDetailScreenTest but we follow this paradigm here
    // for demonstration purposes.
    @Inject
    lateinit var repository: JediRepository
    private lateinit var jediListTitle: String

    @Before
    fun setUp() {
        // inject repository with the FakeJediRepositoryImpl implementation here.
        hiltRule.inject()

        composeTestRule.activity.apply {
            jediListTitle = getString(R.string.jedi_list_title)
        }
    }

    @Test
    fun jediListScreen_showsJediList_onStartupAndLoadSuccess() {
        setContent(repository)

        composeTestRule.onNodeWithText(jediListTitle).assertIsDisplayed()
        composeTestRule.onNodeWithTag(JEDI_LIST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithText("error").assertDoesNotExist()
        composeTestRule.onNodeWithTag(JEDI_PROGRESS_TAG).assertDoesNotExist()

        // NB: Cannot test whether all items in the list exist as LazyColumn doesn't
        // load all items at once. It loads items when they are about to be on screen.
        // If they aren't visible yet, it doesn't load it.

        // Trying to test for such will make this test flaky as the amount of items shown on
        // screen varies depending on the size of the screen in which the app is executed.
        composeTestRule.onNodeWithText(fakeJediList[0].name).assertExists()
        composeTestRule.onNodeWithText(fakeJediList[1].name).assertExists()
    }

    @Test
    fun jediListScreen_showErrorSnackBar_onLoadFailure() {
        // get the injected repository implementation which is FakeJediRepositoryImpl
        // under the hood and cast it to the same class to modify its behaviour
        // before manual injection into the JediListViewModel.
        val fakeRepository = (repository as FakeJediRepositoryImpl)

        fakeRepository.shouldThrowError = true

        // start the screen in the activity
        setContent(fakeRepository)

        composeTestRule.onNodeWithText(jediListTitle).assertIsDisplayed()
        composeTestRule.onNodeWithTag(JEDI_LIST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithText("error").assertIsDisplayed()
    }

    /**
     * Setup content under test
     */
    private fun setContent(repository: JediRepository) {
        // NB: Call setContent on the activity of the rule directly as opposed to doing
        // so on the rule (i.e. composeTestRule.setContent) else an IllegalStateException
        // will be thrown since the activity will call setContent before the rule does.
        composeTestRule.activity.setContent {
            JediTheme {
                JediListScreen(viewModel = JediListViewModel(repository))
            }
        }
    }
}
