package com.nathaniel.carryapp.presentation.ui.compose.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.domain.request.SignInRequest
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.utils.AuthTextField
import com.nathaniel.carryapp.presentation.utils.DynamicButton
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
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
    var mailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF2E7D32), Color(0xFF4CAF50))
                )
            ),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "Sign in",
                showBackButton = false,
                showMenuButton = false,
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.lg, vertical = spacing.xl),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(spacing.md))

                AuthTextField(
                    value = mailAddress,
                    onValueChange = { mailAddress = it },
                    placeholder = "E-mail",
                    leadingIcon = Icons.Default.Email,
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

                Spacer(modifier = Modifier.height(spacing.lg))

                DynamicButton(
                    onClick = {
                        viewModel.onSignIn(SignInRequest(mailAddress, password))
                    },
                    height = sizes.buttonHeight,
                    fontSize = sizes.buttonFontSize,
                    backgroundColor = Color(0xFF2E7D32),
                    content = "Sign in"
                )

                Spacer(modifier = Modifier.height(spacing.md))

                Text(
                    text = "Forgot password?",
                    color = Color.Blue,
                    fontSize = sizes.buttonFontSize,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.SIGN_UP)
                    }
                )

                Spacer(modifier = Modifier.height(spacing.md))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.lsm),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color.White,
                    fontSize = sizes.buttonFontSize
                )
                Text(
                    text = "Sign up",
                    color = Color.Blue,
                    fontSize = sizes.buttonFontSize,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.SIGN_UP)
                    }
                )
            }
        }
    }
}
