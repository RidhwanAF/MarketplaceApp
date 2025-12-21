package com.raf.marketplaceapp.viewmodel

import com.raf.core.domain.model.AppSettings

sealed class AppState {
    object Loading : AppState()
    data class Loaded(
        val isLoggedIn: Boolean,
        val appSettings: AppSettings,
    ) : AppState()
}
