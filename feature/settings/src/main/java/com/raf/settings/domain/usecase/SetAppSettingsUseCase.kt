package com.raf.settings.domain.usecase

import com.raf.core.domain.model.AppSettings
import com.raf.settings.domain.repository.AppSettingsRepository

class SetAppSettingsUseCase(private val appSettingsRepository: AppSettingsRepository) {
    suspend operator fun invoke(appSettings: AppSettings) =
        appSettingsRepository.setAppSettings(appSettings)
}