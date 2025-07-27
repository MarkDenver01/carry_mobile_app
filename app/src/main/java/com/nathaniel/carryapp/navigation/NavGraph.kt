package com.nathaniel.carryapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nathaniel.carryapp.presentation.ui.compose.account.change_password.ChangePasswordScreen
import com.nathaniel.carryapp.presentation.ui.compose.account.display_profile.DisplayProfileScreen
import com.nathaniel.carryapp.presentation.ui.compose.account.update_profile.UpdateProfileScreen
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.DashboardScreen
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.delivery.DeliveryScreen
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.pickup.PickupScreen
import com.nathaniel.carryapp.presentation.ui.compose.initial.InitialScreen
import com.nathaniel.carryapp.presentation.ui.compose.membership.apply.SukiMembershipScreen
import com.nathaniel.carryapp.presentation.ui.compose.membership.payment.SubscriptionScreen
import com.nathaniel.carryapp.presentation.ui.compose.membership.suki_badge.VerifiedSukiCard
import com.nathaniel.carryapp.presentation.ui.compose.membership.view.ViewMembershipScreen
import com.nathaniel.carryapp.presentation.ui.compose.signin.SignInScreen
import com.nathaniel.carryapp.presentation.ui.compose.signup.SignUpScreen
import com.nathaniel.carryapp.presentation.ui.compose.voucher.VoucherScreen

fun NavGraphBuilder.initialGraph(navController: NavController) {
    composable(Routes.INITIAL) {
        InitialScreen(navController = navController)
    }
}

fun NavGraphBuilder.dashboardGraph(navController: NavController) {
    composable(Routes.DASHBOARD) {
        DashboardScreen(navController = navController)
    }
    composable(Routes.DELIVERY) {
        DeliveryScreen(navController = navController)
    }
    composable(Routes.PICKUP) {
        PickupScreen(navController = navController)
    }
    composable(Routes.BADGE_DETAILS) {
        ViewMembershipScreen(navController = navController)
    }

    composable(Routes.VOUCHER) {
        VoucherScreen(navController = navController)
    }
}

fun NavGraphBuilder.signUpGraph(navController: NavController) {
    composable(Routes.SIGN_UP) {
        SignUpScreen(navController = navController)
    }
}

fun NavGraphBuilder.signInGraph(navController: NavController) {
    composable(Routes.SIGN_IN) {
        SignInScreen(navController = navController)
    }
}

fun NavGraphBuilder.displayUserProfileGraph(navController: NavController) {
    composable(Routes.DISPLAY_PROFILE) {
        DisplayProfileScreen(navController = navController)
    }

    composable(Routes.UPDATE_PROFILE) {
        UpdateProfileScreen(navController = navController)
    }

    composable(Routes.CHANGE_PASSWORD) {
        ChangePasswordScreen(navController = navController)
    }
}

fun NavGraphBuilder.membershipGraph(navController: NavController) {
    composable(Routes.MEMBERSHIP) {
        SukiMembershipScreen(navController = navController)
    }

    composable(Routes.SUBSCRIPTION) {
        SubscriptionScreen(navController = navController)
    }
}