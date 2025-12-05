package com.nathaniel.carryapp.presentation.ui.compose.orders.account

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.sharedViewModel

import kotlinx.coroutines.delay

@Composable
fun CashInSuccessScreen(navController: NavController) {
    val customerViewModel: CustomerViewModel = sharedViewModel()

    // Auto navigate back after animation
    LaunchedEffect(Unit) {
        delay(2000)
        customerViewModel.refreshWallet()

        navController.navigate(Routes.ACCOUNT) {
            popUpTo(Routes.ACCOUNT) { inclusive = true }  // Clear stack to Account only
            launchSingleTop = true
        }
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
