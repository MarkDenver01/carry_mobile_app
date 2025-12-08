package com.nathaniel.carryapp.navigation

import SignUpSuccessScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nathaniel.carryapp.presentation.ui.compose.driver.DriverMainScreen
import com.nathaniel.carryapp.presentation.ui.compose.initial.InitialScreen
import com.nathaniel.carryapp.presentation.ui.compose.notifications.NotificationHistoryScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.location.DeliveryAreaScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.account.AccountScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.account.CashInFailedScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.account.CashInScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.account.CashInSuccessScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.account.CustomerRegistrationScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.cart.CartScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.cart.CheckoutScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.category.CategoriesScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.category.ListCategoryProductScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.location.DeliveryAddressScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.location.LocationConfirmationScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.main_screen.OrderDetailScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.main_screen.OrderReceiptScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.main_screen.OrderScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.main_screen.OrdersListScreen
import com.nathaniel.carryapp.presentation.ui.compose.orders.product.ProductDetailRouter
import com.nathaniel.carryapp.presentation.ui.compose.orders.reorder.ReOrderScreen
import com.nathaniel.carryapp.presentation.ui.compose.promo.PromoBannerListScreen
import com.nathaniel.carryapp.presentation.ui.compose.signin.OtpVerificationScreen
import com.nathaniel.carryapp.presentation.ui.compose.signin.SignInScreen
import com.nathaniel.carryapp.presentation.ui.compose.terms.AgreementTermsPrivacyScreen

fun NavGraphBuilder.initialGraph(navController: NavController) {
    composable(Routes.INITIAL) {
        InitialScreen(navController = navController)
    }
}

fun NavGraphBuilder.signInGraph(navController: NavController) {
    composable(Routes.SIGN_IN) {
        SignInScreen(navController = navController)
    }
    composable(
        route = "${Routes.OTP}/{mobileNumber}"
    ) { backStackEntry ->
        val mobile = backStackEntry.arguments?.getString("mobileNumber") ?: ""
        OtpVerificationScreen(
            navController = navController,
            mobileNumber = mobile
        )
    }

    composable(Routes.DELIVERY_AREA) {
        DeliveryAreaScreen(navController = navController)
    }

    composable(Routes.DELIVERY_ADDRESS) {
        DeliveryAddressScreen(navController = navController)
    }

    composable(Routes.LOCATION_CONFIRMATION_SCREEN) {
        LocationConfirmationScreen(navController = navController)
    }

    composable(
        route = "${Routes.AGREEMENT_TERMS_PRIVACY}/{mobileOrEmail}"
    ) { backStackEntry ->
        val mobileOrEmail = backStackEntry.arguments?.getString("mobileOrEmail") ?: ""
        AgreementTermsPrivacyScreen(
            navController = navController,
            mobileOrEmail = mobileOrEmail,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.orderGraph(navController: NavController) {
    composable(Routes.ORDERS) {
        OrderScreen(navController = navController)
    }

    composable(
        route = "${Routes.PRODUCT_DETAIL}/{productId}"
    ) { backStackEntry ->
        val productId = backStackEntry.arguments?.getString("productId")?.toLongOrNull()
        ProductDetailRouter(
            navController = navController,
            productId = productId
        )
    }

    composable(Routes.CATEGORIES) {
        CategoriesScreen(navController = navController)
    }

    composable(
        route = "${Routes.SORT_PRODUCT_BY_CATEGORY}/{categoryName}"
    ) { backStackEntry ->

        val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""

        ListCategoryProductScreen(
            navController = navController,
            categoryName = categoryName
        )
    }

    composable(Routes.ACCOUNT) {
        AccountScreen(navController = navController)
    }

    composable(Routes.CASH_IN) {
        CashInScreen(navController = navController)
    }

    composable(Routes.CUSTOMER_DETAIL) {
        CustomerRegistrationScreen(navController = navController)
    }

    composable(Routes.CASH_IN_SUCCESS) {
        CashInSuccessScreen(
            navController = navController
        )
    }

    composable(Routes.CASH_IN_FAILED) {
        CashInFailedScreen(navController = navController)
    }

    composable(Routes.CART) {
        CartScreen(navController = navController)
    }

    composable(Routes.CHECKOUT) {
        CheckoutScreen(navController = navController)
    }

    composable(Routes.CUSTOMER_REG_SUCCESS) {
        SignUpSuccessScreen(navController = navController)
    }

    composable(Routes.REORDER) {
        ReOrderScreen(navController = navController)
    }

    // TODO -recreate the UI for driver main screen
    composable(Routes.DRIVER_MAIN_SCREEN) {
        DriverMainScreen(navController = navController)
    }

    composable(Routes.PROMO_BANNERS) {
        PromoBannerListScreen(navController)
    }

    composable(Routes.NOTIFICATIONS) {
        NotificationHistoryScreen(navController)
    }

    composable(Routes.ORDER_LIST) {
        OrdersListScreen(navController)
    }

    composable(
        route = "${Routes.ORDER_DETAILS}/{orderId}"
    ) { backStack ->
        val orderId = backStack.arguments?.getString("orderId")?.toLongOrNull()
        OrderDetailScreen(
            navController = navController,
            orderId = orderId
        )
    }

    composable(
        route = "${Routes.ORDER_RECEIPT}/{orderId}"
    ) { backStack ->
        val orderId = backStack.arguments?.getString("orderId")?.toLongOrNull()
        OrderReceiptScreen(
            navController = navController,
            orderId = orderId
        )
    }
}