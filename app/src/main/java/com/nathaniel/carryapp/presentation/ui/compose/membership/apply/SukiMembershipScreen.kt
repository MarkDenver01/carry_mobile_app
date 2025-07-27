package com.nathaniel.carryapp.presentation.ui.compose.membership.apply

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.membership.MembershipViewModel
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.utils.DynamicButton
import com.nathaniel.carryapp.presentation.utils.responsiveDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SukiMembershipScreen(
    navController: NavController,
    viewModel: MembershipViewModel = hiltViewModel()
) {
    val navigateTo by viewModel.navigateTo.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current
    val topSpacing = responsiveDp(34f)
    val outerPadding = responsiveDp(16f)

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "Suki Membership",
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
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF2E7D32), Color(0XFF4CAF50))
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(topSpacing))

                // Header Title
                Text(
                    text = "WHAT IS SUKI MEMBERSHIP?",
                    fontSize = sizes.titleFontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .background(Color(0xFFCDDC39), RoundedCornerShape(32.dp))
                        .padding(horizontal = spacing.lg, vertical = spacing.sm)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = outerPadding)
                ) {

                    Spacer(modifier = Modifier.height(spacing.md))

                    // Info Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            MembershipInfoParagraph()
                        }
                    }

                    Spacer(modifier = Modifier.height(spacing.md))

                    // Highlight Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFCDDC39)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(spacing.md)) {
                            Text(
                                text = "Sign up for the Suki Membership now and start collecting rewards! 🎉",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Start to avail with only ₱150!",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(spacing.md))

                    DynamicButton(
                        onClick = { viewModel.onAvailableNow() },
                        height = sizes.buttonHeight,
                        fontSize = sizes.buttonFontSize,
                        backgroundColor = Color(0xFF2E7D32),
                        content = "Avail Now!"
                    )

                    Spacer(modifier = Modifier.height(spacing.xl))
                }
            }
        }
    }
}

@Composable
fun MembershipInfoParagraph() {
    Text(
        text = buildAnnotatedString {
            append("Become a suki and make every purchase worth it!\n")
            append("With our Suki Avail Membership, every ₱500 worth of products you buy earns you points you can redeem.\n\n")

            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("How to Earn Points:\n")
            }
            append("• Every ₱50 worth of products = 5 points\n")
            append("• Minimum ₱500 to start earning\n\n")

            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Redeem Your Points:\n")
            }
            append("• 1,000 points = ₱500 groceries\n")
            append("• 2,000 points = ₱1,000 groceries\n")
            append("• 3,000 points = ₱1,500 groceries + FREE delivery\n\n")

            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Why Avail?\n")
            }
            append("✔ Save on groceries\n")
            append("✔ Maximize every purchase\n")
            append("✔ Get rewarded while shopping\n")
        },
        fontSize = 14.sp,
        color = Color(0xFF2E7D32)
    )
}

