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
import com.senijoshua.jedi.data.model.Jedi
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.data.repository.FakeJediRepository
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
 * with the JediDetailViewModel.
 */
@HiltAndroidTest
@ExperimentalCoroutinesApi
class JediDetailScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var jediRepository: FakeJediRepository

    private lateinit var backIcon: String

    @Before
    fun setUp() {
        jediRepository = FakeJediRepository()

        composeTestRule.activity.apply {
            backIcon = getString(R.string.back_icon)
        }
    }

    @Test
    fun jediDetailScreen_showsCorrectJedi_onLoadSuccess() {
        val jedi = fakeJediList[1]

        setContent(jedi)

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

        setContent(jedi)

        composeTestRule.onNodeWithText(jedi.name).assertIsDisplayed()

        composeTestRule.onNode(hasTestTag(CONSTRAINT_LAYOUT_TAG)).assertDoesNotExist()
        composeTestRule.onNodeWithTag(jedi.gender).assertDoesNotExist()

        composeTestRule.onNodeWithText(jediRepository.errorText).assertIsDisplayed()
    }

    /**
     * Setup content under test
     */
    private fun setContent(jedi: Jedi) {
        composeTestRule.activity.setContent {
            JediTheme {
                JediDetailScreen(
                    topBarTitle = jedi.name,
                    viewModel = JediDetailViewModel(
                        SavedStateHandle(mapOf(JEDI_DETAIL_ID_ARG to jedi.id.toString())),
                        jediRepository
                    )
                )
            }
        }
    }
}
