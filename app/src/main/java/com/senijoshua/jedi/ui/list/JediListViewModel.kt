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

    init {
        // NB: This is simply for triaging purposes. Google does not recommend triggering asychronous operations as a side-effect of
        // creating an instance as the asychronous op may return before it is fully initialized; a phenomena known as leaking the instance.
        // See: https://developer.android.com/topic/architecture/ui-layer/state-production#stateflow_2 for more.
        // loadJedis()
    }

    fun loadJedis() {
        viewModelScope.launch {
            jediRepository.getJedisStream().collect { result ->
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
