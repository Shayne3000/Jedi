package com.senijoshua.jedi.data.repository

import com.senijoshua.jedi.data.local.JediCacheLimit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Denotation of the length of time that we would store Jedis in the DB before
 * either adding more items from the remote API or clearing it completely.
 */
class OfflineFirstJediCacheLimit @Inject constructor() : JediCacheLimit {
    override val dbRefreshCacheLimit: Long = TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES)

    // Ideally clearing cache timelines isn't something that should be determined locally but on the server.
    override val dbClearCacheLimit: Long = TimeUnit.MILLISECONDS.convert(4, TimeUnit.MINUTES)
}
