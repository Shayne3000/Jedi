package com.senijoshua.jedi.data.repository

import com.senijoshua.jedi.ui.model.Jedi
import com.senijoshua.jedi.ui.model.fakeJediList
import com.senijoshua.jedi.data.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * A mock/fake implementation of [JediRepository] with hard coded data for
 * testing.
 */
class FakeJediRepositoryImpl : JediRepository {

    var shouldThrowError = false

    override suspend fun getJedis(): Flow<Result<List<Jedi>>> = flow {
        if (shouldThrowError) {
            emit(Result.Error(Throwable("error")))
        } else {
            emit(Result.Success(fakeJediList))
        }
    }

    override suspend fun getJediById(jediId: Int): Result<Jedi> = if (shouldThrowError) {
        Result.Error(Throwable("error"))
    } else {
        Result.Success(fakeJediList[jediId])
    }
}
