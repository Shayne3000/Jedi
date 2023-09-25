package com.senijoshua.jedi.ui.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class JediListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(JediListScreenUiState())
    val uiState: StateFlow<JediListScreenUiState> = _uiState


}
