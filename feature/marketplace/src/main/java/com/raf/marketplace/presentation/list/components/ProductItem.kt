package com.raf.marketplace.presentation.list.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.raf.marketplace.R
import com.raf.marketplace.domain.model.Product
import com.raf.marketplace.presentation.components.RatingBar
import com.raf.marketplace.presentation.utilities.CurrencyHelper.covertToIDR

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SharedTransitionScope.ProductItem(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    product: Product,
    selectedId: Int?,
    onClicked: () -> Unit = {},
) {
    Card(
        colors = if (product.id == selectedId) CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
        else CardDefaults.cardColors(),
        onClick = onClicked,
        modifier = modifier
            .padding(2.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState("product-container-${product.id}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SubcomposeAsyncImage(
                model = product.image,
                contentDescription = product.title,
                loading = {
                    ContainedLoadingIndicator(
                        modifier = Modifier
                            .widthIn(max = 64.dp)
                            .aspectRatio(1f)
                    )
                },
                error = {
                    Icon(
                        imageVector = Icons.Default.BrokenImage,
                        contentDescription = stringResource(R.string.failed_to_load_product_image),
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .widthIn(max = 64.dp)
                            .aspectRatio(1f)
                    )
                },
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("product-image-${product.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .fillMaxWidth()
            )
            Text(
                text = product.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("product-title-${product.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .fillMaxWidth(),
            )
            RatingBar(
                rating = product.rating.rate,
                totalRaters = product.rating.count,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("product-rating-${product.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .fillMaxWidth(),
            )
            Text(
                text = product.priceInDollar.covertToIDR(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("product-price-${product.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .fillMaxWidth(),
            )
        }
    }
}