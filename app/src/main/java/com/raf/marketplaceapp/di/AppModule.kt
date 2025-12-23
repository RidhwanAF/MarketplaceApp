package com.raf.marketplaceapp.di

import android.content.Context
import com.raf.auth.data.local.AuthDataStore
import com.raf.auth.data.remote.AuthApiService
import com.raf.auth.data.repository.AuthRepositoryImpl
import com.raf.core.domain.contract.AppSettingsProvider
import com.raf.core.domain.contract.AuthProvider
import com.raf.core.domain.contract.CartProvider
import com.raf.core.domain.contract.ProfileProvider
import com.raf.core.domain.usecase.DeleteAllItemCartUseCase
import com.raf.core.domain.usecase.GetAppSettingsUseCase
import com.raf.core.domain.usecase.GetAuthTokenUseCase
import com.raf.core.domain.usecase.GetUserIdUseCase
import com.raf.core.domain.usecase.GetUserProfileUseCase
import com.raf.core.domain.usecase.LogoutUseCase
import com.raf.marketplace.data.local.room.MarketplaceDatabase
import com.raf.marketplace.data.remote.MarketplaceApiService
import com.raf.marketplace.data.repository.MarketplaceRepositoryImpl
import com.raf.marketplaceapp.BuildConfig
import com.raf.profile.data.local.room.ProfileDatabase
import com.raf.profile.data.remote.ProfileApiService
import com.raf.profile.data.repository.ProfileRepositoryImpl
import com.raf.settings.data.local.AppSettingsDataStore
import com.raf.settings.data.repository.AppSettingsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Remote
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Contracts
     */
    @Provides
    @Singleton
    fun provideAppSettingsProvider(appSettingsDataStore: AppSettingsDataStore): AppSettingsProvider {
        return AppSettingsRepositoryImpl(appSettingsDataStore)
    }

    @Provides
    @Singleton
    fun provideAuthProvider(
        authApiService: AuthApiService,
        authDataStore: AuthDataStore,
    ): AuthProvider {
        return AuthRepositoryImpl(authApiService, authDataStore)
    }

    @Provides
    @Singleton
    fun provideCartProvider(
        @ApplicationContext context: Context,
        apiService: MarketplaceApiService,
        marketplaceDb: MarketplaceDatabase,
    ): CartProvider {
        return MarketplaceRepositoryImpl(context, apiService, marketplaceDb)
    }

    @Provides
    @Singleton
    fun provideProfileProvider(
        @ApplicationContext context: Context,
        profileDatabase: ProfileDatabase,
        profileApiService: ProfileApiService,
    ): ProfileProvider {
        return ProfileRepositoryImpl(context, profileDatabase, profileApiService)
    }

    /**
     * Use Cases
     */
    @Provides
    @Singleton
    fun provideGetAppSettingsUseCase(appSettingsProvider: AppSettingsProvider): GetAppSettingsUseCase {
        return GetAppSettingsUseCase(appSettingsProvider)
    }

    @Provides
    @Singleton
    fun provideGetAuthTokenUseCase(authProvider: AuthProvider): GetAuthTokenUseCase {
        return GetAuthTokenUseCase(authProvider)
    }

    @Provides
    @Singleton
    fun provideGetUserProfileUseCase(provider: ProfileProvider): GetUserProfileUseCase {
        return GetUserProfileUseCase(provider)
    }

    @Provides
    @Singleton
    fun provideGetUserIdUseCase(authProvider: AuthProvider): GetUserIdUseCase {
        return GetUserIdUseCase(authProvider)
    }

    @Provides
    @Singleton
    fun provideDeleteAllItemCartUseCase(cartProvider: CartProvider): DeleteAllItemCartUseCase {
        return DeleteAllItemCartUseCase(cartProvider)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(authProvider: AuthProvider): LogoutUseCase {
        return LogoutUseCase(authProvider)
    }
}