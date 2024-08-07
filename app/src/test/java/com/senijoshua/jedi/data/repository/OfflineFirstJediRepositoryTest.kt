package com.senijoshua.jedi.data.repository

import com.senijoshua.jedi.data.local.FakeJediCacheLimit
import com.senijoshua.jedi.data.local.FakeJediDao
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.data.model.toLocal
import com.senijoshua.jedi.data.remote.FakeApi
import com.senijoshua.jedi.data.util.Result
import com.senijoshua.jedi.util.ERROR_TEXT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class OfflineFirstJediRepositoryTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private lateinit var jediDao: FakeJediDao

    private lateinit var jediApi: FakeApi

    private lateinit var cacheLimit: FakeJediCacheLimit

    private lateinit var repository: OfflineFirstJediRepository

    @Before
    fun setUp() {
        jediDao = FakeJediDao()
        jediApi = FakeApi()
        cacheLimit = FakeJediCacheLimit()
        repository = OfflineFirstJediRepository(jediApi, jediDao, testDispatcher, cacheLimit)
    }

    @Test
    fun `Given that the DB is empty, getJediStream should return jedi list on successful network request`() =
        testScope.runTest {
            assertTrue(jediDao.getAllJedi().first().isEmpty())

            val result = repository.getJediStream().drop(1).first()

            check(result is Result.Success)
            assertTrue(result.data.isNotEmpty())
        }

    @Test
    fun `Given that the DB is empty, getJediStream should return an error on network request failure`() =
        testScope.runTest {
            jediApi.shouldThrowError = true
            assertTrue(jediDao.getAllJedi().first().isEmpty())

            val result = repository.getJediStream().first()

            check(result is Result.Error)
            assertEquals(ERROR_TEXT, result.error.message)
        }

    @Test
    fun `Given that the DB is not empty and does not have stale data, getJediStream should return a jedi list from the DB`() =
        testScope.runTest {
            jediDao.insertAll(jediApi.dummyNetworkJedi.toLocal())

            val result = repository.getJediStream().first()

            check(result is Result.Success)
            assertEquals(fakeJediList.first().gender, result.data.first().gender)
            assertEquals(fakeJediList.last().hairColor, result.data.last().hairColor)
        }

    @Test
    fun `Given that the DB is not empty & has stale data, getJediStream loads a jedi list from the server on Successful request`() =
        testScope.runTest {
            jediDao.insertAll(jediApi.dummyNetworkJedi.toLocal())
            cacheLimit.hasStaleData = true

            val result = repository.getJediStream().drop(1).first()

            check(result is Result.Success)
            assertEquals(fakeJediList.first().gender, result.data.first().gender)
            assertTrue(result.data.size > fakeJediList.size)
        }

    @Test
    fun `Given that the DB is not empty & has stale data, getJediStream returns an error on network request failure`() =
        testScope.runTest {
            jediDao.insertAll(jediApi.dummyNetworkJedi.toLocal())
            cacheLimit.hasStaleData = true
            jediApi.shouldThrowError = true

            val result = repository.getJediStream().first()

            check(result is Result.Error)
            assertEquals(ERROR_TEXT, result.error.message)
        }

    @Test
    fun `Given that the DB is not empty, has stale data and can clear old data, getJediStream gets a newly-inserted jedi list from the DB via the server on success`() =
        testScope.runTest {
            // Populate the DB
            val dbEntities = jediApi.dummyNetworkJedi.toLocal()
            jediDao.insertAll(dbEntities + dbEntities)

            cacheLimit.hasStaleData = true
            cacheLimit.canCleanOldData = true

            // Assert that the DB has substantial items.
            val currentJediElements = jediDao.getAllJedi().first()
            assertTrue(currentJediElements.size > dbEntities.size)

            val result = repository.getJediStream().drop(1).first()

            check(result is Result.Success)
            // Assert that the DB's size has been trimmed down indicating that DB was cleared before insertion occurred.
            assertTrue(result.data.size < currentJediElements.size)
        }

    @Test
    fun `Given that the DB is not empty, its data is stale, and we can clear old data, getJediStream returns data from the DB on network failure`() =
        testScope.runTest {
            jediDao.insertAll(jediApi.dummyNetworkJedi.toLocal())
            cacheLimit.hasStaleData = true
            cacheLimit.canCleanOldData = true
            jediApi.shouldThrowError = true

            val result = repository.getJediStream().first()
            val currentJediElements = jediDao.getAllJedi().first()

            check(result is Result.Error)
            assertEquals(ERROR_TEXT, result.error.message)
            assertTrue(currentJediElements.isNotEmpty())
    }

    @Test
    fun `Given an id, getJediById returns the correct Jedi model on success`() = testScope.runTest {
        val jediId = 1
        jediDao.insertAll(jediApi.dummyNetworkJedi.toLocal())

        val result = repository.getJediById(jediId)

        check(result is Result.Success)
        assertEquals(fakeJediList[jediId].gender, result.data.gender)
    }

    @Test
    fun `Given an id that doesn't exist, getJediById returns an error`() = testScope.runTest {
        val jediId = 2

        val result = repository.getJediById(jediId)

        assertTrue(result is Result.Error)
    }

    @After
    fun tearDown() {
        jediDao.clear()
        jediApi.clear()
        cacheLimit.clear()
    }
}
