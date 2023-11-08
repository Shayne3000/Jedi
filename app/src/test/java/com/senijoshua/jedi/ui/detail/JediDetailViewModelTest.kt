package com.senijoshua.jedi.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.data.repository.FakeJediRepositoryImpl
import com.senijoshua.jedi.util.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class JediDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repo: FakeJediRepositoryImpl

    private lateinit var vm: JediDetailViewModel

    @Before
    fun setUp() {
        repo = FakeJediRepositoryImpl()
        vm = JediDetailViewModel(SavedStateHandle(mapOf(JEDI_DETAIL_ID_ARG to "1")), repo)
    }

    @Test
    fun `Given a passed-in id of a jedi, getJedi returns the correct jedi model on success`() =
        runTest {
            // Set the Main dispatcher to a test dispatcher that does not
            // run coroutines immediately but queues them on the scheduler
            // for this test case alone.
            Dispatchers.setMain(StandardTestDispatcher())

            vm.getJedi()

            assertEquals(JediDetailScreenUiState.Loading, vm.uiState.value)

            advanceUntilIdle()

            assertEquals(JediDetailScreenUiState.Success(jedi = fakeJediList[1]), vm.uiState.value)
        }

    @Test
    fun `getJedi returns an error on failure`() = runTest {
        repo.shouldThrowError = true
        assertEquals(JediDetailScreenUiState.Loading, vm.uiState.value)

        vm.getJedi()

        assertEquals(JediDetailScreenUiState.Error(errorMessage = "error"), vm.uiState.value)
    }
}
