package com.nathaniel.carryapp.presentation.ui.compose.orders.category

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.mapper.ProductMapper.toShopProduct
import com.nathaniel.carryapp.domain.model.ProductRack
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.cart.CartViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.*
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.ui.state.LoginUiAction
import com.nathaniel.carryapp.presentation.ui.state.LoginUiEvent
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListCategoryProductScreen(
    navController: NavController,
    categoryName: String,
) {
    val orderViewModel: OrderViewModel = sharedViewModel()
    val cartViewModel: CartViewModel = sharedViewModel()

    val products by orderViewModel.products.collectAsState()
    val customerSession by orderViewModel.customerSession.collectAsState()
    val cartCount by cartViewModel.cartCount.collectAsState()
    val error by orderViewModel.error.collectAsState()

    val shopProducts = products.map { it.toShopProduct() }

    // ======================================================
    // ⭐ SAME EXPIRY LOGIC USED IN ORDERSCREEN
    // ======================================================
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun daysLeft(exp: String?): Int? {
        return try {
            if (exp.isNullOrBlank()) return null
            val parsed = exp.substringBefore(" ")
            ChronoUnit.DAYS.between(today, LocalDate.parse(parsed, formatter)).toInt()
        } catch (e: Exception) {
            Timber.e("Expiry parse error category-screen: ${e.message}")
            null
        }
    }

    // ======================================================
    // ⭐ Step 1: Identify PROMO PRODUCTS (0–60 days)
    // ======================================================
    val promoProducts = shopProducts.filter {
        val d = daysLeft(it.expiryDate)
        d != null && d in 0..60
    }

    val promoIds = promoProducts.map { it.id }.toSet()

    // ======================================================
    // ⭐ Step 2: NORMAL categories WITHOUT promo products
    // ======================================================
    val normalProducts = shopProducts.filter { it.id !in promoIds }

    val normalRacks = normalProducts
        .groupBy { it.categoryName }
        .map { (cat, list) -> ProductRack(cat, list) }
        .sortedBy { it.title }

    // ======================================================
    // ⭐ Step 3: FINAL CATEGORY LIST
    // PROMO CATEGORY WILL ALWAYS APPEAR FIRST
    // ======================================================
    val finalCategoryList = buildList {
        if (promoProducts.isNotEmpty()) {
            add(ProductRack("Promo Products", promoProducts))
        }
        addAll(normalRacks)
    }

    // ======================================================
    // ⭐ Selected Category (coming from OrderScreen)
    // ======================================================
    var selectedCategory by remember { mutableStateOf(categoryName) }

    // If categoryName = real category, OK
    // If categoryName happens to be promo, also OK

    val selectedProducts = remember(selectedCategory, finalCategoryList) {
        finalCategoryList.find { it.title == selectedCategory }?.products ?: emptyList()
    }

    LaunchedEffect(products) {
        cartViewModel.setProducts(products)
    }

    // Handle navigation
    LaunchedEffect(Unit) {
        orderViewModel.loginUiAction.collect { action ->
            when (action) {
                is LoginUiAction.Navigate -> {
                    navController.navigate(action.route) {
                        popUpTo(Routes.SIGN_IN) { inclusive = false }
                    }
                    orderViewModel.resetLoginAction()
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFFF7F8FA),
        topBar = {
            ShopHeader(
                notifications = 12,
                cartCount = cartCount,
                onCartClick = { navController.navigate(Routes.CART) },
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
        }
    ) { inner ->

        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {

            ShopSearchBar(
                hint = "I'm Smart Search AI, looking for…",
                onSearch = { query ->
                    customerSession?.customer?.customerId?.let { cid ->
                        if (query.isNotBlank()) {
                            orderViewModel.recordUserInteraction(cid, query)
                        }
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            PromoBanner(
                banners = listOf(
                    BannerItem(R.drawable.banner_wrap_n_carry),
                    BannerItem(R.drawable.banner_wrap_n_carry),
                    BannerItem(R.drawable.banner_wrap_n_carry)
                )
            )

            Spacer(Modifier.height(12.dp))

            if (error != null) {
                Text("⚠ Error loading products: $error", color = Color.Red)
            }

            // ⭐ FULL CATEGORY UI: Left menu + products
            CategoryProduct(
                racks = listOf(ProductRack(selectedCategory, selectedProducts)),
                originalRacks = finalCategoryList,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                onProductClick = { id ->
                    navController.navigate("${Routes.PRODUCT_DETAIL}/$id")
                },
                onAdd = { id -> cartViewModel.addProductOriginalDomain(id) },
                onMinus = { id -> cartViewModel.removeProductOriginalDomain(id) }
            )
        }
    }
}
