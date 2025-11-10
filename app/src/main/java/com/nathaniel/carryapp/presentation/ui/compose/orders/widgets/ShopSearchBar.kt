package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopSearchBar(
    hint: String,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        placeholder = {
            Text(
                text = hint,
                color = Color(0xFF4CAF50), // green placeholder
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search Icon",
                tint = Color(0xFF4CAF50)
            )
        },
        singleLine = true,
        textStyle = TextStyle(
            color = Color(0xFF0E1F22), // darker font color for user input
            fontSize = 14.sp
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(48.dp)
            .background(Color.White, RoundedCornerShape(12.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedBorderColor = Color(0xFF4CAF50),
            unfocusedBorderColor = Color(0xFFE6ECEF),
            cursorColor = Color(0xFF118B3C),
            focusedTextColor = Color(0xFF0E1F22),
            unfocusedTextColor = Color(0xFF0E1F22)
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(query)
            }
        )
    )
}
