package com.senijoshua.jedi.ui.list

import androidx.lifecycle.ViewModel
import com.senijoshua.jedi.ui.model.Jedi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class JediListViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(JediListScreenUiState())
    val uiState: StateFlow<JediListScreenUiState> = _uiState


    fun errorMessageShown() {
        _uiState.update { currentUiState ->
            currentUiState.copy(errorMessage = null)
        }
    }
}

/**
 * Data model representing the UI state of the JediListScreen at any instant in time.
 */
data class JediListScreenUiState(
    val isLoadingJedis: Boolean = false,
    val errorMessage: String? = null,
    val jedis: List<Jedi> = emptyList()
)
