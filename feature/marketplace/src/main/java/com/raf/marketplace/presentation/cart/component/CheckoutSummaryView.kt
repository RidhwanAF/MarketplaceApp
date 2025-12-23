package com.raf.marketplace.presentation.cart.component

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.raf.core.presentation.components.customIconButtonShapes
import com.raf.marketplace.R
import com.raf.marketplace.presentation.cart.viewmodel.ProductInCartUi
import com.raf.marketplace.presentation.components.ProductPriceTotalBottomBar
import com.raf.marketplace.presentation.utilities.CurrencyHelper.convertToIDR
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SharedTransitionScope.CheckoutSummaryView(
    modifier: Modifier = Modifier,
    visible: Boolean,
    profileName: String,
    profilePhoneNumber: String,
    profileAddress: String,
    totalPriceInDollar: Double,
    totalQuantity: Int,
    productsInCart: List<ProductInCartUi>,
    onClose: () -> Unit,
    onCheckout: () -> Unit,
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Back Handler
    var backProgression by remember {
        mutableFloatStateOf(0f)
    }
    PredictiveBackHandler(visible) { backEventCompat ->
        backEventCompat.collect { backEvent ->
            backProgression = backEvent.progress
        }
        onClose()
    }
    LaunchedEffect(visible) {
        if (visible) {
            delay(300)
            backProgression = 0f
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()),
        exit = scaleOut(animationSpec = MaterialTheme.motionScheme.fastSpatialSpec())
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.checkout),
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
                                onClick = onClose
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.close)
                                )
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                ProductPriceTotalBottomBar(
                    totalPriceInDollar = totalPriceInDollar,
                    quantity = totalQuantity,
                    buttonLabel = stringResource(R.string.checkout),
                    buttonIcon = Icons.Default.ShoppingCartCheckout,
                    onButtonClicked = {
                        onCheckout()
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                    },
                )
            },
            modifier = modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState("checkout-summary-container-key"),
                    animatedVisibilityScope = this
                )
                .graphicsLayer {
                    val newScale = (1f - backProgression).coerceAtLeast(0.85f)
                    translationY = backProgression * 250f
                    scaleX = newScale
                    scaleY = newScale
                }
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(bottom = 16.dp)
            ) {
                // Profile Detail
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.address_detail),
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                        HorizontalDivider()
                        ProfileSummaryItem(
                            title = stringResource(R.string.name),
                            value = profileName,
                            icon = Icons.Default.Person
                        )
                        ProfileSummaryItem(
                            title = stringResource(R.string.phone_number),
                            value = profilePhoneNumber,
                            icon = Icons.Default.Phone
                        )
                        ProfileSummaryItem(
                            title = stringResource(R.string.address),
                            value = profileAddress,
                            icon = Icons.Default.LocationOn
                        )
                    }
                }
                // Product List and Price
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.price_detail),
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                        HorizontalDivider()
                        productsInCart.forEach { (product, cart) ->
                            ProductSummaryItem(
                                title = product.title,
                                quantity = cart.quantity,
                                totalPriceInDollar = product.priceInDollar * cart.quantity
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.total),
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = totalPriceInDollar.convertToIDR(),
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                textAlign = TextAlign.End,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
