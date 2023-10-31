package com.senijoshua.jedi.ui

import com.senijoshua.jedi.data.repository.FakeJediRepositoryImpl
import com.senijoshua.jedi.data.repository.JediRepository
import com.senijoshua.jedi.di.DataModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
object TestDataModule {

    @Singleton
    @Provides
    fun provideJediRepository(): JediRepository = FakeJediRepositoryImpl()
}
