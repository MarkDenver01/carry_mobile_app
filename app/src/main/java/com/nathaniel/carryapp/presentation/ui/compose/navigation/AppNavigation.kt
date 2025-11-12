package com.nathaniel.carryapp.presentation.ui.compose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.signin.OtpVerificationScreen
import com.nathaniel.carryapp.presentation.ui.compose.signin.SignInScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(navController: NavHostController) {
//    AnimatedNavHost(
//        navController = navController,
//        startDestination = Routes.SIGN_IN,
//        enterTransition = { fadeIn(animationSpec = tween(300)) },
//        exitTransition = {
//
//            fadeOut(animationSpec = tween(300)) }
//    ) {
//        composable(Routes.SIGN_IN) {
//            SignInScreen(navController = navController)
//        }
//        composable(
//            route = "${Routes.OTP}/{mobileNumber}",
//            enterTransition = {
//                slideInVertically(
//                    initialOffsetY = { it / 2 },
//                    animationSpec = tween(500)
//                ) + fadeIn(animationSpec = tween(300))
//            },
//            exitTransition = {
//                slideOutVertically(
//                    targetOffsetY = { -it / 4 },
//                    animationSpec = tween(400)
//                ) + fadeOut(animationSpec = tween(300))
//            },
//        ) { backStackEntry ->
//            val mobileNumber = backStackEntry.arguments?.getString("mobileNumber") ?: ""
//            OtpVerificationScreen(navController = navController, mobileNumber = mobileNumber)
//        }
//    }
}
