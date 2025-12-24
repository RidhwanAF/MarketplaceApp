package com.raf.marketplace.presentation.list.components

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.raf.core.presentation.components.customIconButtonShapes
import com.raf.marketplace.R
import com.raf.marketplace.domain.model.Product
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SharedTransitionScope.PopupProductItemDialog(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isDetailVisible: Boolean,
    product: Product?,
    onDismiss: () -> Unit = {},
    onClicked: (id: Int) -> Unit = {},
    onAddToCart: (id: Int) -> Unit = {},
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(product, isDetailVisible) {
        if (product != null && !isDetailVisible) {
            visible = true
        } else {
            delay(250)
            visible = false
        }
    }

    // Back Handler
    var backProgression by remember { mutableFloatStateOf(0f) }
    PredictiveBackHandler(visible) { backEventCompat ->
        backEventCompat.collect { backEvent ->
            backProgression = backEvent.progress
        }
        onDismiss()
    }
    LaunchedEffect(visible) {
        if (!visible) {
            delay(300)
            backProgression = 0f
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val scrollState = rememberScrollState()

        Scaffold(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(
                (0.55f - backProgression)
                    .coerceAtLeast(0.15f)
            ),
            contentColor = MaterialTheme.colorScheme.surface,
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                itemVerticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { onDismiss() })
                    }
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                product?.let {
                    ProductItem(
                        animatedVisibilityScope = animatedVisibilityScope,
                        popupAnimatedVisibilityScope = this@AnimatedVisibility,
                        product = it,
                        selectedId = null,
                        onClicked = {
                            onClicked(it.id)
                            onDismiss()
                        },
                        modifier = Modifier
                            .graphicsLayer {
                                val newScale = (1f - backProgression).coerceAtLeast(0.85f)
                                scaleX = newScale
                                scaleY = newScale
                            }
                            .clip(MaterialTheme.shapes.large)
                            .widthIn(min = 175.dp, max = 300.dp)
                            .heightIn(min = 250.dp, max = 480.dp)
                            .verticalScroll(scrollState)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    IconLabelButton(
                        icon = Icons.Default.Close,
                        label = stringResource(R.string.close),
                        onClick = onDismiss
                    )
                    IconLabelButton(
                        icon = Icons.Default.AddShoppingCart,
                        label = stringResource(R.string.add_to_cart),
                        onClick = {
                            product?.let {
                                onAddToCart(it.id)
                                onDismiss()
                            }
                        }
                    )
                    IconLabelButton(
                        icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        label = stringResource(R.string.details),
                        onClick = {
                            product?.let {
                                onClicked(it.id)
                                onDismiss()
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun IconLabelButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(80.dp)
    ) {
        FilledIconButton(
            shapes = customIconButtonShapes(),
            onClick = onClick
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            color = MaterialTheme.colorScheme.surface,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.basicMarquee()
        )
    }
}