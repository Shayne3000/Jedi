package com.senijoshua.jedi.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.jedi.data.model.Jedi
import com.senijoshua.jedi.data.repository.JediRepository
import com.senijoshua.jedi.data.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JediListViewModel @Inject constructor(
    private val jediRepository: JediRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(JediListScreenUiState())

    val uiState: StateFlow<JediListScreenUiState> = _uiState

    fun loadJedi() {
        viewModelScope.launch {
            jediRepository.getJediStream().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                isLoadingJedi = false,
                                jedi = result.data
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                isLoadingJedi = false,
                                errorMessage = result.error.message
                            )
                        }
                    }
                }
            }
        }
    }

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
    val isLoadingJedi: Boolean = true,
    val errorMessage: String? = null,
    val jedi: List<Jedi> = emptyList()
)
