package com.senijoshua.jedi.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JediResponse(val results: List<NetworkJedi>)

/**
 * Network layer representation of a Jedi
 */
@JsonClass(generateAdapter = true)
data class NetworkJedi(
    val name: String,
    val gender: String,
    val height: String,
    val mass: String,
    val hairColor: String,
    val skinColor: String,
    val eyeColor: String
)
