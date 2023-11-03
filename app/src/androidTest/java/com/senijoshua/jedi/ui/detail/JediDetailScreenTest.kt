package com.senijoshua.jedi.ui.detail

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.SavedStateHandle
import com.senijoshua.jedi.R
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.data.repository.FakeJediRepositoryImpl
import com.senijoshua.jedi.ui.MainActivity
import com.senijoshua.jedi.ui.theme.JediTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Integration test for the JediDetailScreen; testing its interaction
 * with the JediDetailViewModel
 */
@HiltAndroidTest
@ExperimentalCoroutinesApi
class JediDetailScreenTest {
    // Setup Hilt Rule
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // Setup Compose test rule for the Main activity
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Setup dependencies and any string resources
    private lateinit var jediRepository: FakeJediRepositoryImpl

    private lateinit var backIcon: String

    @Before
    fun setUp() {
        // no hilt inject as we're not injecting anything with hilt
        jediRepository = FakeJediRepositoryImpl()

        composeTestRule.activity.apply {
            backIcon = getString(R.string.back_icon)
        }
    }

    // Setup Test cases that test the state of the screen
    @Test
    fun jediDetailScreen_showsCorrectJedi_onLoadSuccess() {
        val jedi = fakeJediList[1]

        setContent(jedi.name)

        composeTestRule.onNodeWithContentDescription(backIcon).assertIsDisplayed()
        composeTestRule.onNodeWithText(jedi.name).assertIsDisplayed()
        composeTestRule.onNode(hasTestTag(CONSTRAINT_LAYOUT_TAG)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(CONSTRAINT_LAYOUT_TAG).onChildren().assertCountEquals(5)
        composeTestRule.onNodeWithTag(jedi.gender).assertIsDisplayed()
    }

    @Test
    fun jediDetailScreen_showsError_onLoadFailure() {
        val jedi = fakeJediList[1]

        jediRepository.shouldThrowError = true

        setContent(jedi.name)

        composeTestRule.onNodeWithText(jedi.name).assertIsDisplayed()

        composeTestRule.onNode(hasTestTag(CONSTRAINT_LAYOUT_TAG)).assertDoesNotExist()
        composeTestRule.onNodeWithTag(jedi.gender).assertDoesNotExist()

        composeTestRule.onNodeWithText("error").assertIsDisplayed()
    }

    /**
     * Setup content under test
     */
    private fun setContent(jediName: String) {
        composeTestRule.activity.setContent {
            JediTheme {
                JediDetailScreen(
                    topBarTitle = jediName,
                    viewModel = JediDetailViewModel(
                        SavedStateHandle(mapOf(JEDI_DETAIL_ID_ARG to "1")),
                        jediRepository
                    )
                )
            }
        }
    }
}
