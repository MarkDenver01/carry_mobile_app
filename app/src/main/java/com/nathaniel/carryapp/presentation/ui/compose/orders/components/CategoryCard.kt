package com.nathaniel.carryapp.presentation.ui.compose.orders.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.carryapp.domain.model.Category
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.utils.AuthSocialButton

@Composable
fun CategoryCard(
    category: Category,
    viewModel: OrderViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth() // This fixes the spacing
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF4CAF50), RoundedCornerShape(12.dp))
            .background(Color.Gray)
    ) {
        Image(
            painter = painterResource(id = category.imageRes),
            contentDescription = category.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
        ) {
            Text(
                text = category.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 2
            )
        }

        AuthSocialButton(
            label = "SHOP NOW",
            onClick = { viewModel.onClickSelectProduct() },
            height = 30.dp,
            width = 80.dp,
            fontSize = 12.sp,
            backgroundColor = Color(0xFF00C3AD),
            pressedBackgroundColor = Color(0xFF009B89),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .height(30.dp),
        )
    }
}
