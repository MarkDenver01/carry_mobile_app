package com.nathaniel.carryapp.presentation.ui.compose.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.request.SignUpRequest
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.utils.AuthSocialButton
import com.nathaniel.carryapp.presentation.utils.AuthTextField
import com.nathaniel.carryapp.presentation.utils.DynamicButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
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
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var mailAddress by remember { mutableStateOf("") }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "Sign up",
                showBackButton = false,
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
                        listOf(Color(0xFF2E7D32), Color(0xFF4CAF50))
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
                // INPUT FIELDS
                AuthTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    placeholder = "Full name",
                    leadingIcon = Icons.Default.Person,
                    fontSize = sizes.buttonFontSize
                )
                AuthTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    isPassword = true,
                    leadingIcon = Icons.Default.Lock,
                    fontSize = sizes.buttonFontSize
                )
                AuthTextField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = "Address",
                    leadingIcon = Icons.Default.Home,
                    fontSize = sizes.buttonFontSize
                )
                AuthTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = "Phone Number",
                    leadingIcon = Icons.Default.Phone,
                    fontSize = sizes.buttonFontSize
                )
                AuthTextField(
                    value = mailAddress,
                    onValueChange = { mailAddress = it },
                    placeholder = "E-mail",
                    leadingIcon = Icons.Default.Email,
                    fontSize = sizes.buttonFontSize
                )

                Spacer(modifier = Modifier.height(spacing.md))

                DynamicButton(
                    onClick = {
                        viewModel.onSignUp(
                            SignUpRequest(userName, password, address, phone, mailAddress)
                        )
                    },
                    height = sizes.buttonHeight,
                    fontSize = sizes.buttonFontSize,
                    backgroundColor = Color(0xFF2E7D32),
                    content = "Sign up"
                )

                Spacer(modifier = Modifier.height(spacing.sm))

                Text(
                    text = "By signing up you agree to the Terms of Service",
                    fontSize = 13.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(spacing.md))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(modifier = Modifier.weight(1f), color = Color.White)
                    Text(
                        " or ",
                        color = Color.White,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = spacing.sm)
                    )
                    Divider(modifier = Modifier.weight(1f), color = Color.White)
                }

                Spacer(modifier = Modifier.height(spacing.md))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AuthSocialButton(
                        icon = R.drawable.ic_google,
                        label = "Google",
                        onClick = { viewModel.onAuthGoogleSignUp() },
                        40.dp,
                        150.dp,
                        15.sp
                    )

                    AuthSocialButton(
                        icon = R.drawable.ic_facebook,
                        label = "Facebook",
                        onClick = { viewModel.onAuthFacebookSignUp() },
                        40.dp,
                        150.dp,
                        15.sp
                    )
                }

                Spacer(modifier = Modifier.height(spacing.md))

                DynamicButton(
                    onClick = { viewModel.onContinueAsGuest() },
                    height = sizes.buttonHeight * 0.65f,
                    fontSize = 14.sp,
                    backgroundColor = Color(0xFF0B6623),
                    content = "Continue as Guest"
                )

                Spacer(modifier = Modifier.height(spacing.lg))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing.lg),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Already have an account? ",
                        color = Color.White,
                        fontSize = sizes.buttonFontSize
                    )
                    Text(
                        text = "Sign in",
                        color = Color.Blue,
                        fontSize = sizes.buttonFontSize,
                        modifier = Modifier.clickable { navController.navigate(Routes.SIGN_IN) }
                    )
                }
            }
        }
    }
}
