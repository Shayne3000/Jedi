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

    @Query("SELECT * FROM jedi WHERE id = :jediId")
    suspend fun getJediById(jediId: Int): JediEntity

    @Query("DELETE FROM jedi")
    suspend fun deleteAll()
}
