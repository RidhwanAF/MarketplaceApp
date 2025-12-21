package com.raf.settings.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SharedTransitionScope.SettingsItemActionWithTransition(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    actionIcon: ImageVector? = null,
    showNavigationIndicator: Boolean = false,
    onClick: () -> Unit,
) {
    val localMotionScheme = MaterialTheme.LocalMotionScheme.current
    AnimatedContent(
        targetState = visible,
        transitionSpec = {
            scaleIn(
                animationSpec = localMotionScheme.fastSpatialSpec()
            ) togetherWith scaleOut(
                animationSpec = localMotionScheme.fastSpatialSpec()
            )
        }
    ) { targetState ->
        if (targetState) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            "dialog_transition_${title}_container_key"
                        ),
                        animatedVisibilityScope = this@AnimatedContent
                    )
                    .fillMaxWidth()
                    .clickable { onClick() }
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(
                                "dialog_transition_${title}_icon_key"
                            ),
                            animatedVisibilityScope = this@AnimatedContent,
                        )
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(
                                    "dialog_transition_${title}_title_key"
                                ),
                                animatedVisibilityScope = this@AnimatedContent,
                                renderInOverlayDuringTransition = false
                            )
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (showNavigationIndicator) {
                    Icon(
                        imageVector = actionIcon ?: Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = title,
                    )
                }
            }
        }
    }
}