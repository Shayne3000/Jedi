package com.senijoshua.jedi.data.repository

import com.senijoshua.jedi.data.local.JediCacheLimit
import com.senijoshua.jedi.data.local.JediDao
import com.senijoshua.jedi.data.model.Jedi
import com.senijoshua.jedi.data.model.toExternalModel
import com.senijoshua.jedi.data.model.toLocal
import com.senijoshua.jedi.data.remote.JediApi
import com.senijoshua.jedi.data.util.Result
import com.senijoshua.jedi.data.util.asResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Implementation of [JediRepository] that combines data from local and remote data sources
 * and abstracts away implementation details of data retrieval from higher layers
 * of the architectural hierarchy.
 *
 * This facilitates an offline-first paradigm by employing the DB as
 * the single source of truth for getting a list of Jedi.
 */
class OfflineFirstJediRepository @Inject constructor(
    private val apiService: JediApi,
    private val db: JediDao,
    private val dispatcher: CoroutineDispatcher,
    private val cacheLimit: JediCacheLimit,
) : JediRepository {

    /**
     * An observable stream of Jedi is returned from the DB which notifies us of
     * any changes to the Jedi table and retrieves fresh data from the
     * network if the DB is empty or the held data is stale.
     */
    override suspend fun getJediStream(): Flow<Result<List<Jedi>>> {
        return db.getAllJedi()
            .map { jediEntities -> jediEntities.toExternalModel() }
            .onEach {
                if (cachedDataIsEmptyOrStale()) {
                    val jediResponse = apiService.getJedi()

                    if (canCleanUpData()) {
                        db.clear()
                    }

                    db.insertAll(jediResponse.results.toLocal())
                }
            }
            .flowOn(dispatcher)
            .asResult()
    }

    override suspend fun getJediById(jediId: Int): Result<Jedi> {
        return try {
            Result.Success(db.getJediById(jediId).toExternalModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * A cache invalidation paradigm that facilitates the retrieval
     * of fresh data from the remote service if the currently held data is stale
     * (i.e. has been stored for over an hour). It also implicitly verifies
     * whether the table is empty or not as `getTimeCreated()` returns null
     * if the table is empty.
     */
    private suspend fun cachedDataIsEmptyOrStale(): Boolean {
        return db.getTimeCreated()
            ?.let { timeCreated -> (System.currentTimeMillis() - (timeCreated)) > cacheLimit.dbRefreshCacheLimit }
            // The DB is empty
            ?: true
    }

    /**
     * An invalidation paradigm for the cache that clears all the data in the jedi table
     * for refilling with new data as data is considered redundant if there
     * exists data in the DB that was inserted more than a week ago.
     *
     * Basically, we clear up the database after 1 week.
     */
    private suspend fun canCleanUpData(): Boolean {
        return db.getTimeCreated()
            ?.let { timeCreated -> (System.currentTimeMillis() - (timeCreated)) > cacheLimit.dbClearCacheLimit }
            // The DB is empty, don't waste resources cleaning an empty DB
            ?: false
    }

}
