package com.nathaniel.carryapp.presentation.ui.compose.promo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.carryapp.domain.model.SnowballPromo

@Composable
fun PromoOfferCard(
    promo: SnowballPromo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ✅ TOP GRADIENT SECTION
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFF59E0B), // ORANGE
                                Color(0xFF22C55E)  // GREEN
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // 🎁 ICON
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "🎁",
                            fontSize = 22.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            text = promo.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = promo.reward,
                            color = Color.White.copy(alpha = 0.95f),
                            fontSize = 13.sp
                        )
                    }
                }

                // ✅ EXPIRY BADGE
                if (promo.hasExpiry && promo.expiry != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Red.copy(alpha = 0.9f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Until ${promo.expiry}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ✅ BOTTOM SECTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFF5F6F7))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {

                Text(
                    text = "Buy ${promo.requiredQty} to unlock reward",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = promo.terms,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7D85)
                )

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF16A34A)
                    ),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        "Shop Now",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
