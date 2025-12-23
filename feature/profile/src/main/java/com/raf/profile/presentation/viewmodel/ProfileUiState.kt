package com.raf.profile.presentation.viewmodel

import com.raf.profile.domain.model.Profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val logOuting: Boolean = false,
    val profile: Profile? = null,
    val uiMessage: String? = null
)
