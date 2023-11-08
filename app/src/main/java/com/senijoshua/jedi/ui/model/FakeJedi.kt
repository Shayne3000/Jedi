package com.senijoshua.jedi.ui.model

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
