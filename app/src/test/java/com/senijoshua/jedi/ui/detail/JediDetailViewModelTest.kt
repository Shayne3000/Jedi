package com.senijoshua.jedi.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.data.repository.FakeJediRepository
import com.senijoshua.jedi.util.ERROR_TEXT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Test class for [JediDetailViewModel] that demonstrates testing
 * with coroutines sans the MainDispatcherRule.
 */
@ExperimentalCoroutinesApi
class JediDetailViewModelTest {

    private lateinit var repo: FakeJediRepository

    private lateinit var vm: JediDetailViewModel

    @Before
    fun setUp() {
        repo = FakeJediRepository()
        vm = JediDetailViewModel(SavedStateHandle(mapOf(JEDI_DETAIL_ID_ARG to "1")), repo)
    }

    @Test
    fun `Given a passed-in id of a jedi, getJedi returns the correct jedi model on success`() =
        runTest {
            // Replace the Main dispatcher with a test dispatcher that does not
            // run coroutines immediately but queues them on the scheduler.
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            vm.getJedi()

            assertEquals(JediDetailScreenUiState.Loading, vm.uiState.value)

            advanceUntilIdle()

            assertEquals(JediDetailScreenUiState.Success(jedi = fakeJediList[1]), vm.uiState.value)

            Dispatchers.resetMain()
        }

    @Test
    fun `getJedi returns an error on failure`() = runTest {
        // Replace the Main dispatcher with a test dispatcher that runs
        // the coroutines eagerly/immediately.
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))

        repo.shouldThrowError = true
        assertEquals(JediDetailScreenUiState.Loading, vm.uiState.value)

        vm.getJedi()

        assertEquals(JediDetailScreenUiState.Error(errorMessage = ERROR_TEXT), vm.uiState.value)

        Dispatchers.resetMain()
    }
}
