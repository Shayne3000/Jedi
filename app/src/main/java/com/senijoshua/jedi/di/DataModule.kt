package com.senijoshua.jedi.di

import com.senijoshua.jedi.data.repository.JediRepository
import com.senijoshua.jedi.data.repository.OfflineFirstJediRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {

    @ViewModelScoped
    @Binds
    abstract fun bindJediRepository(jediRepository: OfflineFirstJediRepository): JediRepository
}
