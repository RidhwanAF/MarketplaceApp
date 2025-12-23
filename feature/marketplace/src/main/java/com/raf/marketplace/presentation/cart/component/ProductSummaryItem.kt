package com.raf.marketplace.presentation.cart.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.raf.marketplace.R
import com.raf.marketplace.presentation.utilities.CurrencyHelper.convertToIDR

@Composable
fun ProductSummaryItem(
    modifier: Modifier = Modifier,
    title: String,
    quantity: Int,
    totalPriceInDollar: Double,
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )

            val quantityString = if (quantity > 1) {
                stringResource(R.string.items_with_args, quantity)
            } else {
                stringResource(R.string.item_with_args, quantity)
            }
            Text(
                text = "*$quantityString",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(
            text = totalPriceInDollar.convertToIDR(),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            textAlign = TextAlign.End,
        )
    }
}