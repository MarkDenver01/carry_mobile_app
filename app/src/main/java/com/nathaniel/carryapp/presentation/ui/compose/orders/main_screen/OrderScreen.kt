package com.nathaniel.carryapp.presentation.ui.compose.orders.main_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.model.Category
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.navigation.BottomNavigationBar
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.components.CategoryCard
import com.nathaniel.carryapp.presentation.utils.AuthSocialButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val navigateTo by viewModel.navigateTo.collectAsState()
    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val categories = listOf(
        Category("BEVERAGES", R.drawable.ic_bevarages),
        Category("WINES & LIQUOR", R.drawable.ic_wine),
        Category("SNACKS", R.drawable.ic_snack),
        Category("SWEETS", R.drawable.ic_bevarages),
        Category("MILK PRODUCTS", R.drawable.ic_bevarages),
        Category("FORMULA MILK & BABY FOODS", R.drawable.ic_bevarages),
        Category("CIGARS", R.drawable.ic_bevarages),
        Category("CONDIMENTS, SAUCES & DRESSINGS", R.drawable.ic_bevarages),
        Category("CANNED GOODS", R.drawable.ic_bevarages),
        Category("GROCERY STAPLES", R.drawable.ic_bevarages),
    )

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }

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
                title = "Order",
                showBackButton = false,
                showMenuButton = false,
                scrollBehavior = scrollBehavior
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
            // Main Grid Content
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = spacing.sm,
                    end = spacing.sm,
                    top = spacing.md,
                    bottom = spacing.xl + 60.dp // Extra bottom padding for button
                ),
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
                horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { category ->
                    CategoryCard(category, viewModel)
                }
            }

            // Floating Icon Button (bottom-left)
            FloatingActionButton(
                onClick = { viewModel.onClickCart() }, // replace with your cart route
                containerColor = Color(0xFF2E7D32),
                contentColor = Color.White,
                shape = RoundedCornerShape(50),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = 16.dp, end = 16.dp, bottom = 90.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cart), // replace with your cart icon
                    contentDescription = "Go to Cart"
                )
            }

        }
    }

}