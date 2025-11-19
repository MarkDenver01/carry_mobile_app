package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel

@Composable
fun BottomActionBar(
    viewModel: OrderViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Hindi kasama dito ang iyong lugar?",
            fontSize = 14.sp,
            color = Color(0xFF4F4F4F)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = buildAnnotatedString {
                append("Follow our ")
                withStyle(
                    SpanStyle(
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.SemiBold
                    )
                ) { append("social media accounts") }
                append(" for more updates.")
            },
            fontSize = 14.sp,
            color = Color(0xFF4F4F4F)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onDeliveryAreaClick(navController) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF118B3C)),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Next",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
