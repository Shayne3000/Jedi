package com.senijoshua.jedi.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface JediDao {
    @Upsert
    suspend fun insertAll(jediList: List<JediEntity>)

    @Query("SELECT * FROM jedi")
    fun getAllJedis(): Flow<List<JediEntity>>

    @Query("SELECT * FROM jedi WHERE id = :jediId")
    suspend fun getJediById(jediId: Int): JediEntity
}
