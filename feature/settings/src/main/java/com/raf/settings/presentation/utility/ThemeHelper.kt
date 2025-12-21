package com.raf.settings.presentation.utility

import android.content.Context
import com.raf.core.domain.model.DarkTheme
import com.raf.settings.R

object ThemeHelper {
    fun DarkTheme.toLabel(context: Context): String {
        return when (this) {
            DarkTheme.LIGHT -> context.getString(R.string.light)
            DarkTheme.DARK -> context.getString(R.string.dark)
            DarkTheme.FOLLOW_SYSTEM -> context.getString(R.string.follow_system)
        }
    }
}