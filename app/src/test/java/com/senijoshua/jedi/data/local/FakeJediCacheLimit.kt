package com.senijoshua.jedi.data.local

class FakeJediCacheLimit : JediCacheLimit {
    var hasStaleData = false
    var canCleanOldData = false
    
    override val dbRefreshCacheLimit: Long
        get() = refreshCacheLimit()
    override val dbClearCacheLimit: Long
        get() = clearCacheLimit()
    
    private fun refreshCacheLimit(): Long {
        return invalidateCache(hasStaleData)
    }

    private fun clearCacheLimit(): Long {
        return invalidateCache(canCleanOldData)
    }

    private fun invalidateCache(shouldInvalidate: Boolean): Long {
        return if (shouldInvalidate) {
            -1
        } else {
            System.currentTimeMillis()
        }
    }
}
