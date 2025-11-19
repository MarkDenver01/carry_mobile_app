package com.nathaniel.carryapp.presentation.ui.compose.location

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.location.AreaCard
import com.nathaniel.carryapp.presentation.ui.compose.orders.location.BackHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryAreaScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val selectedArea by viewModel.selected.collectAsState()
    val regions = viewModel.regions
    val mainRegion = viewModel.mainRegion

    Scaffold(
        containerColor = Color.White,
        topBar = {
            BackHeader(
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                    onClick = { viewModel.onNext(navController) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Next",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { inner ->

        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Ang Sukigrocer ay maaaring mag deliver sa mga sumusunod na lugar",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 28.sp
                    )

                    Spacer(Modifier.height(24.dp))

                    AreaCard(
                        title = mainRegion,
                        isSelected = selectedArea == mainRegion,
                        onClick = { viewModel.select(mainRegion) },
                        isMain = true
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "Mga piling lugar sa",
                        fontSize = 14.sp,
                        color = Color(0xFF4F4F4F)
                    )

                    Spacer(Modifier.height(12.dp))
                }
            }

            items(regions.size) { index ->
                val region = regions[index]

                AreaCard(
                    title = region,
                    isSelected = selectedArea == region,
                    onClick = { viewModel.select(region) },
                    isMain = false
                )

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
