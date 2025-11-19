package com.nathaniel.carryapp.presentation.ui.compose.orders.location

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.components.BackHeader

// ───────────────── COLORS ─────────────────
private val GreenPrimary = Color(0xFF118B3C)
private val DarkGray = Color(0xFF4F4F4F)
private val BorderGray = Color(0xFFE0E0E0)
private val PlaceholderGray = Color(0xFF4F4F4F)
private val ErrorRed = Color(0xFFD84315)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryAddressScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    // ───────────────── STATE ─────────────────
    var address by remember { mutableStateOf("") }
    var province by remember { mutableStateOf<String?>(null) }
    var city by remember { mutableStateOf<String?>(null) }
    var barangay by remember { mutableStateOf<String?>(null) }
    var landmark by remember { mutableStateOf("") }

    // Sample data – palitan mo later with real lists
    val provinces = listOf("Batangas", "Laguna", "Cavite", "Rizal")
    val cities = listOf("City of Sto. Tomas", "Tanauan", "Lipa")
    val barangays = listOf("San Francisco", "Poblacion", "Darasa")

    // Required fields only: address, province, city, barangay
    val isFormValid =
        address.isNotBlank() &&
                !province.isNullOrBlank() &&
                !city.isNullOrBlank() &&
                !barangay.isNullOrBlank()

    Scaffold(
        containerColor = Color.White,
        topBar = { BackHeader(onBack = { navController.popBackStack() }) },
        bottomBar = {
            DeliveryBottomBar(
                enabled = isFormValid,
                onNext = { viewModel.onNext(navController) }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {

            // ───────── TITLE & SUBTITLE ─────────
            item {
                Text(
                    text = "Ilagay ang iyong delivery address",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Siguraduhing tama ang iyong address para masigurado na tama ang delivery",
                    fontSize = 14.sp,
                    color = DarkGray
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // ───────── ADDRESS ─────────
            item {
                RequiredLabel(text = "Blk/House No./Street")

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 96.dp),
                    placeholder = {
                        Text(
                            text = "Enter your address",
                            color = PlaceholderGray,
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = BorderGray,
                        cursorColor = GreenPrimary,

                        // FIXED: actual input text color
                        focusedTextColor = DarkGray,
                        unfocusedTextColor = DarkGray,

                        // Placeholder color
                        focusedPlaceholderColor = PlaceholderGray,
                        unfocusedPlaceholderColor = PlaceholderGray

                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // ───────── PROVINCE ─────────
            item {
                RequiredLabel(text = "Province")
                Spacer(modifier = Modifier.height(4.dp))

                DropdownField(
                    value = province,
                    placeholder = "Select province",
                    options = provinces,
                    onSelected = { province = it },
                    showCheckWhenSelected = true
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // ───────── CITY ─────────
            item {
                RequiredLabel(text = "City")
                Spacer(modifier = Modifier.height(4.dp))

                DropdownField(
                    value = city,
                    placeholder = "Select city",
                    options = cities,
                    onSelected = { city = it },
                    showCheckWhenSelected = true
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // ───────── BARANGAY ─────────
            item {
                RequiredLabel(text = "Barangay")
                Spacer(modifier = Modifier.height(4.dp))

                DropdownField(
                    value = barangay,
                    placeholder = "Select barangay",
                    options = barangays,
                    onSelected = { barangay = it },
                    showCheckWhenSelected = true
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // ───────── LANDMARK (no *) ─────────
            item {
                Text(
                    text = "Landmark",
                    fontSize = 14.sp,
                    color = DarkGray
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = landmark,
                    onValueChange = { landmark = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Landmark",
                            color = PlaceholderGray
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = BorderGray,
                        cursorColor = GreenPrimary,

                        // FIXED: actual input text color
                        focusedTextColor = DarkGray,
                        unfocusedTextColor = DarkGray,

                        // Placeholder color
                        focusedPlaceholderColor = PlaceholderGray,
                        unfocusedPlaceholderColor = PlaceholderGray
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    )
                )

                Spacer(modifier = Modifier.height(80.dp)) // para hindi dumikit sa bottom bar
            }
        }
    }
}

/* ─────────────────────────────
   Shared components
   ───────────────────────────── */

@Composable
private fun RequiredLabel(text: String) {
    Text(
        text = buildAnnotatedString {
            append(text)
            append(" ")
            withStyle(SpanStyle(color = ErrorRed)) {
                append("*")
            }
        },
        fontSize = 14.sp,
        color = DarkGray
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    value: String?,
    placeholder: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    showCheckWhenSelected: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = value ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    text = placeholder,
                    color = PlaceholderGray
                )
            },
            trailingIcon = {
                if (showCheckWhenSelected && !value.isNullOrBlank()) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = GreenPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = PlaceholderGray
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = BorderGray,
                cursorColor = GreenPrimary,

                // FIXED: actual input text color
                focusedTextColor = DarkGray,
                unfocusedTextColor = DarkGray,

                // Placeholder color
                focusedPlaceholderColor = PlaceholderGray,
                unfocusedPlaceholderColor = PlaceholderGray
            ),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DeliveryBottomBar(
    enabled: Boolean,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Button(
            onClick = onNext,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenPrimary,
                disabledContainerColor = Color(0xFFE0E0E0),
                contentColor = Color.White,
                disabledContentColor = Color(0xFFBDBDBD)
            ),
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
