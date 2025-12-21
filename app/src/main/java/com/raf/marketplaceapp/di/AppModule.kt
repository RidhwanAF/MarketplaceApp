package com.raf.marketplaceapp.di

import com.raf.auth.data.local.AuthDataStore
import com.raf.auth.data.local.db.AuthDatabase
import com.raf.auth.data.repository.AuthRepositoryImpl
import com.raf.core.domain.contract.AppSettingsProvider
import com.raf.core.domain.contract.AuthProvider
import com.raf.core.domain.usecase.GetAppSettingsUseCase
import com.raf.core.domain.usecase.GetAuthTokenUseCase
import com.raf.core.domain.usecase.LogoutUseCase
import com.raf.settings.data.local.AppSettingsDataStore
import com.raf.settings.data.repository.AppSettingsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
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
        authDatabase: AuthDatabase,
        authDataStore: AuthDataStore,
    ): AuthProvider {
        return AuthRepositoryImpl(authDatabase, authDataStore)
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
    fun provideLogoutUseCase(authProvider: AuthProvider): LogoutUseCase {
        return LogoutUseCase(authProvider)
    }
}