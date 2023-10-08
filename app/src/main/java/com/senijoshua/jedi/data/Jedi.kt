package com.senijoshua.jedi.data

/**
 * Data type for use in higher layers of
 * the hierarchy that are external to the data layer i.e. presentation layer.
 *
 * It only holds information relevant to said layer. In other words, it is
 * the representation of a Jedi type in the presentation layer.
 */
data class Jedi(
    val id: String,
    val name: String,
    val gender: String
)
