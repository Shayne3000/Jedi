package com.senijoshua.jedi.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Class that encapsulates the result of a data layer operation and
 * communicates the type of said result to the presentation layer.
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: Throwable) : Result<Nothing>()
}

/**
 * Flow Intermediary operator extension function to parameterize the data
 * type from a Flow in a Result type and in turn parameterize said Result type within a Flow.
 *
 * Essentially, it converts Flow<T> to Flow<Result<T>>.
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> = this.map<T, Result<T>> {
    Result.Success(it)
}.catch {
    emit(Result.Error(it))
}
