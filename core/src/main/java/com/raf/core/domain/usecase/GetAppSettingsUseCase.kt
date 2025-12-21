package com.raf.core.domain.usecase

import com.raf.core.domain.contract.AppSettingsProvider

class GetAppSettingsUseCase(private val appSettingsProvider: AppSettingsProvider) {
    operator fun invoke() = appSettingsProvider.getAppSettings()
}