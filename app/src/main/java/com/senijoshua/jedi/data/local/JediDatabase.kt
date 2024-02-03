package com.senijoshua.jedi.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [JediEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class JediDatabase : RoomDatabase() {
    abstract fun jediDao(): JediDao

    companion object {
        const val DATABASE_NAME = "jedi_db"
    }
}
