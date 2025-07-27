package com.nathaniel.carryapp.presentation.ui.compose.voucher

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.model.Voucher
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.ui.compose.voucher.card.VoucherCard
import com.nathaniel.carryapp.presentation.utils.AuthTextField
import com.nathaniel.carryapp.presentation.utils.DynamicButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherScreen(
    navController: NavController,
    viewModel: VoucherViewModel = hiltViewModel()
) {
    val navigateTo by viewModel.navigateTo.collectAsState()
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var redeemCode by remember { mutableStateOf("") }
    var selectedVoucher by remember { mutableStateOf<Voucher?>(null) }

    // Replace with actual state from viewModel later
    val voucherList by remember {
        mutableStateOf(
            listOf(
                Voucher("200% Discount", "Get 200% off your next order", "200%"),
                Voucher("Free Delivery", "Enjoy free shipping for orders above ₱500", "Free")
            )
        )
    }

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "My Vouchers",
                showBackButton = true,
                showMenuButton = false,
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF2E7D32), Color(0xFF4CAF50))
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = spacing.lg, vertical = spacing.xl)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AuthTextField(
                        value = redeemCode,
                        onValueChange = { redeemCode = it },
                        placeholder = "Redeem Code",
                        leadingIconPainter = painterResource(id = R.drawable.ic_voucher),
                        fontSize = 13.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = spacing.sm)
                    )

                    DynamicButton(
                        onClick = { /* handle redeem */ },
                        modifier = Modifier
                            .height(sizes.buttonHeight)
                            .width(100.dp),
                        fontSize = 13.sp,
                        backgroundColor = Color(0xFF2E7D32),
                        content = "REDEEM"
                    )
                }
                Spacer(modifier = Modifier.height(spacing.sm))

                Divider(
                    color = Color.White,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(spacing.lg))

                if (voucherList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(spacing.sm)
                    ) {
                        items(voucherList) { voucher ->
                            VoucherCard(
                                voucher,
                                onClick = { selectedVoucher = it }
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = spacing.xl),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_voucher),
                            contentDescription = "No Vouchers",
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(spacing.md))
                        Text(
                            text = "You currently have no active vouchers.",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                if (selectedVoucher != null) {
                    AlertDialog(
                        onDismissRequest = { selectedVoucher = null },
                        confirmButton = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { selectedVoucher = null }) {
                                    Text("Cancel", color = Color.Gray)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                TextButton(onClick = {
                                    viewModel.applyVoucher(selectedVoucher!!)
                                    selectedVoucher = null
                                }) {
                                    Text("Apply", color = Color(0xFF2E7D32))
                                }
                            }
                        },
                        title = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // 👇 Replace with your actual voucher image
                                Image(
                                    painter = painterResource(id = R.drawable.ic_voucher), // Your image here
                                    contentDescription = "Voucher Image",
                                    modifier = Modifier
                                        .height(100.dp)
                                        .padding(bottom = 8.dp)
                                )
                                Text(
                                    text = selectedVoucher!!.title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        },
                        text = {
                            Text(
                                text = selectedVoucher!!.description,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        containerColor = Color.White,
                        tonalElevation = 6.dp
                    )
                }



            }
        }
    }
}