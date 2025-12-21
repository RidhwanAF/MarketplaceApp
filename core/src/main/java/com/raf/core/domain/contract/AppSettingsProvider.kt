package com.raf.core.domain.contract

import com.raf.core.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsProvider {
    fun getAppSettings(): Flow<AppSettings>
}