package com.senijoshua.jedi.data

import com.senijoshua.jedi.data.local.JediDao
import com.senijoshua.jedi.data.remote.JediApi
import com.senijoshua.jedi.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Abstraction that encapsulates and hides the implementation details of data retrieval
 * from higher layers of the architectural hierarchy.
 *
 * This repository facilitates an offline-first paradigm by employing the DB as
 * the single source of truth for getting a list of Jedis.
 *
 */
class JediRepository @Inject constructor(
    private val apiService: JediApi,
    private val db: JediDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
){
    /**
     * An observable stream of jedis is returned from the DB which allows us to be
     * notified of any updates to the Jedi table in the DB and retrieves fresh data from the
     * network if the table is empty.
     */
    suspend fun getJedis(): Flow<Result<List<Jedi>>> {
        return withContext(dispatcher) {
             db.getAllJedis()
                .map { jediList ->
                    jediList.toExternalModel()
                }.onEach { jediList ->
                    if (jediList.isEmpty()) {
                        val jediResponse = apiService.getJedis()
                        db.insertAll(jediResponse.results.toLocal())
                    }
                }
                 // We apply the distinctUntilChanged operator in the chain to ensure we only get notified when the data we're interested in changes.
                .distinctUntilChanged()
                .asResult()


            // TODO May not need to move the execution of the coroutine off the main thread to the
            //  io thread with an injected Dispatcher because both Retrofit & Room now performs suspendable operations using Dispatcher.IO by default.
            //  Though that makes one wonder how to inject a test dispatcher to unit test this function when JediRepo is under test.
        }
    }
}
