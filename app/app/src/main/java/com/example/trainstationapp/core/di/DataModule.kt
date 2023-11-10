package com.example.trainstationapp.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.example.trainstationapp.BuildConfig
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.database.RemoteKeysDao
import com.example.trainstationapp.data.database.StationDao
import com.example.trainstationapp.data.database.converters.ListConverters
import com.example.trainstationapp.data.datastore.JwtSerializer
import com.example.trainstationapp.data.datastore.OAuthSerializer
import com.example.trainstationapp.data.datastore.Session
import com.example.trainstationapp.data.github.GithubApi
import com.example.trainstationapp.data.github.GithubLogin
import com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthAPIGrpcKt
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationAPIGrpcKt
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.grpc.Grpc
import io.grpc.TlsChannelCredentials
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asExecutor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideStationAPI(): StationAPIGrpcKt.StationAPICoroutineStub {
        return StationAPIGrpcKt.StationAPICoroutineStub(
            Grpc.newChannelBuilder("api.train.mnguyen.fr:443", TlsChannelCredentials.create())
                .executor(Dispatchers.IO.asExecutor())
                .build()
        )
    }

    @Provides
    @Singleton
    fun provideAuthAPI(): AuthAPIGrpcKt.AuthAPICoroutineStub {
        return AuthAPIGrpcKt.AuthAPICoroutineStub(
            Grpc.newChannelBuilder("api.train.mnguyen.fr:443", TlsChannelCredentials.create())
                .executor(Dispatchers.IO.asExecutor())
                .build()
        )
    }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideGithubApi(client: OkHttpClient, json: Json): GithubApi {
        return Retrofit.Builder()
            .baseUrl(GithubApi.BASE_URL)
            .addConverterFactory(json.asConverterFactory(GithubApi.CONTENT_TYPE.toMediaType()))
            .client(client)
            .build()
            .create(GithubApi::class.java)
    }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideGithubLogin(client: OkHttpClient, json: Json): GithubLogin {
        return Retrofit.Builder()
            .baseUrl(GithubLogin.BASE_URL)
            .addConverterFactory(json.asConverterFactory(GithubLogin.CONTENT_TYPE.toMediaType()))
            .client(client)
            .build()
            .create(GithubLogin::class.java)
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
    fun provideJson() = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

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
    fun provideJwtDataStore(@ApplicationContext context: Context): DataStore<Session.Jwt> {
        return DataStoreFactory.create(
            serializer = JwtSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.dataStoreFile("jwt.pb") }
        )
    }

    @Singleton
    @Provides
    fun provideOAuthDataStore(@ApplicationContext context: Context): DataStore<Session.OAuth> {
        return DataStoreFactory.create(
            serializer = OAuthSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.dataStoreFile("oauth.pb") }
        )
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
