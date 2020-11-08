package com.example.trainstationapp.core.di

import com.example.trainstationapp.domain.repositories.MockStationRepository
import com.example.trainstationapp.domain.repositories.StationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface DomainModule {
    @Binds
    @Singleton
    fun bindStationRepository(repository: MockStationRepository): StationRepository
}