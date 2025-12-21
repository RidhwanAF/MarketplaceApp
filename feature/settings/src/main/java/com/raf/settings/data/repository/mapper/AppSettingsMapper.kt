package com.raf.settings.data.repository.mapper

import com.raf.core.domain.model.AppSettings
import com.raf.core.domain.model.DarkTheme
import com.raf.settings.data.model.AppSettingsData
import com.raf.settings.data.model.DarkThemeData

object AppSettingsMapper {

    fun AppSettingsData.toDomain() = AppSettings(
        darkTheme = darkTheme.toDomain(),
        dynamicColor = dynamicColor
    )

    private fun DarkThemeData.toDomain() = DarkTheme.valueOf(this.name)

    fun AppSettings.toData() = AppSettingsData(
        darkTheme = darkTheme.toData(),
        dynamicColor = dynamicColor
    )

    private fun DarkTheme.toData() = DarkThemeData.valueOf(this.name)
}