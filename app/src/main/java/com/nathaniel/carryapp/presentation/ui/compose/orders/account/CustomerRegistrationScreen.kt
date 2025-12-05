package com.nathaniel.carryapp.presentation.ui.compose.orders.account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.nathaniel.carryapp.domain.request.CustomerRegistrationRequest
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.components.BackHeader
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.signin.SignInViewModel
import com.nathaniel.carryapp.presentation.ui.state.CustomerRegistrationUIEvent
import com.nathaniel.carryapp.presentation.ui.state.CustomerUiAction
import com.nathaniel.carryapp.presentation.utils.CustomToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerRegistrationScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = hiltViewModel(),
    signInViewModel: SignInViewModel = hiltViewModel()
) {
    val state by orderViewModel.uiState.collectAsState()
    val toastState by orderViewModel.toastState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        // =========================
        // SCREEN CONTENT BELOW
        // =========================
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
                            val request = CustomerRegistrationRequest(
                                userName = state.userName,
                                address = state.address,
                                mobileNumber = state.mobileNumber,
                                email = state.email
                            )
                            signInViewModel.saveLoginSession(email = state.email, session = true)
                            orderViewModel.submitCustomer(request, photoUri = state.photoUri)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Text(
                            "Register Customer",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Customer Registration",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Fill out the required details to register your customer.",
                            fontSize = 14.sp,
                            color = Color(0xFF4F4F4F),
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                item {
                    UploadPhotoSection(
                        imageUri = state.photoUri,
                        onPickImage = { uri ->
                            orderViewModel.onCustomerFillEvent(
                                CustomerRegistrationUIEvent.OnPhotoSelected(uri)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                        RegistrationInputField(
                            label = "Full Name",
                            value = state.userName,
                            onValueChange = {
                                orderViewModel.onCustomerFillEvent(
                                    CustomerRegistrationUIEvent.OnUserNameChanged(it)
                                )
                            }
                        )

                        RegistrationInputField(
                            label = "Email Address",
                            value = state.email,
                            enabled = !state.isEmailDisabled,
                            onValueChange = {
                                orderViewModel.onCustomerFillEvent(
                                    CustomerRegistrationUIEvent.OnEmailChanged(it)
                                )
                            }
                        )

                        RegistrationInputField(
                            label = "Mobile Number",
                            value = state.mobileNumber,
                            enabled = !state.isMobileDisabled,
                            onValueChange = {
                                orderViewModel.onCustomerFillEvent(
                                    CustomerRegistrationUIEvent.OnMobileChanged(it)
                                )
                            },
                            keyboardType = KeyboardType.Phone
                        )

                        RegistrationLocationField(
                            label = "Address",
                            value = state.address + ", Philippines"
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }

        // =========================
        // TOAST OVERLAY (ALWAYS ON TOP)
        // =========================
        CustomToast(
            toastMessage = toastState,
            onDismiss = { orderViewModel.resetToast() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = 20.dp)
        )

        // =========================
        // NAVIGATION EVENT
        // =========================
        LaunchedEffect(orderViewModel.customerUiAction) {
            orderViewModel.customerUiAction.collect { action ->
                when (action) {
                    is CustomerUiAction.Navigate -> {
                        navController.navigate(action.route) {
                            popUpTo(Routes.SIGN_IN) { inclusive = false }
                        }
                        orderViewModel.resetCustomerAction()
                    }

                    null -> Unit
                }
            }
        }
    }
}

/* ------------------------------------------------------
   UPLOAD PHOTO COMPONENT
--------------------------------------------------------- */

@Composable
fun UploadPhotoSection(
    imageUri: Uri?,
    onPickImage: (Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onPickImage(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFF2F2F2), CircleShape)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {

            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Profile Photo",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    "Upload Photo",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Tap to upload profile photo",
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}

/* ------------------------------------------------------
   CUSTOM INPUT FIELD
--------------------------------------------------------- */

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )

        Spacer(Modifier.height(12.dp))
    }
}

/* ------------------------------------------------------
   READ-ONLY FIELD (Address)
--------------------------------------------------------- */

@Composable
fun RegistrationLocationField(label: String, value: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = DarkGray
        )
        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
                .padding(vertical = 14.dp, horizontal = 12.dp)
        ) {
            Text(
                text = value ?: "Loading...",
                fontSize = 15.sp,
                color = DarkGray
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}


/* ------------------------------------------------------
   UNIFIED OUTLINED INPUT FIELD (Matches DeliveryAddressScreen)
--------------------------------------------------------- */

private val GreenPrimary = Color(0xFF118B3C)
private val DarkGray = Color(0xFF4F4F4F)
private val BorderGray = Color(0xFFE0E0E0)
private val PlaceholderGray = Color(0xFF4F4F4F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = DarkGray
        )
        Spacer(Modifier.height(4.dp))

        OutlinedTextField(
            value = value,
            onValueChange = { if (enabled) onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp),
            placeholder = {
                Text(text = label, color = PlaceholderGray)
            },
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = BorderGray,
                disabledBorderColor = BorderGray,
                disabledTextColor = Color.Gray,
                cursorColor = GreenPrimary,
                focusedTextColor = DarkGray,
                unfocusedTextColor = DarkGray,
                disabledPlaceholderColor = Color.LightGray,
                disabledContainerColor = Color(0xFFF7F7F7),
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(Modifier.height(16.dp))
    }
}