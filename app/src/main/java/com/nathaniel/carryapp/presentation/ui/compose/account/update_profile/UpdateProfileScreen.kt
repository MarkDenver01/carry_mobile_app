package com.nathaniel.carryapp.presentation.ui.compose.account.update_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.account.AccountViewModel
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.utils.AuthTextField
import com.nathaniel.carryapp.presentation.utils.DatePickerField
import com.nathaniel.carryapp.presentation.utils.DropdownSelectorField
import com.nathaniel.carryapp.presentation.utils.DynamicButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(
    navController: NavController,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val navigateTo by viewModel.navigateTo.collectAsState()

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }

    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current

    var userName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var mailAddress by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "Update User Profile",
                showBackButton = true,
                showMenuButton = false,
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF2E7D32), Color(0XFF4CAF50))
                    )
                )
                .padding(innerPadding)
        ) {
            // SCROLLABLE COLUMN
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = spacing.lg, vertical = spacing.xl)
            ) {
                Text(
                    text = "Update your information",
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(spacing.md))

                // INPUT FIELDS
                AuthTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    placeholder = "Full name",
                    leadingIcon = Icons.Default.Person,
                    fontSize = sizes.buttonFontSize
                )

                AuthTextField(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    placeholder = "Mobile number",
                    leadingIcon = Icons.Default.Phone,
                    fontSize = sizes.buttonFontSize
                )

                AuthTextField(
                    value = mailAddress,
                    onValueChange = { mailAddress = it },
                    placeholder = "Email address",
                    leadingIcon = Icons.Default.Email,
                    fontSize = sizes.buttonFontSize
                )

                AuthTextField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = "Address",
                    leadingIcon = Icons.Default.Home,
                    fontSize = sizes.buttonFontSize
                )

                DatePickerField(
                    date = dateOfBirth,
                    onDateSelected = { dateOfBirth = it },
                    placeholder = "____-__-__",
                    leadingIcon = Icons.Default.DateRange
                )

                DropdownSelectorField(
                    selectedOption = gender,
                    onOptionSelected = { gender = it },
                    placeholder = "Select Gender",
                    leadingIcon = Icons.Default.Person
                )

                AuthTextField(
                    value = age,
                    onValueChange = { age = it },
                    placeholder = "Age",
                    leadingIcon = Icons.Default.Star,
                    fontSize = sizes.buttonFontSize
                )

                Spacer(modifier = Modifier.height(spacing.md))

                DynamicButton(
                    onClick = {  },
                    height = sizes.buttonHeight,
                    fontSize = sizes.buttonFontSize,
                    backgroundColor = Color(0xFF2E7D32),
                    content = "Save Profile"
                )

            }
        }
    }
}