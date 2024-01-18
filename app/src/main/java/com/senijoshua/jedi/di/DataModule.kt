package com.senijoshua.jedi.di

import com.senijoshua.jedi.data.repository.JediRepository
import com.senijoshua.jedi.data.repository.OfflineFirstJediRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindJediRepository(jediRepository: OfflineFirstJediRepository): JediRepository
}
