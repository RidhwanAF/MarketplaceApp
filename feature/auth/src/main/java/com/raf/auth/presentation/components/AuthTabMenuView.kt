package com.raf.auth.presentation.components

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.raf.auth.R
import kotlinx.coroutines.launch

@Composable
fun AuthTabMenuView(
    modifier: Modifier = Modifier,
    isLoginScreen: Boolean = false,
    onTabChange: (isLoginScreen: Boolean) -> Unit = { },
) {
    val scope = rememberCoroutineScope()
    val localHapticFeedback = LocalHapticFeedback.current
    val localDensity = LocalDensity.current
    var rowSize by remember {
        mutableStateOf(DpSize.Zero)
    }

    var maxXOffset by rememberSaveable {
        mutableStateOf(0f)
    }
    val currentContainerPos = remember {
        Animatable(if (isLoginScreen) 0f else maxXOffset)
    }
    var onDrag by remember(currentContainerPos.value) {
        mutableStateOf(
            currentContainerPos.value != 0f &&
                    currentContainerPos.value != maxXOffset
        )
    }
    LaunchedEffect(currentContainerPos.value) {
        onTabChange(currentContainerPos.value <= rowSize.width.value / 2)
    }
    LaunchedEffect(isLoginScreen) {
        localHapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
    }

    // Back Handler
    PredictiveBackHandler(!isLoginScreen || onDrag) { backEvent ->
        val initPos = currentContainerPos.value
        var newPos = initPos

        backEvent.collect { backProgress ->
            val pos = initPos - (backProgress.progress * rowSize.width.value)
            newPos = pos.coerceIn(0f, maxXOffset)
            currentContainerPos.snapTo(newPos)
        }

        val newTarget = if (newPos >= rowSize.width.value / 2) maxXOffset else 0f
        currentContainerPos.animateTo(newTarget)
    }

    Box(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            onDrag = true
                        },
                        onDragEnd = {
                            onDrag = false
                            scope.launch {
                                currentContainerPos.animateTo(
                                    if (currentContainerPos.value >= maxXOffset / 2) maxXOffset
                                    else 0f
                                )
                            }
                        }
                    ) { change, dragAmount ->
                        scope.launch {
                            val newPosition =
                                currentContainerPos.value + dragAmount + dragAmount * 0.5f
                            currentContainerPos.snapTo(newPosition)
                        }
                        change.consume()
                    }
                }
                .onGloballyPositioned {
                    with(localDensity) {
                        rowSize = DpSize(
                            width = it.size.width.toDp(),
                            height = it.size.height.toDp()
                        )
                        maxXOffset = (it.size.width / 2).toFloat()
                    }
                }
                .fillMaxWidth()
                .shadow(8.dp, MaterialTheme.shapes.extraLarge)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            AuthTabMenuItem(
                icon = Icons.AutoMirrored.Filled.Login,
                title = stringResource(R.string.login),
                isSelected = isLoginScreen,
                onTabClick = {
                    scope.launch {
                        currentContainerPos.animateTo(0f)
                    }
                },
                modifier = Modifier.weight(1f)
            )
            AuthTabMenuItem(
                icon = Icons.AutoMirrored.Filled.Input,
                title = stringResource(R.string.register),
                isSelected = !isLoginScreen,
                onTabClick = {
                    scope.launch {
                        currentContainerPos.animateTo(maxXOffset)
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = currentContainerPos.value.coerceIn(0f, maxXOffset)
                    scaleX = if (onDrag) 1.05f else 1f
                    scaleY = if (onDrag) 1.25f else 1f
                }
                .height(rowSize.height)
                .width(rowSize.width / 2)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.primary.copy(0.15f))
        )
    }
}

@Composable
private fun AuthTabMenuItem(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Filled.Login,
    title: String = stringResource(R.string.login),
    isSelected: Boolean = false,
    onTabClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
                onClick = onTabClick
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}