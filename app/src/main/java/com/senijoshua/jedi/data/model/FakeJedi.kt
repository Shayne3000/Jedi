package com.senijoshua.jedi.data.model

/**
 * Dummy implementation of the Jedi model that can be used in tests.
 * NB: Ideally this could be in a shared test module that local and
 * instrumented tests can access.
 */
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
