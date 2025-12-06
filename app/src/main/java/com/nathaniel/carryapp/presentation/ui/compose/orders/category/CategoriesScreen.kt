package com.nathaniel.carryapp.presentation.ui.compose.orders.category

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.mapper.ProductMapper.toShopProduct
import com.nathaniel.carryapp.domain.model.Category
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopBottomBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopHeader
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopSearchBar
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.ui.state.LoginUiAction
import com.nathaniel.carryapp.presentation.ui.state.LoginUiEvent

@Composable
fun RotatingLoader(size: Int = 34, color: Color = Color(0xFF118B3C)) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotate")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing)
        ),
        label = "angle"
    )

    Icon(
        painter = painterResource(R.drawable.ic_broken_image), // ⚠️ Replace with your loader icon
        contentDescription = null,
        tint = color,
        modifier = Modifier
            .size(size.dp)
            .rotate(angle)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavController
) {
    val orderViewModel: OrderViewModel = sharedViewModel()
    val products by orderViewModel.products.collectAsState()

    val shopProducts = products.map { it.toShopProduct() }

    val categories: List<Category> = shopProducts
        .groupBy { it.categoryName }
        .map { (categoryName, items) ->
            Category(name = categoryName, imageUrl = items.firstOrNull()?.imageUrl)
        }
        .sortedBy { it.name }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    LaunchedEffect(orderViewModel.loginUiAction) {
        orderViewModel.loginUiAction.collect { action ->
            when (action) {
                is LoginUiAction.Navigate -> {
                    navController.navigate(action.route) {
                        popUpTo(Routes.SIGN_IN) { inclusive = false }
                    }
                    orderViewModel.resetLoginAction()
                }
                is LoginUiAction.ShowToast -> Unit
                null -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            ShopHeader(
                notifications = 12,
                cartCount = 15,
                onCartClick = {
                    navController.navigate(Routes.CART) {
                        popUpTo(Routes.ORDERS) { inclusive = true }
                    }
                },
                onNotificationClick = {}
            )
        },
        bottomBar = {
            ShopBottomBar(
                selectedIndex = orderViewModel.selectedTab.collectAsState().value,
                onItemSelected = { orderViewModel.updateSelectedTab(it) },
                onHome = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnHomeClicked) },
                onCategories = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnCategoriesClicked) },
                onReorder = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnReorderClicked) },
                onAccount = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnAccountClicked) }
            )
        },
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            ShopSearchBar(
                hint = "I'm looking for…",
                onSearch = {}
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "All Categories",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0E1F22),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = screenHeight * 2),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 96.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(categories) { category ->
                    CategoryCard(
                        title = category.name,
                        imageUrl = category.imageUrl,
                        onClick = {
                            navController.navigate("${Routes.SORT_PRODUCT_BY_CATEGORY}/${category.name}") {
                                popUpTo(Routes.CATEGORIES) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun CategoryCard(
    title: String,
    imageUrl: String?,
    onClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var showLoader by remember { mutableStateOf(true) }

    // Delay hide logic
    LaunchedEffect(isLoading) {
        if (!isLoading) {
            kotlinx.coroutines.delay(1000)
            showLoader = false
        } else {
            showLoader = true
        }
    }

    Column(
        modifier = Modifier
            .width(100.dp)
            .padding(4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .background(Color(0xFFF1F5F6)),
            contentAlignment = Alignment.Center
        ) {

            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                onLoading = {
                    isLoading = true
                    isError = false
                    showLoader = true
                },
                onSuccess = {
                    isLoading = false
                    isError = false
                },
                onError = {
                    isLoading = false
                    isError = true
                }
            )

            // ⭐ LOADING ROTATION
            if (showLoader) {
                RotatingLoader(size = 28)
            }

            // ❌ ERROR ICON
            if (isError && !showLoader) {
                Icon(
                    painter = painterResource(R.drawable.ic_broken_image),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
