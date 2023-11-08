package com.senijoshua.jedi.ui.model

/**
 * Data type for use in higher layers of
 * the hierarchy that are external to the data layer i.e. presentation layer.
 *
 * It only holds information relevant to said layer. In other words, it is
 * the representation of a Jedi type in the presentation layer.
 */
data class Jedi(
    val id: Int,
    val name: String,
    val gender: String,
    val height: String,
    val mass: String,
    val hairColor: String,
    val skinColor: String
)
