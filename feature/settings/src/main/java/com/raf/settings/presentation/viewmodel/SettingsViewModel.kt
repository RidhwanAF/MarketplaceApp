package com.raf.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.core.domain.model.AppSettings
import com.raf.core.domain.model.DarkTheme
import com.raf.core.domain.usecase.GetAppSettingsUseCase
import com.raf.settings.domain.usecase.SetAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val setAppSettingsUseCase: SetAppSettingsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAppSettings()
    }

    private fun getAppSettings() {
        viewModelScope.launch {
            getAppSettingsUseCase().collect { appSettings ->
                _uiState.update {
                    it.copy(
                        darkTheme = appSettings.darkTheme,
                        dynamicColor = appSettings.dynamicColor
                    )
                }
            }
        }
    }

    fun setAppDarkTheme(darkTheme: DarkTheme) {
        viewModelScope.launch {
            val newAppSettings = AppSettings(
                darkTheme = darkTheme,
                dynamicColor = _uiState.value.dynamicColor
            )
            setAppSettingsUseCase(newAppSettings)
        }
    }

    fun setDynamicColor(dynamicColor: Boolean) {
        viewModelScope.launch {
            val newAppSettings = AppSettings(
                darkTheme = _uiState.value.darkTheme,
                dynamicColor = dynamicColor
            )
            setAppSettingsUseCase(newAppSettings)
        }
    }
}