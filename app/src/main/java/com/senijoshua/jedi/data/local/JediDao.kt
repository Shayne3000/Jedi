package com.senijoshua.jedi.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface JediDao {
    @Upsert
    suspend fun insertAll(jediList: List<JediEntity>)

    @Upsert
    suspend fun insertOrUpdateJedi(jedi: JediEntity)
    // TODO use a query to update a part of an entry in the Jedi table that has an id matching the supplied id

    @Query("SELECT * FROM jedi")
    suspend fun getAllJedis(): List<JediEntity>

    @Query("SELECT * FROM jedi WHERE id = :jediId")
    suspend fun getJediById(jediId: Int): JediEntity

    @Query("DELETE FROM jedi")
    suspend fun deleteAll()

    // TODO Use Flows to get the list of jedis and be notified of any changes to the jedis table
}
