package com.senijoshua.jedi.data.repository

import com.senijoshua.jedi.data.local.JediDao
import com.senijoshua.jedi.data.model.Jedi
import com.senijoshua.jedi.data.model.toExternalModel
import com.senijoshua.jedi.data.model.toLocal
import com.senijoshua.jedi.data.remote.JediApi
import com.senijoshua.jedi.data.util.Result
import com.senijoshua.jedi.data.util.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
) : JediRepository {

    private val dbCacheLimit = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

    /**
     * An observable stream of Jedi is returned from the DB which notifies us of
     * any changes to the Jedi table and retrieves fresh data from the
     * network if the DB is empty or the held data is stale.
     */
    override suspend fun getJedisStream(): Flow<Result<List<Jedi>>> {
        return db.getAllJedis()
            .map { jediEntities -> jediEntities.toExternalModel() }
            .onEach {
                if (jediTableIsEmptyOrHasStaleData()) {
                    val jediResponse = apiService.getJedis()
                    db.insertAll(jediResponse.results.toLocal())
                }
            }.distinctUntilChanged().asResult()
    }

    override suspend fun getJediById(jediId: Int): Result<Jedi> {
        return try {
            Result.Success(db.getJediById(jediId).toExternalModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * An invalidation paradigm for cached data that facilitates the retrieval
     * of fresh data from the remote service if the currently held data
     * has been stored for over an hour. It also implicitly verifies whether
     * the table is empty or not.
     *
     * NB: `getTimeCreated()` would return null if the table is empty.
     */
    private suspend fun jediTableIsEmptyOrHasStaleData(): Boolean {
        return (System.currentTimeMillis() - (db.getTimeCreated()
            ?: 0)) > dbCacheLimit
    }
}
