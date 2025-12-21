package com.raf.auth.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.auth.R
import com.raf.auth.domain.model.AuthResult
import com.raf.auth.domain.usecase.LoginUseCase
import com.raf.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    var username by mutableStateOf("")
        private set

    var usernameError by mutableStateOf(false)
        private set

    var password by mutableStateOf("")
        private set

    var showPassword by mutableStateOf(false)
        private set

    var passwordError by mutableStateOf(false)
        private set

    var passwordConfirmation by mutableStateOf("")
        private set

    var showPasswordConfirmation by mutableStateOf(false)
        private set

    var passwordConfirmationError by mutableStateOf(false)
        private set

    fun onUsernameChange(newUsername: String) {
        username = newUsername
        usernameError = username.isBlank()
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        passwordError = password.isBlank() || password.length < 8
    }

    fun onPasswordConfirmationChange(newPasswordConfirmation: String) {
        passwordConfirmation = newPasswordConfirmation
        passwordConfirmationError =
            passwordConfirmation.isBlank() || passwordConfirmation != password
    }

    fun toggleShowPassword() {
        showPassword = !showPassword
    }

    fun toggleShowPasswordConfirmation() {
        showPasswordConfirmation = !showPasswordConfirmation
    }

    fun toggleLoginState(value: Boolean? = null) {
        _uiState.update {
            it.copy(isLoginState = value ?: !it.isLoginState)
        }
        usernameError = false
        passwordError = false
        passwordConfirmationError = false
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = loginUseCase(username, password)) {
                is AuthResult.Success -> {
                    _uiState.update {
                        it.copy(isLoginSuccess = result.data)
                    }
                }

                is AuthResult.Error -> {
                    showUiMessage(result.message)
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun register() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = registerUseCase.invoke(username, passwordConfirmation)) {
                is AuthResult.Success -> {
                    showUiMessage(R.string.register_success_please_login)
                    toggleLoginState()
                    username = ""
                    password = ""
                    passwordConfirmation = ""
                }

                is AuthResult.Error -> {
                    showUiMessage(result.message)
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    var messageJob: Job? = null
    fun showUiMessage(@StringRes message: Int) {
        messageJob?.cancel()
        messageJob = viewModelScope.launch {
            _uiState.update { it.copy(uiMessageResId = message) }
            delay(2000)
            _uiState.update { it.copy(uiMessageResId = null) }
        }
    }
}