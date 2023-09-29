package com.senijoshua.jedi.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    // Install in the singleton component because we want only one instance of Retrofit in the app.
}
