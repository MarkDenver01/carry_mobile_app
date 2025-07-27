package com.nathaniel.carryapp.presentation.ui.compose.voucher.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.carryapp.domain.model.Voucher
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing

@Composable
fun VoucherCard(
    voucher: Voucher,
    onClick: (Voucher) -> Unit // Callback when clicked
) {
    val spacing = LocalAppSpacing.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(voucher) }, // Make the card clickable
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(spacing.md)) {
            Text(
                text = voucher.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = voucher.description,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
