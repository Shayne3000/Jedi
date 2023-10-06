package com.senijoshua.jedi.di

import android.content.Context
import androidx.room.Room
import com.senijoshua.jedi.data.local.db.JediDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideJediDatabase(@ApplicationContext context: Context): JediDatabase {
        return Room.databaseBuilder(context, JediDatabase::class.java, JediDatabase.DATABASE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideJediDao(jediDb: JediDatabase) = jediDb.jediDao()
}
