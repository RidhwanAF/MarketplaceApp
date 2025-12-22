package com.raf.marketplace.presentation.detail.components

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.raf.core.presentation.components.customIconButtonShapes
import com.raf.core.presentation.components.shimmerLoading
import com.raf.marketplace.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SharedTransitionScope.ProductImageViewer(
    modifier: Modifier = Modifier,
    visible: Boolean,
    productId: Int,
    title: String,
    image: String,
    onCLose: () -> Unit,
) {
    val localWindowInfo = LocalWindowInfo.current
    val windowHeight = localWindowInfo.containerDpSize.height
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    // Image Drag Handler
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val zoom = remember { Animatable(1f) }
    val angle = remember { Animatable(0f) }
    val offsetToCloseProgress =
        if (zoom.value > 1f) 0f else abs(offset.value.y) / windowHeight.value

    // Back Handler
    var backProgress by remember {
        mutableFloatStateOf(0f)
    }
    PredictiveBackHandler(visible) { backEventCompat ->
        backEventCompat.collect { backEvent ->
            backProgress = backEvent.progress
        }
        onCLose()
    }
    LaunchedEffect(visible) {
        if (!visible) {
            delay(300)
            backProgress = 0f
            offset.snapTo(Offset.Zero)
            zoom.snapTo(1f)
            angle.snapTo(0f)
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                shadow = Shadow(
                                    color = Color.Black,
                                    blurRadius = 4f
                                )
                            ),
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    navigationIcon = {
                        TooltipBox(
                            positionProvider =
                                TooltipDefaults.rememberTooltipPositionProvider(
                                    TooltipAnchorPosition.Below
                                ),
                            tooltip = {
                                PlainTooltip {
                                    Text(text = stringResource(R.string.close))
                                }
                            },
                            state = rememberTooltipState()
                        ) {
                            IconButton(
                                shapes = customIconButtonShapes(),
                                onClick = onCLose
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.close)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = Color.White,
                    ),
                    scrollBehavior = scrollBehavior,
                )
            },
            containerColor = Color.Black.copy(
                alpha = (1f - backProgress - offsetToCloseProgress)
                    .coerceAtLeast(0.25f)
            ),
            contentColor = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) { innerPadding ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                SubcomposeAsyncImage(
                    model = image,
                    contentDescription = title,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(64.dp)
                                .shimmerLoading()
                        )
                    },
                    error = {
                        Icon(
                            imageVector = Icons.Default.BrokenImage,
                            contentDescription = title,
                            modifier = Modifier.size(64.dp)
                        )
                    },
                    modifier = Modifier
                        .graphicsLayer { // Back Handler
                            val backScale = (1f - backProgress).coerceAtLeast(0.5f)
                            scaleX = backScale
                            scaleY = backScale
                            translationY = backProgress * -500f
                        }
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown()
                                do {
                                    val event = awaitPointerEvent()

                                    scope.launch {
                                        val newZoom =
                                            (zoom.value * event.calculateZoom())
                                                .coerceIn(0.5f, 2f)
                                        zoom.snapTo(newZoom)
                                        angle.snapTo(angle.value + event.calculateRotation())
                                        offset.snapTo(offset.value + event.calculatePan() / zoom.value)
                                    }
                                } while (event.changes.any { it.pressed })

                                scope.launch {
                                    launch {
                                        val isZooming = zoom.value > 1f
                                        val closeOffsetProgress =
                                            abs(offset.value.y) / windowHeight.value
                                        if (!isZooming && closeOffsetProgress in -0.99..0.99) {
                                            offset.animateTo(
                                                Offset.Zero,
                                                spring()
                                            )
                                        } else if (!isZooming && closeOffsetProgress >= 1f) {
                                            onCLose()
                                        }
                                    }
                                    launch {
                                        if (zoom.value <= 1f) {
                                            zoom.animateTo(1f, spring())
                                        }
                                    }
                                    launch {
                                        angle.animateTo(0f, spring())
                                    }
                                }
                            }
                        }
                        .graphicsLayer {
                            translationX = offset.value.x * zoom.value
                            translationY = offset.value.y * zoom.value
                            scaleX = zoom.value
                            scaleY = zoom.value
                            rotationZ = angle.value
                        }
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product-image-fullscreen-${productId}"),
                            animatedVisibilityScope = this@AnimatedVisibility
                        )
                        .fillMaxWidth()
                )
            }
        }
    }
}