package com.senijoshua.jedi.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.jedi.ui.model.Jedi
import com.senijoshua.jedi.data.repository.JediRepository
import com.senijoshua.jedi.data.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JediDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val jediRepository: JediRepository
) : ViewModel() {
    private val jediId: Int = savedStateHandle.get<String>(JEDI_DETAIL_ID_ARG)!!.toInt()

    private val _uiState =
        MutableStateFlow<JediDetailScreenUiState>(JediDetailScreenUiState.Loading)
    val uiState: StateFlow<JediDetailScreenUiState> = _uiState

    fun getJedi() {
        viewModelScope.launch {
            when (val jediResult = jediRepository.getJediById(jediId)) {
                is Result.Success -> {
                    _uiState.value = JediDetailScreenUiState.Success(jediResult.data)
                }

                is Result.Error -> {
                    _uiState.value = JediDetailScreenUiState.Error(jediResult.error.message)
                }
            }
        }
    }

    fun errorMessageShown() {
        _uiState.value = JediDetailScreenUiState.Error(null)
    }
}

sealed interface JediDetailScreenUiState {
    data class Success(val jedi: Jedi) : JediDetailScreenUiState
    data class Error(val errorMessage: String?) : JediDetailScreenUiState
    object Loading : JediDetailScreenUiState
}
