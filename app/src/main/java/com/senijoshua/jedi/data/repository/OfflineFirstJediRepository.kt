package com.senijoshua.jedi.data.repository

import com.senijoshua.jedi.data.local.JediDao
import com.senijoshua.jedi.data.local.JediEntity
import com.senijoshua.jedi.data.model.Jedi
import com.senijoshua.jedi.data.model.toExternalModel
import com.senijoshua.jedi.data.model.toLocal
import com.senijoshua.jedi.data.remote.JediApi
import com.senijoshua.jedi.data.util.Result
import com.senijoshua.jedi.data.util.asResult
import kotlinx.coroutines.flow.Flow
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
 */
class OfflineFirstJediRepository @Inject constructor(
    private val apiService: JediApi,
    private val db: JediDao,
) : JediRepository {

    private val dbRefreshCacheLimit = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
    private val dbClearCacheLimit = TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS)

    /**
     * An observable stream of Jedi is returned from the DB which notifies us of
     * any changes to the Jedi table and retrieves fresh data from the
     * network if the DB is empty or the held data is stale.
     */
    override suspend fun getJedisStream(): Flow<Result<List<Jedi>>> {
        return db.getAllJedis()
            .onEach {
                if (isJediDataStaleOrEmpty(it)) {
                    val jediResponse = apiService.getJedis()

                    if (canCleanUpOldData(it)) {
                        db.clear()
                    }

                    db.insertAll(jediResponse.results.toLocal())
                }
            }.map { jediEntities -> jediEntities.toExternalModel() }
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
    private fun isJediDataStaleOrEmpty(jedis: List<JediEntity>): Boolean {
        if (jedis.isEmpty()) {
            return true
        }

        return isLimitExceeded(jedis, dbRefreshCacheLimit)
    }

    /**
     * An invalidation paradigm for the cache that clears all the data in the jedi table
     * for refilling with new data as data is considered redundant if there
     * exists data in the DB that was inserted more than a week ago.
     *
     * Basically, we clear up the database after 1 week.
     */
    private fun canCleanUpOldData(jedis: List<JediEntity>) =
        isLimitExceeded(jedis, dbClearCacheLimit)

    private fun isLimitExceeded(jedis: List<JediEntity>, limit: Long) =
        (System.currentTimeMillis() - (jedis[0].timeCreated)) > limit

}
