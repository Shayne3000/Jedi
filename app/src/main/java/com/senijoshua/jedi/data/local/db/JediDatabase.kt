package com.senijoshua.jedi.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [JediEntity::class], version = 1, exportSchema = false)
abstract class JediDatabase : RoomDatabase() {
    abstract fun jediDao(): JediDao

    companion object {
        const val DATABASE_NAME = "jedi_db"
    }
}
