package com.senijoshua.jedi.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface JediDao {
    @Upsert
    suspend fun insertAll(jediList: List<JediEntity>)

    @Query("SELECT * FROM jedi ORDER BY time_created DESC")
    fun getAllJedi(): Flow<List<JediEntity>>

    @Query("SELECT * FROM jedi WHERE id = :jediId")
    suspend fun getJediById(jediId: Int): JediEntity

    @Query("DELETE FROM jedi")
    fun clear()

    @Query("SELECT time_created FROM jedi ORDER BY time_created DESC LIMIT 1")
    suspend fun getTimeCreated(): Long?
}
