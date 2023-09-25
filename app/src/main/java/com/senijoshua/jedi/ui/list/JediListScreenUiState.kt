package com.senijoshua.jedi.ui.list

/**
 * Data model representing the state of the JediListScreen at any instant in time
 * for ostensible use in the presentation layer.
 */
data class JediListScreenUiState(
    val isLoadingJedis: Boolean = false,
    val errorMessage: String? = null,
    val jediList: List<JediItemUiState> = listOf()
)

data class JediItemUiState(
    val name: String,
    val gender: String
)
