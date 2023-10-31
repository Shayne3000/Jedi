package com.senijoshua.jedi.data.remote

class FakeApi : JediApi {
    val dummyNetworkJedi = List(10) { index ->
        NetworkJedi(
            "Jedi $index",
            "Jedi $index gender",
            "Jedi $index height",
            "Jedi $index mass",
            "Jedi $index hairColor",
            "Jedi $index skinColor",
            "Jedi $index eyeColor"
        )
    }

    var shouldThrowError = false

    override suspend fun getJedis(): JediResponse {
        return if (shouldThrowError) {
            throw Exception("error")
        } else {
            JediResponse(results = dummyNetworkJedi)
        }
    }
}
