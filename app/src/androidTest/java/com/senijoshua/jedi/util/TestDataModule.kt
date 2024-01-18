package com.senijoshua.jedi.util

import com.senijoshua.jedi.data.repository.FakeJediRepository
import com.senijoshua.jedi.data.repository.JediRepository
import com.senijoshua.jedi.di.DataModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
abstract class TestDataModule {

    @Singleton
    @Binds
    abstract fun provideJediRepository(repository: FakeJediRepository): JediRepository
}
