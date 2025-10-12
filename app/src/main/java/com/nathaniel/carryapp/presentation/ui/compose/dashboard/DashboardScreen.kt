package com.nathaniel.carryapp.presentation.ui.compose.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.components.PromoBannerCarousel
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.service.ServiceOptionCard
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.sidemenu.DashboardDrawerContent
import com.nathaniel.carryapp.presentation.ui.compose.navigation.BottomNavigationBar
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.utils.responsiveDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsState()
    val navigateTo by viewModel.navigateTo.collectAsState()

    var isDrawerOpen by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val outerPadding = responsiveDp(16f)
    val bannerHeight = responsiveDp(180f)
    val sectionSpacing = responsiveDp(20f)
    val topSpacing = responsiveDp(1f)
    val cardSpacing = responsiveDp(16f)

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val drawerWidth = screenWidth * 0.7f

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
                title = "Mabuhay, $userName",
                showBackButton = true,
                showMenuButton = true,
                scrollBehavior = scrollBehavior,
                onMenuClick = { isDrawerOpen = true }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF2E7D32), Color(0xFF4CAF50))
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(topSpacing))

                PromoBannerCarousel(
                    modifier = Modifier.fillMaxWidth(),
                    bannerHeight = bannerHeight
                )

                Spacer(modifier = Modifier.height(sectionSpacing))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = outerPadding)
                ) {
                    ServiceOptionCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Delivery",
                        subtitle = "Groceries delivered at your doorsteps",
                        iconRes = R.drawable.delivery_scooter,
                        onClick = { viewModel.onDeliveryClick() }
                    )

                    Spacer(modifier = Modifier.height(cardSpacing))

                    ServiceOptionCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = "In-store Pickup",
                        subtitle = "Pick up at your favorite store",
                        iconRes = R.drawable.cart_basket,
                        onClick = { viewModel.onPickupClick() }
                    )
                }

                Spacer(modifier = Modifier.height(sectionSpacing))
            }

            // Drawer and overlay
            if (isDrawerOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            isDrawerOpen = false
                        }
                )

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(drawerWidth)
                        .background(Color.White)
                        .align(Alignment.CenterStart)
                ) {
                    DashboardDrawerContent(
                        viewModel = viewModel,
                        onCloseDrawer = { isDrawerOpen = false }
                    )
                }
            }
        }
    }
}
