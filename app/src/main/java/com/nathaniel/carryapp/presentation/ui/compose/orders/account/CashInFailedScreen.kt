package com.nathaniel.carryapp.presentation.ui.compose.orders.account

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun CashInFailedScreen(
    navController: NavController
) {

    LaunchedEffect(Unit) {
        delay(2000)
        navController.popBackStack()
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(com.nathaniel.carryapp.R.raw.cash_in_failed)
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
                text = "Cash-In Failed",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Something went wrong.\nPlease try again.",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}