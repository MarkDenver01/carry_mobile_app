package com.nathaniel.carryapp.presentation.ui.compose.orders.location

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.nathaniel.carryapp.domain.request.DeliveryAddressRequest
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.components.BackHeader
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.utils.rememberLocationPermissionState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun LocationConfirmationScreen(
    navController: NavController
) {
    val viewModel: OrderViewModel = sharedViewModel()
    // Observe VIEWMODEL ADDRESS STATE
    val addressState = viewModel.reverseAddress.collectAsState()
    val pinPosition by viewModel.selectedLatLng.collectAsState()

    // CAMERA
    val cameraPositionState = rememberCameraPositionState()

    // 🔥 WHENEVER pinPosition changes → MOVE CAMERA
    LaunchedEffect(pinPosition) {
        if (pinPosition != null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(pinPosition!!, 17f),
                durationMs = 800
            )
        }
    }

    val requestLocationPermission = rememberLocationPermissionState(
        onPermissionGranted = {
            viewModel.loadCurrentLocation()
        }
    )

    Scaffold(
        containerColor = Color.White,
        topBar = { BackHeader { navController.popBackStack() } },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        val detailAddress = addressState.value.fullAddressLine
                        val province = addressState.value.province
                        val city = addressState.value.city
                        val barangay = addressState.value.barangay

                        val deliveryAddressRequest = DeliveryAddressRequest(
                            provinceCode = "",
                            provinceName = province ?: "",
                            cityCode = "",
                            cityName = city ?: "",
                            barangayCode = "",
                            barangayName = barangay ?: "",
                            addressDetails = detailAddress ?: "",
                            landMark = ""
                        )

                        viewModel.confirmAddress(deliveryAddressRequest, navController)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text(
                        "Confirm Address",
                        color = Color.White,
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
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {

            // HEADER TITLE
            item {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "I-confirm kung tama ang iyong address",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "Siguraduhing tama ang iyong address para masigurado na tama ang delivery",
                        fontSize = 14.sp,
                        color = Color(0xFF4F4F4F),
                        lineHeight = 20.sp
                    )

                    Spacer(Modifier.height(20.dp))
                }
            }

            // ░░▒▒▓▓ GOOGLE MAP ▓▓▒▒░░
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(horizontal = 16.dp)
                        .background(Color.LightGray, RoundedCornerShape(12.dp))
                ) {

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(),
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = false,
                            myLocationButtonEnabled = false
                        ),
                        onMapClick = { tapped ->
                            viewModel.updatePin(tapped)
                        }
                    ) {
                        if (pinPosition != null) {
                            Marker(
                                state = MarkerState(position = pinPosition!!),
                                title = "Delivery Location"
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
            }

            // USE MY CURRENT LOCATION BUTTON
            item {
                Button(
                    onClick = {
                        requestLocationPermission()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE3F2FD),
                        contentColor = Color(0xFF1E88E5)
                    )
                ) {
                    Text(
                        "Use My Current Location",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.height(16.dp))
            }

            // ADDRESS FIELDS
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                    LocationField("Full Address", addressState.value.fullAddressLine)
                    LocationField("Province", addressState.value.province)
                    LocationField("City / Municipality", addressState.value.city)
                    LocationField("Barangay", addressState.value.barangay)

                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun LocationField(label: String, value: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
                .padding(14.dp)
        ) {
            Text(
                text = value ?: "Loading...",
                fontSize = 15.sp,
                color = Color.Black
            )
        }
        Spacer(Modifier.height(12.dp))
    }
}
