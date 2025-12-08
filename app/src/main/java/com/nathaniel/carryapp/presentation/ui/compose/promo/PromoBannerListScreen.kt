package com.nathaniel.carryapp.presentation.ui.compose.promo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.sharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoBannerListScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = sharedViewModel()
) {
    // ✅ NOW USING SNOWBALL PROMOS
    val promos by orderViewModel.snowballPromos.collectAsState()

    LaunchedEffect(Unit) {
        orderViewModel.loadSnowballPromos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF118B3C),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(
                        text = "Offers",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        },
        containerColor = Color(0xFFF7F8FA)
    ) { inner ->

        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {
            items(promos, key = { it.id }) { promo
                ->
                PromoOfferCard(
                    promo = promo,
                    onClick = {
                        navController.navigate("orders")
                    }
                )
            }
        }
    }
}
