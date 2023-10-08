package com.senijoshua.jedi.ui.model

import com.senijoshua.jedi.data.Jedi
import com.senijoshua.jedi.ui.list.JediListScreenUiState

val jediList = listOf(
    Jedi("1", "Luke Skywalker", "Male"),
    Jedi("2", "Qui Gon Jin", "Male"),
    Jedi("3", "Obi Wan Kenobi", "Male"),
    Jedi("4", "Ahsoka Tano", "Female"),
    Jedi("5", "Rey Skywalker", "Male"),
    Jedi("6", "Yoda", "Male")
)

val jediListPreviewUiState = JediListScreenUiState(
    jedis = jediList
)
