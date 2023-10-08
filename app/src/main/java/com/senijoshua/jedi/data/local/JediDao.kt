package com.senijoshua.jedi.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface JediDao {
    @Upsert
    suspend fun insertAll(jediList: List<JediEntity>)

    @Upsert
    suspend fun insertOrUpdateJedi(jedi: JediEntity)
    // TODO use a query to update a part of an entry in the Jedi table that has an id matching the supplied id

    // Use Flows to track changes in the Jedi table to keep the displayed list of Jedis in the UI up-to-date
    @Query("SELECT * FROM jedi")
    fun getAllJedis(): Flow<List<JediEntity>>
    // TODO apply the distinctUntilChanged operator after the collect operator in the chain to ensure we only get notified when the data we're interested in changes

    @Query("SELECT * FROM jedi WHERE id = :jediId")
    suspend fun getJediById(jediId: Int): JediEntity

    @Query("DELETE FROM jedi")
    suspend fun deleteAll()
}
