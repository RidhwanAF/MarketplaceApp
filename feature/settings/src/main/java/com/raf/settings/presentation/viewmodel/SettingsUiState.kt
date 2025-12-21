package com.raf.settings.presentation.viewmodel

import com.raf.core.domain.model.DarkTheme

data class SettingsUiState(
    val darkTheme: DarkTheme = DarkTheme.FOLLOW_SYSTEM,
    val dynamicColor: Boolean = false,
)
