package com.nathaniel.carryapp.presentation.ui.compose.membership.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.enum.ButtonVariants
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.membership.MembershipViewModel
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.utils.IconButton
import com.nathaniel.carryapp.presentation.utils.IconButtonWithDescription
import com.nathaniel.carryapp.presentation.utils.responsiveDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    navController: NavController,
    viewModel: MembershipViewModel = hiltViewModel()
) {
    val navigateTo by viewModel.navigateTo.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current
    val topSpacing = responsiveDp(34f)

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "Payment Option",
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
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF2E7D32), Color(0XFF4CAF50))
                        )
                    )
                    .padding(
                        horizontal = spacing.lg, vertical = spacing.xl
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // START
                IconButtonWithDescription(
                    onClick = { viewModel.onGcashClick() },
                    modifier = Modifier.fillMaxWidth(),
                    height = 76.dp,
                    fontSize = 19.sp,
                    variant = ButtonVariants.Outlined,
                    painter = painterResource(id = R.drawable.ic_gcash),
                    title = "GCASH DIRECT",
                    description = "PAY USING GCASH VIA OUR SECURED FORM",
                    backgroundColor = Color(0xFF2E7D32),
                    outlinedBorderColor = Color(0xFFD6E7FF),
                    contentColor = Color.White
                )

                Spacer(modifier = Modifier.height(spacing.md))

                IconButtonWithDescription(
                    onClick = { viewModel.onGcashClick() },
                    modifier = Modifier.fillMaxWidth(),
                    height = 76.dp,
                    fontSize = 19.sp,
                    variant = ButtonVariants.Outlined,
                    painter = painterResource(id = R.drawable.ic_maya),
                    title = "MAYA DIRECT",
                    description = "PAY USING MAYA VIA OUR SECURED FORM",
                    backgroundColor = Color.White,
                    enabled = false
                )
            }
        }
    }
}