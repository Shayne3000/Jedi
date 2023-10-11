package com.senijoshua.jedi.data.remote

import com.squareup.moshi.Json
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
    @Json(name = "hair_color")
    val hairColor: String,
    @Json(name = "skin_color")
    val skinColor: String,
    @Json(name = "eye_color")
    val eyeColor: String
)
