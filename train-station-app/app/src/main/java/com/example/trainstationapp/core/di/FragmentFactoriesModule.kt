package com.example.trainstationapp.core.di

import com.example.trainstationapp.domain.repositories.StationRepository
import com.example.trainstationapp.presentation.ui.fragments.StationListFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object FragmentFactoriesModule {
    @Provides
    fun provideStationListFragmentFactory(stationRepository: StationRepository): StationListFragment.Factory {
        return StationListFragment.Factory(stationRepository)
    }
}