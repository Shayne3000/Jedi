package com.senijoshua.jedi.data.repository

import com.senijoshua.jedi.data.model.Jedi
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.data.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * A mock/fake implementation of [JediRepository] with hard coded data for
 * testing.
 */
class FakeJediRepository @Inject constructor(): JediRepository {

    var shouldThrowError = false
    var errorText = "Error!"

    override suspend fun getJediStream(): Flow<Result<List<Jedi>>> = flow {
        if (shouldThrowError) {
            emit(Result.Error(Throwable(errorText)))
        } else {
            emit(Result.Success(fakeJediList))
        }
    }

    override suspend fun getJediById(jediId: Int): Result<Jedi> = if (shouldThrowError) {
        Result.Error(Throwable(errorText))
    } else {
        Result.Success(fakeJediList[jediId])
    }
}
