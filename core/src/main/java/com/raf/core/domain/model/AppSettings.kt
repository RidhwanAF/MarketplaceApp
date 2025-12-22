package com.raf.core.domain.model

import android.os.Build

data class AppSettings(
    val darkTheme: DarkTheme = DarkTheme.FOLLOW_SYSTEM,
    val dynamicColor: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
)

enum class DarkTheme {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK
}