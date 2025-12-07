package com.nathaniel.carryapp.presentation.ui.compose.orders.reorder

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.enum.AlertType
import com.nathaniel.carryapp.domain.model.ProductRack
import com.nathaniel.carryapp.domain.model.toShopProduct
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.cart.CartViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.*
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.ui.state.LoginUiAction
import com.nathaniel.carryapp.presentation.ui.state.LoginUiEvent
import com.nathaniel.carryapp.presentation.utils.SweetAlertDialog

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReOrderScreen(navController: NavController) {
    val orderViewModel: OrderViewModel = sharedViewModel()
    val reorderViewModel: ReorderViewModel = sharedViewModel()
    val cartViewModel: CartViewModel = sharedViewModel()

    val reorderHistory by reorderViewModel.history.collectAsState()
    val cartCount by cartViewModel.cartCount.collectAsState()

    var showRestoreAllConfirm by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = (screenWidth / 2.2f)   // ✅ Slightly smaller = more breathing room
    val cardHeight = 330.dp
    val bannerCount by orderViewModel.bannerCount.collectAsState()

    val racks = remember(reorderHistory) {
        reorderHistory
            .groupBy { it.categoryName.ifBlank { "Others" } }
            .map { (category, items) ->
                ProductRack(
                    title = category,
                    products = items.map { it.toShopProduct() }
                )
            }
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

                is LoginUiAction.ShowToast -> "ReOrder"
                null -> Unit
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFFF7F8FA),
        topBar = {
            ShopHeader(
                notifications = 0,
                cartCount = cartCount,
                onCartClick = { navController.navigate(Routes.CART) },
                onNotificationClick = {}
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

        if (reorderHistory.isEmpty()) {
            EmptyReorderState(Modifier.padding(inner))
        } else {

            LazyColumn(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = 96.dp,
                    top = 12.dp
                )
            ) {

                // ✅ RESTORE ALL BUTTON (WITH SIDE PADDING)
                item {
                    Button(
                        onClick = { showRestoreAllConfirm = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF118B3C)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Restore All With Quantities",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                }

                // ✅ CATEGORY SECTIONS
                items(racks) { rack ->

                    // ✅ HEADER WITH PROPER SIDE PADDING
                    SectionHeader(
                        title = rack.title,
                        actionText = "Add All",
                        onActionClick = {
                            reorderViewModel.reorderAgain(
                                reorderHistory.filter { it.categoryName == rack.title }
                            )
                        }
                    )

                    Spacer(Modifier.height(8.dp))

                    // ✅ HORIZONTAL SCROLL WITH LEFT & RIGHT PADDING
                    LazyRow(
                        contentPadding = PaddingValues(
                            start = 16.dp,   // ✅ LEFT SPACE
                            end = 16.dp     // ✅ RIGHT SPACE
                        ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp) // ✅ GAP BETWEEN CARDS
                    ) {
                        items(rack.products) { p ->

                            ProductCard(
                                cardWidth = cardWidth,
                                cardHeight = cardHeight,
                                imageUrl = p.imageUrl,
                                name = p.name,
                                weight = p.weight,
                                sold = p.sold,
                                price = p.price,
                                expiryDate = p.expiryDate,
                                onFavorite = {},
                                onAdd = {
                                    cartViewModel.addProductOriginalDomain(p.id)
                                },
                                onMinus = {
                                    cartViewModel.removeProductOriginalDomain(p.id)
                                },
                                onRestore = {},
                                onDeduct = {},
                                onDetailClick = {
                                    navController.navigate(
                                        "${Routes.PRODUCT_DETAIL}/${p.id}"
                                    )
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(28.dp)) // ✅ SPACE BETWEEN CATEGORIES
                }
            }

            // ✅ CONFIRM DIALOG FOR RESTORE ALL
            if (showRestoreAllConfirm) {
                SweetAlertDialog(
                    type = AlertType.WARNING,
                    title = "Restore All Orders?",
                    message = "This will add all past purchased items back to your cart.",
                    show = true,
                    confirmText = "Yes, Restore",
                    dismissText = "Cancel",
                    onConfirm = {
                        showRestoreAllConfirm = false
                        reorderViewModel.restoreAllWithQuantities {
                            cartViewModel.refreshCartFromOutside()
                        }
                    },
                    onDismiss = {
                        showRestoreAllConfirm = false
                    }
                )
            }
        }
    }
}


@Composable
fun EmptyReorderState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_order_history),
            contentDescription = null,
            modifier = Modifier.size(160.dp),
            tint = Color.Gray
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "No Order History Yet",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Your completed purchases will appear here for fast re-ordering.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}
