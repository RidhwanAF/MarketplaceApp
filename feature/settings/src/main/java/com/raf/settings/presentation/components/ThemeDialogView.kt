package com.raf.settings.presentation.components

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.raf.core.domain.model.DarkTheme
import com.raf.core.presentation.components.DialogWithTransition
import com.raf.settings.R
import com.raf.settings.presentation.utility.ThemeHelper.toLabel

@Composable
fun SharedTransitionScope.ThemeDialogView(
    modifier: Modifier = Modifier,
    visible: Boolean,
    currentTheme: DarkTheme,
    onDismiss: () -> Unit,
    onThemeChange: (theme: DarkTheme) -> Unit,
) {
    DialogWithTransition(
        visible = visible,
        onDismiss = onDismiss,
        icon = Icons.Default.DarkMode,
        title = stringResource(R.string.themes),
    ) {
        val context = LocalContext.current

        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            DarkTheme.entries.forEach { theme ->
                val icon = when (theme) {
                    DarkTheme.LIGHT -> Icons.Default.LightMode
                    DarkTheme.DARK -> Icons.Default.ModeNight
                    DarkTheme.FOLLOW_SYSTEM -> Icons.Default.DarkMode
                }

                ThemeItem(
                    icon = icon,
                    label = theme.toLabel(context),
                    isSelected = theme == currentTheme,
                    onClick = {
                        onThemeChange(theme)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun ThemeItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(0.05f) else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}