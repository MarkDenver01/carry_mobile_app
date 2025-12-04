package com.nathaniel.carryapp.presentation.ui.compose.orders.location

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.components.AreaRow
import com.nathaniel.carryapp.presentation.ui.compose.orders.components.BackHeader
import com.nathaniel.carryapp.presentation.ui.compose.orders.components.LoadingAreaCardShimmer
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.BottomActionBar
import com.nathaniel.carryapp.presentation.ui.sharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryAreaScreen(
    navController: NavController
) {
    val viewModel: OrderViewModel = sharedViewModel()
    val selectedArea by viewModel.selected.collectAsState()
    val regions by viewModel.regions.collectAsState()
    val error by viewModel.error.collectAsState()

    val mainRegion = "Region IV-A (CALABARZON)"

    Scaffold(
        containerColor = Color.White,
        topBar = { BackHeader(onBack = { navController.popBackStack() }) },
        bottomBar = { BottomActionBar(viewModel, navController) }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {

            // HEADER
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Ang WrapAndCarry ay maaaring mag deliver sa mga sumusunod na lugar",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(24.dp))

                    AreaRow(
                        title = mainRegion,
                        isSelected = selectedArea == mainRegion,
                        isMain = true
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "Mga piling lugar sa $mainRegion",
                        fontSize = 14.sp,
                        color = Color(0xFF4F4F4F)
                    )

                    Spacer(Modifier.height(10.dp))
                }
            }

            // ERROR
            if (!error.isNullOrEmpty()) {
                item {
                    Text(
                        text = error ?: "Error loading areas",
                        color = Color.Red,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // SHIMMER LOADING
            if (regions.isEmpty() && error == null) {
                items(5) { LoadingAreaCardShimmer() }
            }

            // LIST OF PROVINCES — NO SPACING BELOW EACH ROW
            items(regions.size) { index ->
                val province = regions[index]

                AreaRow(
                    title = province,
                    isSelected = selectedArea == province,
                    isMain = false
                )
            }
        }
    }
}
