package com.raf.settings.data.repository

import android.util.Log
import com.raf.core.domain.contract.AppSettingsProvider
import com.raf.core.domain.model.AppSettings
import com.raf.settings.data.local.AppSettingsDataStore
import com.raf.settings.data.model.AppSettingsData
import com.raf.settings.data.repository.mapper.AppSettingsMapper.toData
import com.raf.settings.data.repository.mapper.AppSettingsMapper.toDomain
import com.raf.settings.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    private val appSettingsDataStore: AppSettingsDataStore,
) : AppSettingsProvider, AppSettingsRepository {

    private companion object {
        private const val TAG = "AppSettingsRepository"
    }

    private val json = Json { ignoreUnknownKeys = true }

    override fun getAppSettings(): Flow<AppSettings> {
        return appSettingsDataStore.getSettings().map { encodedSettings ->
            if (encodedSettings == null) return@map AppSettings()
            json.decodeFromString<AppSettingsData>(encodedSettings).toDomain()
        }
    }

    override suspend fun setAppSettings(appSettings: AppSettings) {
        try {
            val mappedAppSettings = appSettings.toData()
            val encodedSettings = json.encodeToString(mappedAppSettings)
            appSettingsDataStore.setSettings(encodedSettings)
        } catch (e: Exception) {
            Log.e(TAG, "setAppSettings Failed", e)
        }
    }
}