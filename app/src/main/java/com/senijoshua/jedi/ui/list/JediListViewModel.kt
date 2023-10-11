package com.senijoshua.jedi.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.jedi.data.Jedi
import com.senijoshua.jedi.data.JediRepository
import com.senijoshua.jedi.data.Result
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

    init {
        _uiState.update { currentUiState -> currentUiState.copy(isLoadingJedis = true) }
    }

    fun loadJedis() {
        viewModelScope.launch {
            jediRepository.getJedis().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                isLoadingJedis = false,
                                jedis = result.data
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { currentUiState -> currentUiState.copy(errorMessage = result.error.message) }
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
 * // TODO Use a Sealed interface for states in the detail screen instead of a data class.
 */
data class JediListScreenUiState(
    val isLoadingJedis: Boolean = false,
    val errorMessage: String? = null,
    val jedis: List<Jedi> = emptyList()
)
