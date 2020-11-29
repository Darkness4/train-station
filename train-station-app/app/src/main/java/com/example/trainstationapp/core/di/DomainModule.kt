package com.example.trainstationapp.core.di

import com.example.trainstationapp.data.repositories.StationRepositoryImpl
import com.example.trainstationapp.domain.repositories.StationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {
    @Binds
    @Singleton
    fun bindStationRepository(repository: StationRepositoryImpl): StationRepository
}
