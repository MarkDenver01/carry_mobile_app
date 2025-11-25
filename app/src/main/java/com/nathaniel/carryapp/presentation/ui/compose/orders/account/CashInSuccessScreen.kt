package com.nathaniel.carryapp.presentation.ui.compose.orders.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun CashInSuccessScreen(
    navController: NavController,
    onDone: () -> Unit // refresh wallet
) {

    // Auto navigate back after animation
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds delay
        onDone()
        navController.popBackStack()
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(com.nathaniel.carryapp.R.raw.cash_in_success)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(220.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Cash-In Successful!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Your payment has been completed.",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}