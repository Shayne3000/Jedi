package com.senijoshua.jedi.ui.list

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.senijoshua.jedi.R
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.data.repository.FakeJediRepository
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
 * Integration test for the JediListScreen verifying its interaction with
 * the JediListViewModel.
 */
@HiltAndroidTest
class JediListScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Really not necessary just wanted to triage the approach of
    // letting hilt replace the injected implementation with a test alternative
    @Inject
    lateinit var repository: JediRepository
    private lateinit var jediListTitle: String

    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.activity.apply {
            jediListTitle = getString(R.string.jedi_list_title)
        }
    }

    @Test
    fun jediListScreen_showsJediList_onStartupAndLoadSuccess() {
        val errorText = "error"

        setContent(repository)

        composeTestRule.onNodeWithText(jediListTitle).assertIsDisplayed()
        composeTestRule.onNodeWithTag(JEDI_LIST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithText(errorText).assertDoesNotExist()
        composeTestRule.onNodeWithTag(JEDI_PROGRESS_TAG).assertDoesNotExist()

        composeTestRule.onNodeWithText(fakeJediList[0].name).assertExists()
        composeTestRule.onNodeWithText(fakeJediList[1].name).assertExists()
    }

    @Test
    fun jediListScreen_showErrorSnackBar_onLoadFailure() {
        val fakeRepository = (repository as FakeJediRepository)

        fakeRepository.shouldThrowError = true

        setContent(fakeRepository)

        composeTestRule.onNodeWithText(jediListTitle).assertIsDisplayed()
        composeTestRule.onNodeWithTag(JEDI_LIST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithText(fakeRepository.errorText).assertIsDisplayed()
    }

    /**
     * Setup content under test
     */
    private fun setContent(repository: JediRepository) {
        composeTestRule.activity.setContent {
            JediTheme {
                JediListScreen(viewModel = JediListViewModel(repository))
            }
        }
    }
}
