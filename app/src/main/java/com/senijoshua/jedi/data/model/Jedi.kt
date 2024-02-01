package com.senijoshua.jedi.data.model

/**
 * Representation of a Jedi type in the presentation layer that
 * only holds information that is relevant to said layer.
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
