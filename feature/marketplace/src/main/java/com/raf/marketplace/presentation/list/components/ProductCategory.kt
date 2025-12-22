package com.raf.marketplace.presentation.list.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductCategory(
    modifier: Modifier = Modifier,
    categories: List<String>,
    selectedCategories: List<String> = emptyList(),
    onCLicked: (String) -> Unit = {},
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        ButtonGroup(
            overflowIndicator = { },
            expandedRatio = 0f,
        ) {
            categories.forEach { category ->
                val label =
                    category.split(" ")
                        .joinToString(" ") { value -> value.replaceFirstChar { it.uppercase() } }

                toggleableItem(
                    checked = category in selectedCategories,
                    onCheckedChange = {
                        onCLicked(category)
                        if (it) {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
                        } else {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                        }
                    },
                    label = label,
                    icon = {
                        if (category in selectedCategories) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = category
                            )
                        }
                    },
                )
            }
        }
    }
}