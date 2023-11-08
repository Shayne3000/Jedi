package com.senijoshua.jedi.ui.list

import com.senijoshua.jedi.data.repository.FakeJediRepository
import com.senijoshua.jedi.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class JediListViewModelTest {

    // Setup a test rule to replace the main dispatcher with a TestDispatcher for all test cases so that
    // an exception does not occur
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Fake dependencies
    private lateinit var repo: FakeJediRepository

    // Class under test
    private lateinit var vm: JediListViewModel

    @Before
    fun setUp() {
        repo = FakeJediRepository()
        vm = JediListViewModel(repo)
    }

    @Test
    fun `loading state shows on init with no error`() = runTest {
        assertTrue(vm.uiState.value.isLoadingJedis)
        assertTrue(vm.uiState.value.jedis.isEmpty())
        assertNull(vm.uiState.value.errorMessage)
    }

    @Test
    fun `jediLoad returns a list of jedi on success`() = runTest {
        vm.loadJedis()

        assertTrue(vm.uiState.value.jedis.isNotEmpty())
        assertEquals("Jedi 0 gender", vm.uiState.value.jedis[0].gender)
        assertNull(vm.uiState.value.errorMessage)
        assertFalse(vm.uiState.value.isLoadingJedis)
    }

    @Test
    fun `jediLoad returns an error on jedi load failure`() = runTest {
        repo.shouldThrowError = true

        vm.loadJedis()

        assertEquals("error", vm.uiState.value.errorMessage)
        assertTrue(vm.uiState.value.jedis.isEmpty())
        assertFalse(vm.uiState.value.isLoadingJedis)

        vm.errorMessageShown()
        assertNull(vm.uiState.value.errorMessage)
    }
}
