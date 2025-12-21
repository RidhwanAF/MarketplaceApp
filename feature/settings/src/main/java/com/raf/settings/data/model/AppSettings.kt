package com.raf.settings.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AppSettingsData(
    val darkTheme: DarkThemeData = DarkThemeData.FOLLOW_SYSTEM,
    val dynamicColor: Boolean = false,
)

@Serializable
enum class DarkThemeData {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK
}
