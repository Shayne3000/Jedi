package com.senijoshua.jedi.data.remote

import retrofit2.http.GET

/**
 * Retrofit API for the Jedi resource.
 */
interface JediApi {
    @GET("people")
    suspend fun getJedi(): JediResponse
}
