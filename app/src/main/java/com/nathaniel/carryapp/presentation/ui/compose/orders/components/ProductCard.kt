package com.nathaniel.carryapp.presentation.ui.compose.orders.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalAppTypography
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.orders.sub_screen.DummyProduct
import com.nathaniel.carryapp.presentation.utils.AuthSocialButton

@Composable
fun ProductCard(
    dummyProduct: DummyProduct,
    modifier: Modifier = Modifier
) {
    val spacing = LocalAppSpacing.current
    val context = LocalContext.current
    val responsiveSizes = LocalResponsiveSizes.current
    val typography = LocalAppTypography.current

    Card(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.4f),
                shape = MaterialTheme.shapes.large
            )
            .fillMaxWidth()
            .clickable { /* Handle click */ },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2E7D32), // Dark green
                            Color(0xFF4CAF50)  // Light green
                        )
                    ),
                    shape = MaterialTheme.shapes.large
                )
                .padding(spacing.lg)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(percent = 25)) // Oval shape
                    .border(
                        width = 2.dp,
                        color = Color.White.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(percent = 25)
                    )
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.delivery_scooter),
                    contentDescription = dummyProduct.productName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp) // Add padding here
                )
            }


            Spacer(modifier = Modifier.height(spacing.md))

            Text(
                text = dummyProduct.productName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(spacing.xs))

            Text(
                text = "$${String.format("%.2f", dummyProduct.price)}",
                style = typography.body,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(spacing.md))

            AuthSocialButton(
                label = "Add to Cart",
                onClick = { },
                height = 30.dp,
                width = 140.dp,
                fontSize = 12.sp,
                backgroundColor = Color(0xFF00C3AD),
                pressedBackgroundColor = Color(0xFF009B89),
                modifier = Modifier
                    .padding(8.dp)
                    .height(30.dp),
            )
        }
    }
}

