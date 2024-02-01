package com.senijoshua.jedi.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.jedi.data.model.Jedi
import com.senijoshua.jedi.data.repository.JediRepository
import com.senijoshua.jedi.data.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JediListViewModel @Inject constructor(
    private val jediRepository: JediRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(JediListScreenUiState())

    // NB: This is for triaging purposes and a bit of overkill, a correct way would be to use stateIn when dealing with complex
    // data-layer requests or a Flow for which there are multiple consumers/collectors listening for values.
    val uiState: StateFlow<JediListScreenUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _uiState.value
    )

    fun loadJedis() {
        viewModelScope.launch {
            jediRepository.getJedisStream().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { currentUiState ->
                            // return a copy of the current state but now with some properties changed
                            currentUiState.copy(
                                isLoadingJedis = false,
                                jedis = result.data
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                isLoadingJedis = false,
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
    val isLoadingJedis: Boolean = true,
    val errorMessage: String? = null,
    val jedis: List<Jedi> = emptyList()
)
