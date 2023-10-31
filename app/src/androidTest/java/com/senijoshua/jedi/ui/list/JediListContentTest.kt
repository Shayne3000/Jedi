package com.senijoshua.jedi.ui.list

import androidx.compose.ui.test.junit4.createComposeRule
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.ui.theme.JediTheme
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test for the actual screen content section of the JediListScreen
 */
class JediListContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testJediListScreenUiState = JediListScreenUiState(
        jedis = fakeJediList,
        isLoadingJedis = false,
        errorMessage = null
    )

    @Before
    fun setUp() {
        composeTestRule.setContent {
            JediTheme {
                JediListContent(uiState = testJediListScreenUiState)
            }
        }
    }



    // test cases
}
