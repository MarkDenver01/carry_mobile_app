package com.nathaniel.carryapp.presentation.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.navigation.initialGraph
import com.nathaniel.carryapp.navigation.orderGraph
import com.nathaniel.carryapp.navigation.signInGraph
import com.nathaniel.carryapp.presentation.theme.CarryappTheme
import com.nathaniel.carryapp.presentation.ui.compose.orders.account.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarryappTheme {
                val navController = rememberNavController()

                //AppNavigation(navController = navController)

                NavHost(
                    navController = navController,
                    startDestination = Routes.INITIAL
                ) {
                    initialGraph(navController)
                    signInGraph(navController)
                    orderGraph(navController)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val uri = intent.data ?: return

        when (uri.host) {

            "cashin_success" -> {
                // 🔥 Get CustomerViewModel tied to Activity
                val vm = ViewModelProvider(this)[CustomerViewModel::class.java]

                // 🔥 Refresh wallet immediately
                vm.refreshWallet()

                // 🔥 Navigate to success screen
                navController.navigate(Routes.CASH_IN_SUCCESS) {
                    launchSingleTop = true
                }
            }

            "cashin_failed" -> {
                navController.navigate(Routes.CASH_IN_FAILED) {
                    launchSingleTop = true
                }
            }
        }
    }
}