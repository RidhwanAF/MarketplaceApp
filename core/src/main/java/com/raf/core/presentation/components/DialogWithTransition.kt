package com.raf.core.presentation.components

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.raf.core.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SharedTransitionScope.DialogWithTransition(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: ImageVector? = null,
    title: String,
    onDismiss: () -> Unit,
    dismissText: String? = null,
    confirmText: String? = null,
    onConfirmation: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val scrollState = rememberScrollState()
    val hasScrollVertically = scrollState.maxValue > 0

    var backProgression by remember {
        mutableFloatStateOf(0f)
    }

    PredictiveBackHandler(visible) { backEvent ->
        backEvent.collect { eventCompat ->
            backProgression = eventCompat.progress
        }
        onDismiss()
    }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(500)
            backProgression = 0f
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.Black.copy(
                                alpha = (0.5f - backProgression).coerceIn(
                                    minimumValue = 0.1f,
                                    maximumValue = 0.5f
                                )
                            )
                        )
                )
                // Content
                AnimatedVisibility(
                    visible = visible,
                    enter = scaleIn(
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                    ),
                    exit = scaleOut(
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                    )
                ) {
                    val animatedVisibilityScope = this
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { onDismiss() }
                                )
                            }
                    ) {
                        ElevatedCard(
                            shape = MaterialTheme.shapes.extraLarge,
                            modifier = modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState("dialog_transition_${title}_container_key"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .graphicsLayer {
                                    val backProgressionScale =
                                        (1f - backProgression).coerceAtLeast(0.9f)
                                    scaleX = backProgressionScale
                                    scaleY = backProgressionScale
                                }
                                .padding(innerPadding)
                                .padding(16.dp)
                                .widthIn(
                                    min = 480.dp,
                                    max = 640.dp
                                )
                                .heightIn(max = 800.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = { /*NO-OP*/ }
                                    )
                                }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val contentModifier =
                                    if (hasScrollVertically) Modifier.weight(1f) else Modifier

                                if (icon != null) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = title,
                                        modifier = Modifier
                                            .sharedElement(
                                                sharedContentState = rememberSharedContentState("dialog_transition_${title}_icon_key"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                            .padding(horizontal = 16.dp)
                                            .padding(top = 16.dp, bottom = 8.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .sharedElement(
                                            sharedContentState = rememberSharedContentState("dialog_transition_${title}_title_key"),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        )
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .padding(bottom = 8.dp)
                                )
                                Column(
                                    modifier = contentModifier
                                        .fillMaxWidth()
                                        .heightIn(max = 640.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .verticalScroll(scrollState)
                                    ) {
                                        content()
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(
                                        8.dp,
                                        Alignment.End
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp, horizontal = 8.dp)
                                ) {
                                    TextButton(
                                        shapes = CustomButtonShapes(),
                                        onClick = onDismiss,
                                    ) {
                                        Text(
                                            text = dismissText ?: stringResource(R.string.close)
                                        )
                                    }
                                    if (onConfirmation != null) {
                                        TextButton(
                                            shapes = CustomButtonShapes(),
                                            onClick = onConfirmation,
                                        ) {
                                            Text(
                                                text = confirmText
                                                    ?: stringResource(R.string.confirm)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}