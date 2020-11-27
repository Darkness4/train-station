package com.example.trainstationapp.core.di

import android.content.Context
import androidx.room.Room
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.database.RemoteKeysDao
import com.example.trainstationapp.data.database.StationDao
import com.example.trainstationapp.data.datasources.TrainStationDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideTrainStationDataSource(): TrainStationDataSource {
        return Retrofit.Builder()
            .baseUrl(TrainStationDataSource.BASE_URL)
            .addConverterFactory(
                Json { ignoreUnknownKeys = true; isLenient = true }.asConverterFactory(
                    TrainStationDataSource.CONTENT_TYPE.toMediaType()
                )
            )
            .build()
            .create(TrainStationDataSource::class.java)
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "cache.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideStationDao(database: Lazy<Database>): StationDao {
        return database.get().stationDao()
    }

    @Singleton
    @Provides
    fun provideRemoteKeysDao(database: Lazy<Database>): RemoteKeysDao {
        return database.get().remoteKeysDao()
    }
}
