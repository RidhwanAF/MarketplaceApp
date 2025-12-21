package com.raf.settings.data.di

import android.content.Context
import com.raf.settings.data.local.AppSettingsDataStore
import com.raf.settings.data.repository.AppSettingsRepositoryImpl
import com.raf.settings.domain.repository.AppSettingsRepository
import com.raf.settings.domain.usecase.SetAppSettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppSettingsModule {

    @Provides
    @Singleton
    fun provideAppSettingsDataStore(@ApplicationContext context: Context): AppSettingsDataStore {
        return AppSettingsDataStore(context)
    }

    @Provides
    @Singleton
    fun provideAppSettingsRepository(appSettingsDataStore: AppSettingsDataStore): AppSettingsRepository {
        return AppSettingsRepositoryImpl(appSettingsDataStore)
    }

    @Provides
    @Singleton
    fun provideSetAppSettingsUseCase(appSettingsRepository: AppSettingsRepository): SetAppSettingsUseCase {
        return SetAppSettingsUseCase(appSettingsRepository)
    }
}