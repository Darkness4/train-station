package com.example.trainstationapp.core.di

import android.content.Context
import androidx.room.Room
import com.example.trainstationapp.BuildConfig
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.database.RemoteKeysDao
import com.example.trainstationapp.data.database.StationDao
import com.example.trainstationapp.data.database.converters.ListConverters
import com.example.trainstationapp.data.datasources.TrainStationDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideTrainStationDataSource(client: OkHttpClient, json: Json): TrainStationDataSource {
        return Retrofit.Builder()
            .baseUrl(TrainStationDataSource.BASE_URL)
            .addConverterFactory(
                json.asConverterFactory(TrainStationDataSource.CONTENT_TYPE.toMediaType())
            )
            .client(client)
            .build()
            .create(TrainStationDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return when {
            BuildConfig.DEBUG -> {
                val interceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                OkHttpClient.Builder().addInterceptor(interceptor).build()
            }
            else -> OkHttpClient()
        }
    }

    @Singleton
    @Provides
    fun provideJson() = Json { isLenient = true }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context, json: Json): Database {
        val listConverters = ListConverters.create(json)
        return Room.databaseBuilder(context, Database::class.java, "cache.db")
            .addTypeConverter(listConverters)
            .build()
    }

    @Singleton
    @Provides
    fun provideStationDao(database: Database): StationDao {
        return database.stationDao()
    }

    @Singleton
    @Provides
    fun provideRemoteKeysDao(database: Database): RemoteKeysDao {
        return database.remoteKeysDao()
    }
}
