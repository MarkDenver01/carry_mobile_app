package com.nathaniel.carryapp.presentation.ui.compose.orders.main_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import com.nathaniel.carryapp.presentation.utils.PromoPopupDialog
import com.nathaniel.carryapp.presentation.utils.shouldShowPromoToday
import kotlinx.coroutines.delay
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OrderScreen(
    navController: NavController,
) {
    val orderViewModel: OrderViewModel = sharedViewModel()
    val cartViewModel: CartViewModel = sharedViewModel()

    val isRefreshing by orderViewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            orderViewModel.refreshProducts() // ✅ USE YOUR VM FUNCTION
        }
    )

    val products by orderViewModel.products.collectAsState()
    val error by orderViewModel.error.collectAsState()
    val cartCount by cartViewModel.cartCount.collectAsState()
    val customerSession by orderViewModel.customerSession.collectAsState()
    val unreadCount by orderViewModel.unreadCount.collectAsState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = (screenWidth / 2) - 24.dp
    val cardHeight = 330.dp

    val context = LocalContext.current
    var showPromoPopup by remember { mutableStateOf(shouldShowPromoToday(context)) }

    // DOMAIN -> UI
    val shopProducts = products.map { it.toShopProduct() }
    val searchQuery by orderViewModel.searchQuery.collectAsState()

    // =====================================================
    // 🔍 GLOBAL SEARCH FILTER
    // =====================================================
    val filteredProducts = remember(searchQuery, shopProducts) {
        if (searchQuery.isBlank()) {
            shopProducts
        } else {
            val q = searchQuery.lowercase()

            shopProducts.filter { p ->
                p.name.lowercase().contains(q) ||
                        p.categoryName.lowercase().contains(q)
            }
        }
    }

    // =====================================================
    // PRODUCT BANNER COUNT
    // =====================================================
    val bannerCount by orderViewModel.bannerCount.collectAsState()
    val productBanners by orderViewModel.productBanners.collectAsState()

    // =====================================================
    // 🔥 EXPIRY CALC (SAME LOGIC AS ProductCard)
    // =====================================================
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun daysLeft(expiryDate: String?): Int? {
        isRefreshing
        return try {
            if (!expiryDate.isNullOrBlank()) {
                val dateOnly = expiryDate.substringBefore(" ")
                ChronoUnit.DAYS.between(
                    today,
                    LocalDate.parse(dateOnly, formatter)
                ).toInt()
            } else null
        } catch (e: Exception) {
            Timber.e("Expiry parse error in OrderScreen: ${e.message}")
            null
        }
    }

    // Optional: debug log to verify what is considered promo
    LaunchedEffect(filteredProducts) {
        filteredProducts.forEach { p ->
            Timber.d(
                "PROMO_CHECK :: name=%s expiry=%s daysLeft=%s",
                p.name,
                p.expiryDate,
                daysLeft(p.expiryDate)
            )
        }
    }

    // =====================================================
    // ⭐ STEP 1: GET ALL PROMO PRODUCTS (<= 60 DAYS)
    // =====================================================
    val promoProducts = filteredProducts.filter { p ->
        val d = daysLeft(p.expiryDate)
        d != null && d in 0..60   // 0–60 days before expiry
    }

    Timber.e("xxxxxx prod: ${promoProducts.size}")

    // =====================================================
    // ⭐ STEP 2: REMOVE PROMO PRODUCTS FROM NORMAL LIST
    // =====================================================
    val promoIds = promoProducts.map { it.id }.toSet()

    val nonPromoProducts = filteredProducts.filter { p ->
        p.id !in promoIds
    }

    // =====================================================
    // ⭐ STEP 3: BUILD NORMAL CATEGORY RACKS (WITHOUT PROMOS)
    // =====================================================
    val normalRacks = nonPromoProducts
        .groupBy { it.categoryName }
        .map { (categoryName, productList) ->
            ProductRack(
                title = categoryName,
                products = productList
            )
        }
        .filter { it.products.isNotEmpty() }  // hide empty categories

    // =====================================================
    // ⭐ STEP 4: FINAL RACK ORDER → PROMO FIRST, THEN NORMAL
    // =====================================================
    val racks = buildList {
        if (promoProducts.isNotEmpty()) {
            // sort promo by nearest to expire (optional pero useful)
            add(
                ProductRack(
                    title = "Promo Products",
                    products = promoProducts.sortedBy { daysLeft(it.expiryDate) ?: Int.MAX_VALUE }
                )
            )
        }
        addAll(normalRacks)
    }

    // =====================================================
    // VIEWMODEL EFFECTS
    // =====================================================
    LaunchedEffect(products) {
        cartViewModel.setProducts(products)
    }

    LaunchedEffect(orderViewModel.loginUiAction) {
        orderViewModel.loginUiAction.collect { action ->
            when (action) {
                is LoginUiAction.Navigate -> {
                    navController.navigate(action.route) {
                        popUpTo(Routes.SIGN_IN) { inclusive = false }
                    }
                    orderViewModel.resetLoginAction()
                }

                is LoginUiAction.ShowToast -> "Order"
                null -> Unit
            }
        }
    }

    // ⚠ Handle API errors
    error?.let {
        Text("Error loading products: $it", color = Color.Red)
    }

    // =====================================================
    // UI START
    // =====================================================
    Scaffold(
        containerColor = Color(0xFFF7F8FA),
        topBar = {
            ShopHeader(
                notifications = unreadCount,
                cartCount = cartCount,
                onCartClick = { navController.navigate(Routes.CART) },
                onNotificationClick = { navController.navigate(Routes.NOTIFICATIONS) }
            )
        },
        bottomBar = {
            ShopBottomBar(
                offersCount = bannerCount,
                selectedIndex = orderViewModel.selectedTab.collectAsState().value,
                onItemSelected = { orderViewModel.updateSelectedTab(it) },
                onHome = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnHomeClicked) },
                onCategories = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnCategoriesClicked) },
                onReorder = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnReorderClicked) },
                onAccount = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnAccountClicked) },
                onPromo = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnPromoClicked) }
            )
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                // 🔍 Search + Banner
                item {
                    Column {
                        ShopSearchBar(
                            hint = "I'm Smart Search AI, looking for…",
                            onSearch = { query ->
                                orderViewModel.updateSearchQuery(query)
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                        PromoBanner(banners = productBanners)
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // =====================================================
                // CATEGORY SECTIONS (PROMO FIRST, THEN NORMAL RACKS)
                // =====================================================
                items(racks.size) { index ->
                    val rack = racks[index]

                    // Section Header
                    SectionHeader(
                        title = rack.title,
                        actionText = "View More",
                        onActionClick = {
                            navController.navigate("${Routes.SORT_PRODUCT_BY_CATEGORY}/${rack.title}") {
                                popUpTo(Routes.ORDERS) { inclusive = false }
                            }
                        }
                    )

                    Spacer(Modifier.height(10.dp))

                    // 🔥 HORIZONTAL LIST OF PRODUCTS
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(rack.products, key = { it.id }) { p ->

                            val qty by cartViewModel.getQty(p.id)
                                .collectAsState(initial = 0)

                            ProductCard(
                                cardWidth = cardWidth,
                                cardHeight = cardHeight,
                                nameMaxLines = 2,
                                imageUrl = p.imageUrl,
                                name = p.name,
                                weight = p.weight,
                                sold = p.sold,
                                price = p.price,
                                expiryDate = p.expiryDate,
                                qty = qty,
                                onFavorite = {},
                                onAdd = {
                                    cartViewModel.addProductOriginalDomain(p.id)

                                    val customerId = customerSession?.customer?.customerId
                                    if (customerId != null) {
                                        orderViewModel.recordUserInteraction(customerId, p.name)
                                    }
                                },
                                onMinus = {
                                    cartViewModel.removeProductOriginalDomain(p.id)
                                },
                                onRestore = {},
                                onDeduct = {},
                                onDetailClick = {
                                    val customerId = customerSession?.customer?.customerId
                                    if (customerId != null) {
                                        orderViewModel.recordUserInteraction(customerId, p.name)
                                    }
                                    navController.navigate("${Routes.PRODUCT_DETAIL}/${p.id}")
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }

            PromoPopupDialog(
                isVisible = showPromoPopup,
                promoImage = R.drawable.ic_promo_per_day, // 🔁 Replace with your banner
                onDismiss = {
                    showPromoPopup = false
                }
            )

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
