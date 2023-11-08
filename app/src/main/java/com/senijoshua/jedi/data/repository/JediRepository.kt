package com.senijoshua.jedi.data.repository

import com.senijoshua.jedi.data.model.Jedi
import com.senijoshua.jedi.data.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * This is the interface through which higher layers in the hierarchy
 * (i.e. the presentation layer) communicates with the data layer.
 *
 * This is in keeping with the dependency inversion principle where
 * we depend on interfaces instead of concrete implementations for better decoupling
 * and testing.
 */
interface JediRepository {
    suspend fun getJedisStream(): Flow<Result<List<Jedi>>>

    suspend fun getJediById(jediId: Int): Result<Jedi>
}
