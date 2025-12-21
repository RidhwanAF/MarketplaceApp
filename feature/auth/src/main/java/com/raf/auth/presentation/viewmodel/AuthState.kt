package com.raf.auth.presentation.viewmodel

import androidx.annotation.StringRes

data class AuthState(
    val isLoading: Boolean = false,
    val isLoginState: Boolean = true,
    val isLoginSuccess: String? = null,
    @param:StringRes val uiMessageResId: Int? = null,
)
