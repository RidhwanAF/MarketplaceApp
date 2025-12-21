package com.raf.marketplaceapp.viewmodel

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.core.domain.model.DarkTheme
import com.raf.core.domain.usecase.GetAppSettingsUseCase
import com.raf.core.domain.usecase.GetAuthTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    getAppSettingsUseCase: GetAppSettingsUseCase,
    getAuthTokenUseCase: GetAuthTokenUseCase,
) : ViewModel() {

    val appState: StateFlow<AppState> = combine(
        getAppSettingsUseCase(),
        getAuthTokenUseCase()
    ) { appSettings, token ->
        Log.d(TAG, "appState: Loaded")
        updateTheme(appSettings.darkTheme)

        AppState.Loaded(
            isLoggedIn = token != null,
            appSettings = appSettings,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AppState.Loading,
    )

    private fun updateTheme(darkTheme: DarkTheme) {
        val themeMode = when (darkTheme) {
            DarkTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            DarkTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            DarkTheme.FOLLOW_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    companion object {
        private const val TAG = "AppViewModel"
    }
}