package com.raf.settings.domain.repository

import com.raf.core.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    suspend fun setAppSettings(appSettings: AppSettings)
    fun getAppSettings(): Flow<AppSettings>
}