package com.senijoshua.jedi.ui.list

import com.senijoshua.jedi.data.Jedi

val fakeJediList = List(10) { index ->
    Jedi(
        index, 
        "Jedi $index", 
        "Jedi $index gender",
        "Jedi $index height",
        "Jedi $index mass",
        "Jedi $index hairColor",
        "Jedi $index skinColor"
    )
}

val jediListPreviewUiState = JediListScreenUiState(
    jedis = fakeJediList
)
