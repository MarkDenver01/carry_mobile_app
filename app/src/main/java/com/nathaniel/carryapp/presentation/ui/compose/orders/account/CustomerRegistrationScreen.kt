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
import com.nathaniel.carryapp.presentation.ui.compose.orders.CustomerRegistrationUIEvent
import com.nathaniel.carryapp.presentation.ui.compose.orders.components.BackHeader
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerRegistrationScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {

    val state = viewModel.uiState.collectAsState()

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
                            state.value.userName,
                            state.value.address,
                            state.value.mobileNumber,
                            state.value.email
                        )
                        viewModel
                            .submitCustomer(request)
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
    ) { inner ->

        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {

            /** TITLE */
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


            /** UPLOAD PHOTO */
            item {
                UploadPhotoSection(
                    imageUri = state.value.photoUri,
                    onPickImage = { uri ->
                        viewModel.onEvent(
                            CustomerRegistrationUIEvent.OnPhotoSelected(
                                uri
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            /** INPUT FIELDS */
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                    RegistrationInputField(
                        label = "Full Name",
                        value = state.value.userName,
                        onValueChange = {
                            viewModel.onEvent(
                                CustomerRegistrationUIEvent.OnUserNameChanged(
                                    it
                                )
                            )
                        }
                    )

                    RegistrationInputField(
                        label = "Email Address",
                        value = state.value.email,
                        enabled = !state.value.isEmailDisabled,
                        onValueChange = {
                            viewModel.onEvent(
                                CustomerRegistrationUIEvent.OnEmailChanged(
                                    it
                                )
                            )
                        }
                    )

                    RegistrationInputField(
                        label = "Mobile Number",
                        value = state.value.mobileNumber,
                        enabled = !state.value.isMobileDisabled,
                        onValueChange = {
                            viewModel.onEvent(
                                CustomerRegistrationUIEvent.OnMobileChanged(
                                    it
                                )
                            )
                        },
                        keyboardType = KeyboardType.Phone
                    )

                    RegistrationLocationField(
                        label = "Address",
                        value = state.value.address
                    )

                    Spacer(modifier = Modifier.height(20.dp))
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

