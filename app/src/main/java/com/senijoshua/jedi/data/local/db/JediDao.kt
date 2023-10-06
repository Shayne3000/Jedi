package com.senijoshua.jedi.data.local.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface JediDao {
    @Upsert
    suspend fun insertAll(jediList: List<JediEntity>)

    @Upsert
    suspend fun insertOrUpdateJedi(jedi: JediEntity)

    @Query("SELECT * FROM jedis")
    suspend fun getAllJedis(): List<JediEntity>

    @Query("SELECT * FROM jedis WHERE id = :jediId")
    suspend fun getJediById(jediId: Int): JediEntity

    @Query("DELETE FROM jedis")
    suspend fun deleteAll()

    // TODO Use Flows to get the list of jedis and be notified of any changes to the jedis table
}
