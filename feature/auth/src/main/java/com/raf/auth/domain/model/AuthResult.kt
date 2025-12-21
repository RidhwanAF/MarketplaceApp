package com.raf.auth.domain.model

import androidx.annotation.StringRes

sealed class AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Error(@param:StringRes val message: Int) : AuthResult<Nothing>()
}