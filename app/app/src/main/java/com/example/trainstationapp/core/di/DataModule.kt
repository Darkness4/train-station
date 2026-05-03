package com.example.trainstationapp.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.connectrpc.ProtocolClientConfig
import com.connectrpc.extensions.GoogleJavaLiteProtobufStrategy
import com.connectrpc.impl.ProtocolClient
import com.connectrpc.okhttp.ConnectOkHttpClient
import com.example.trainstationapp.BuildConfig
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.database.RemoteKeysDao
import com.example.trainstationapp.data.database.StationDao
import com.example.trainstationapp.data.database.converters.ListConverters
import com.example.trainstationapp.data.datastore.CodeVerifierSerializer
import com.example.trainstationapp.data.datastore.OAuthSerializer
import com.example.trainstationapp.data.datastore.Session
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationAPIClient
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationAPIClientInterface
import com.example.trainstationapp.data.oidc.OidcClient
import com.example.trainstationapp.data.oidc.OidcDiscoveryDocument
import com.example.trainstationapp.data.oidc.OidcException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideOidcClient(
        httpClient: OkHttpClient,
        json: Json,
    ): OidcClient = runBlocking(Dispatchers.IO) {
        val request = Request.Builder()
            .url(BuildConfig.OIDC_DISCOVERY_URL)
            .get()
            .header("Accept", "application/json")
            .build()

        val response = httpClient.newCall(request).execute()
        val body = response.body.string()

        if (!response.isSuccessful) {
            throw OidcException("Discovery fetch failed HTTP ${response.code}")
        }

        val discovery = try {
            json.decodeFromString(OidcDiscoveryDocument.serializer(), body)
        } catch (e: Exception) {
            throw OidcException("Failed to parse discovery document", e)
        }

        OidcClient(httpClient, json, discovery)
    }

    @Provides
    @Singleton
    fun provideStationAPI(okHttpClient: OkHttpClient): StationAPIClientInterface = StationAPIClient(
        ProtocolClient(
            httpClient = ConnectOkHttpClient(okHttpClient),
            ProtocolClientConfig(
                host = BuildConfig.TRAIN_STATION_API_URL,
                serializationStrategy = GoogleJavaLiteProtobufStrategy(),
                ioCoroutineContext = Dispatchers.IO,
            ),
        ),
    )

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = when {
        BuildConfig.DEBUG -> {
            val interceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder().addInterceptor(interceptor).build()
        }

        else -> OkHttpClient()
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
    fun provideOAuthDataStore(@ApplicationContext context: Context): DataStore<Session.OAuth> = DataStoreFactory.create(
        serializer = OAuthSerializer,
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = { context.dataStoreFile("oauth.pb") },
    )

    @Singleton
    @Provides
    fun provideCodeVerifierDataStore(@ApplicationContext context: Context): DataStore<Session.CodeVerifier> = DataStoreFactory.create(
        serializer = CodeVerifierSerializer,
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = { context.dataStoreFile("code_verifier.pb") },
    )

    @Singleton
    @Provides
    fun provideStationDao(database: Database): StationDao = database.stationDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(database: Database): RemoteKeysDao = database.remoteKeysDao()
}
