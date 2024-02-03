package com.senijoshua.jedi.data.repository

import com.senijoshua.jedi.data.local.FakeJediDao
import com.senijoshua.jedi.data.model.fakeJediList
import com.senijoshua.jedi.data.model.toLocal
import com.senijoshua.jedi.data.remote.FakeApi
import com.senijoshua.jedi.data.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
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

    private lateinit var repository: OfflineFirstJediRepository

    @Before
    fun setUp() {
        jediDao = FakeJediDao()
        jediApi = FakeApi()
        repository = OfflineFirstJediRepository(jediApi, jediDao, testDispatcher)
    }

    @Test
    fun `Given that the DB is empty, getJedis should return jedi list on successful network request`() =
        testScope.runTest {
            assertTrue(jediDao.getAllJedis().first().isEmpty())

            // NB: drop(1) is because, the "DB"'s Flow makes multiple emissions
            // but we are only interested in the second emission.
            // The first emission is when the DB is empty resulting in Result's data being empty
            // and the second one is after the network request returns and injects a list of jedis into the DB.
            val result = repository.getJedisStream().drop(1).first()

            check(result is Result.Success)
            assertTrue(result.data.isNotEmpty())
        }

    @Test
    fun `Given that the DB is empty, getJedis should return an error on network request failure`() =
        testScope.runTest {
            jediApi.shouldThrowError = true

            val result = repository.getJedisStream().first()

            check(result is Result.Error)
            assertEquals("error", result.error.message)
        }

    @Test
    fun `Given that the DB is not empty, getJedis should return a jedi list from the DB`() =
        testScope.runTest {
            jediDao.insertAll(jediApi.dummyNetworkJedi.toLocal())

            val result = repository.getJedisStream().first()

            check(result is Result.Success)
            assertEquals(fakeJediList.first().gender, result.data.first().gender)
            assertEquals(fakeJediList.last().hairColor, result.data.last().hairColor)
        }

    @Test
    fun `Given an id, getJediById returns the correct Jedi model on success`() = testScope.runTest {
        val jediId = 1
        jediDao.insertAll(jediApi.dummyNetworkJedi.toLocal())

        val result = repository.getJediById(jediId)

        check(result is Result.Success)
        assertEquals("Jedi 1 gender", result.data.gender)
    }

    @Test
    fun `Given an id that doesn't exist, getJediById returns an error`() = testScope.runTest {
        val jediId = 2

        val result = repository.getJediById(jediId)

        assertTrue(result is Result.Error)
    }
}
