package com.senijoshua.jedi.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.senijoshua.jedi.data.Jedi
import com.senijoshua.jedi.data.JediRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class JediDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    jediRepository: JediRepository
) : ViewModel() {
    private val jediId: Int = savedStateHandle[JEDI_DETAIL_ID_ARG]!!

    private val _uiState =
        MutableStateFlow<JediDetailScreenUiState>(JediDetailScreenUiState.Loading)
    val uiState: StateFlow<JediDetailScreenUiState> = _uiState.asStateFlow()


    fun errorMessageShown() {
        _uiState.value = JediDetailScreenUiState.Error(null)
    }
}

sealed interface JediDetailScreenUiState {
    data class Success(val jedi: Jedi) : JediDetailScreenUiState
    data class Error(val errorMessage: String?) : JediDetailScreenUiState
    object Loading : JediDetailScreenUiState
}
