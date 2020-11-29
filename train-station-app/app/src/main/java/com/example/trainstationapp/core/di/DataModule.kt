package com.example.trainstationapp.core.di

import android.content.Context
import androidx.room.Room
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.database.RemoteKeysDao
import com.example.trainstationapp.data.database.StationDao
import com.example.trainstationapp.data.datasources.TrainStationDataSource
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideTrainStationDataSource(client: OkHttpClient): TrainStationDataSource {
        return Retrofit.Builder()
            .baseUrl(TrainStationDataSource.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .client(client)
            .build()
            .create(TrainStationDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        // val interceptor = HttpLoggingInterceptor()
        //     .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            // .addInterceptor(interceptor)
            .build()
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
