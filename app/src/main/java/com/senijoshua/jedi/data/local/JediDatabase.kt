package com.senijoshua.jedi.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [JediEntity::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ]
)
abstract class JediDatabase : RoomDatabase() {
    abstract fun jediDao(): JediDao

    companion object {
        const val DATABASE_NAME = "jedi_db"
    }
}
