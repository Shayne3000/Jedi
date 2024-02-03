package com.senijoshua.jedi.data.repository

import com.senijoshua.jedi.data.local.JediDao
import com.senijoshua.jedi.data.model.Jedi
import com.senijoshua.jedi.data.model.toExternalModel
import com.senijoshua.jedi.data.model.toLocal
import com.senijoshua.jedi.data.remote.JediApi
import com.senijoshua.jedi.data.util.Result
import com.senijoshua.jedi.data.util.asResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Implementation of [JediRepository] that combines data from local and remote data sources
 * and abstracts away implementation details of data retrieval from higher layers
 * of the architectural hierarchy.
 *
 * This facilitates an offline-first paradigm by employing the DB as
 * the single source of truth for getting a list of Jedis.
 *
 * NB: Data source classes aren't explicitly used as that would just be unnecessary bloat at this
 * scale.
 */
class OfflineFirstJediRepository @Inject constructor(
    private val apiService: JediApi,
    private val db: JediDao,
    private val dispatcher: CoroutineDispatcher
) : JediRepository {

    private val dbCacheLimit = TimeUnit.MILLISECONDS.convert(15, TimeUnit.MINUTES)

    /**
     * An observable stream of Jedi is returned from the DB which notifies us of
     * any changes to the Jedi table and retrieves fresh data from the
     * network if the DB is empty or the held data is stale.
     *
     * NB: May not need to move the execution of the coroutine off the Main thread
     * to the IO thread as Retrofit & Room perform suspend ops using the IO dispatcher,
     * however doing so is helpful when unit testing this function.
     */
    override suspend fun getJedisStream(): Flow<Result<List<Jedi>>> {
        return withContext(dispatcher) {
            if (cachedDataIsStale()) {
                val jediResponse = apiService.getJedis()
                db.insertAll(jediResponse.results.toLocal())
            }

            db.getAllJedis().map { jediList ->
                jediList.toExternalModel()
            }.distinctUntilChanged().asResult()
        }
    }

    override suspend fun getJediById(jediId: Int): Result<Jedi> {
        return withContext(dispatcher) {
            try {
                Result.Success(db.getJediById(jediId).toExternalModel())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    private suspend fun cachedDataIsStale(): Boolean {
        // NB: This also implicitly verifies that the table
        // is not empty. If it is, `getTimeCreated` would return null.
        return (System.currentTimeMillis() - (db.getTimeCreated()
            ?: 0)) > dbCacheLimit
    }
}
