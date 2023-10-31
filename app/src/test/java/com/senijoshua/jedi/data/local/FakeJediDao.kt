package com.senijoshua.jedi.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A mock/fake "working" implementation of [JediDao] with hard coded data for
 * testing.
 */
class FakeJediDao : JediDao {
    // In-memory "database" against which we would execute DB operations.
    private var jediEntities = MutableStateFlow(
        emptyList<JediEntity>()
    )

    override suspend fun insertAll(jediList: List<JediEntity>) {
        jediEntities.value = jediList
    }

    override fun getAllJedis(): Flow<List<JediEntity>> = jediEntities

    override suspend fun getJediById(jediId: Int): JediEntity {
        return jediEntities.value[jediId]
    }
}
