package com.senijoshua.jedi.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A mock/fake "working" implementation of [JediDao] with hard coded data for
 * testing.
 */
class FakeJediDao : JediDao {

    // In-memory "database" against which we execute DB operations.
    private var jediEntities = MutableStateFlow(
        emptyList<JediEntity>()
    )

    override suspend fun insertAll(jediList: List<JediEntity>) {
        val currentList = jediEntities.value
        val newList = currentList.plus(jediList)
        jediEntities.value = newList
    }

    override fun getAllJedis(): Flow<List<JediEntity>> = jediEntities


    override suspend fun getJediById(jediId: Int): JediEntity {
        return jediEntities.value[jediId]
    }

    override suspend fun clear() {
        jediEntities.value = emptyList()
    }
}
