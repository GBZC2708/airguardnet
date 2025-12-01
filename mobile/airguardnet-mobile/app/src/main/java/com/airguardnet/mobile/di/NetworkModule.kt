package com.airguardnet.mobile.di

import android.content.Context
import androidx.room.Room
import com.airguardnet.mobile.core.network.ApiConstants
import com.airguardnet.mobile.data.local.AirGuardNetDatabase
import com.airguardnet.mobile.data.local.dao.UserSessionDao
import com.airguardnet.mobile.data.remote.AirGuardNetApiService
import com.airguardnet.mobile.data.repository.impl.AuthRepositoryImpl
import com.airguardnet.mobile.data.repository.impl.ConfigRepositoryImpl
import com.airguardnet.mobile.data.repository.impl.DeviceRepositoryImpl
import com.airguardnet.mobile.data.repository.impl.HotspotRepositoryImpl
import com.airguardnet.mobile.domain.repository.AuthRepository
import com.airguardnet.mobile.domain.repository.ConfigRepository
import com.airguardnet.mobile.domain.repository.DeviceRepository
import com.airguardnet.mobile.domain.repository.HotspotRepository
import com.airguardnet.mobile.domain.usecase.AddHotspotUseCase
import com.airguardnet.mobile.domain.usecase.ClearHotspotsUseCase
import com.airguardnet.mobile.domain.usecase.LoginUseCase
import com.airguardnet.mobile.domain.usecase.LogoutUseCase
import com.airguardnet.mobile.domain.usecase.ObserveAlertsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import com.airguardnet.mobile.domain.usecase.ObserveHotspotsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveReadingsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSensorConfigsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import com.airguardnet.mobile.domain.usecase.RefreshAlertsUseCase
import com.airguardnet.mobile.domain.usecase.RefreshDevicesUseCase
import com.airguardnet.mobile.domain.usecase.RefreshReadingsUseCase
import com.airguardnet.mobile.domain.usecase.RefreshSensorConfigsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): AirGuardNetDatabase = Room.databaseBuilder(
        app,
        AirGuardNetDatabase::class.java,
        "airguardnet.db"
    ).build()

    @Provides
    @Singleton
    fun provideApiService(userSessionDao: UserSessionDao): AirGuardNetApiService {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val authInterceptor = Interceptor { chain ->
            val shouldSkipAuth = chain.request().url.encodedPathSegments.contains("login")
            val token = runBlocking { userSessionDao.getSession()?.jwtToken }
            val requestBuilder = chain.request().newBuilder()
            if (!shouldSkipAuth && !token.isNullOrBlank()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            val response = chain.proceed(requestBuilder.build())
            if (response.code == 401) {
                runBlocking { userSessionDao.clear() }
            }
            response
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
            .create(AirGuardNetApiService::class.java)
    }

    @Provides
    fun provideUserSessionDao(db: AirGuardNetDatabase) = db.userSessionDao()

    @Provides
    fun provideDeviceDao(db: AirGuardNetDatabase) = db.deviceDao()

    @Provides
    fun provideReadingDao(db: AirGuardNetDatabase) = db.readingDao()

    @Provides
    fun provideAlertDao(db: AirGuardNetDatabase) = db.alertDao()

    @Provides
    fun provideHotspotDao(db: AirGuardNetDatabase) = db.hotspotDao()

    @Provides
    @Singleton
    fun provideAuthRepository(api: AirGuardNetApiService, userSessionDao: UserSessionDao): AuthRepository =
        AuthRepositoryImpl(api, userSessionDao)

    @Provides
    @Singleton
    fun provideDeviceRepository(
        api: AirGuardNetApiService,
        db: AirGuardNetDatabase
    ): DeviceRepository = DeviceRepositoryImpl(api, db.deviceDao(), db.readingDao(), db.alertDao())

    @Provides
    @Singleton
    fun provideHotspotRepository(db: AirGuardNetDatabase): HotspotRepository = HotspotRepositoryImpl(db.hotspotDao())

    @Provides
    @Singleton
    fun provideConfigRepository(api: AirGuardNetApiService): ConfigRepository = ConfigRepositoryImpl(api)

    @Provides
    fun provideLoginUseCase(repo: AuthRepository) = LoginUseCase(repo)

    @Provides
    fun provideObserveSessionUseCase(repo: AuthRepository) = ObserveSessionUseCase(repo)

    @Provides
    fun provideLogoutUseCase(repo: AuthRepository) = LogoutUseCase(repo)

    @Provides
    fun provideObserveDevicesUseCase(repo: DeviceRepository) = ObserveDevicesUseCase(repo)

    @Provides
    fun provideObserveReadingsUseCase(repo: DeviceRepository) = ObserveReadingsUseCase(repo)

    @Provides
    fun provideObserveAlertsUseCase(repo: DeviceRepository) = ObserveAlertsUseCase(repo)

    @Provides
    fun provideRefreshDevicesUseCase(repo: DeviceRepository) = RefreshDevicesUseCase(repo)

    @Provides
    fun provideRefreshReadingsUseCase(repo: DeviceRepository) = RefreshReadingsUseCase(repo)

    @Provides
    fun provideRefreshAlertsUseCase(repo: DeviceRepository) = RefreshAlertsUseCase(repo)

    @Provides
    fun provideObserveHotspotsUseCase(repo: HotspotRepository) = ObserveHotspotsUseCase(repo)

    @Provides
    fun provideAddHotspotUseCase(repo: HotspotRepository) = AddHotspotUseCase(repo)

    @Provides
    fun provideClearHotspotsUseCase(repo: HotspotRepository) = ClearHotspotsUseCase(repo)

    @Provides
    fun provideObserveSensorConfigsUseCase(repo: ConfigRepository) = ObserveSensorConfigsUseCase(repo)

    @Provides
    fun provideRefreshSensorConfigsUseCase(repo: ConfigRepository) = RefreshSensorConfigsUseCase(repo)
}
