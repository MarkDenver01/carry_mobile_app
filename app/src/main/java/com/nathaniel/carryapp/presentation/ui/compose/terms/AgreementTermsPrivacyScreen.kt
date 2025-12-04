package com.nathaniel.carryapp.presentation.ui.compose.terms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nathaniel.carryapp.presentation.theme.AppSpacing
import com.nathaniel.carryapp.presentation.theme.AppTypography
import com.nathaniel.carryapp.presentation.theme.LocalAppColors
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalAppTypography
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.ui.compose.signin.SignInViewModel
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.utils.DynamicButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgreementTermsPrivacyScreen(
    navController: NavController,
    mobileOrEmail: String? = "",
) {
    val signInViewModel: SignInViewModel = sharedViewModel()
    val colors = LocalAppColors.current
    val spacing = LocalAppSpacing.current
    val typography = LocalAppTypography.current
    val sizes = LocalResponsiveSizes.current

    var selectedTab by remember { mutableStateOf(0) } // 0 = Terms, 1 = Privacy

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "Agreement Terms and Privacy Policy",
                showMenuButton = false,
                showBackButton = true
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        horizontal = sizes.paddingHorizontal,
                        vertical = spacing.md
                    ),
                contentAlignment = Alignment.Center
            ) {
                DynamicButton(
                    onClick = {
                        signInViewModel.addAgreementStatus(
                            email = mobileOrEmail ?: "",
                            agreementStatus = true,
                            navController
                        )
                    },
                    enabled = true,
                    height = sizes.buttonHeight,
                    backgroundColor = colors.primary,
                    pressedBackgroundColor = colors.primaryDark,
                    content = "I agree to these Terms",
                    fontSize = sizes.buttonFontSize
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {

            // ============================
            //         HEADER TITLE
            // ============================
            Text(
                text = "Agree to Wrap and Carry's Terms and Privacy Policy",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = sizes.paddingHorizontal,
                        vertical = spacing.md
                    ),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111111)
            )

            // ============================
            //           TABS
            // ============================
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,
                contentColor = colors.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .height(3.dp),
                        color = colors.primary
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Terms and Conditions",
                            fontSize = typography.caption.fontSize,
                            fontWeight = if (selectedTab == 0) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Privacy Policy",
                            fontSize = typography.caption.fontSize,
                            fontWeight = if (selectedTab == 1) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                )
            }

            // ============================
            //        TAB CONTENT
            // ============================
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = sizes.paddingHorizontal,
                        vertical = spacing.md
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = spacing.xl) // space above bottom button
                ) {
                    if (selectedTab == 0) {
                        TermsContent(typography = typography, spacing = spacing)
                    } else {
                        PrivacyContent(typography = typography, spacing = spacing)
                    }
                }
            }
        }
    }
}

@Composable
private fun TermsContent(
    typography: AppTypography, // adjust type name if different
    spacing: AppSpacing
) {
    Text(
        text = "Terms & Conditions",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF111111)
    )
    Spacer(modifier = Modifier.height(spacing.sm))

    Text(
        text = "By downloading or using Wrap And Carry App, these terms will automatically apply to you. " +
                "Please read them carefully before using the app. You are not allowed to copy or " +
                "modify the app, any part of the app, or our trademarks in any way. You are not " +
                "allowed to attempt to extract the source code of the app, and you should not " +
                "try to translate the app into other languages or make derivative versions.",
        fontSize = typography.body.fontSize,
        color = Color(0xFF444444),
        lineHeight = 20.sp
    )

    Spacer(modifier = Modifier.height(spacing.sm))

    Text(
        text = "Wrap and Carry app remains the property of the company, including all trademarks, " +
                "copyright, database rights, and other intellectual property rights related to it.",
        fontSize = typography.body.fontSize,
        color = Color(0xFF444444),
        lineHeight = 20.sp
    )

    Spacer(modifier = Modifier.height(spacing.sm))

    Text(
        text = "We are committed to ensuring that the app is as useful and efficient as possible. " +
                "For that reason, we reserve the right to make changes to the app or to charge for " +
                "its services at any time and for any reason. We will always make it clear to you " +
                "if any charges apply.",
        fontSize = typography.body.fontSize,
        color = Color(0xFF444444),
        lineHeight = 20.sp
    )

    Spacer(modifier = Modifier.height(spacing.sm))

    Text(
        text = "You are responsible for keeping your device secure and for any activity within " +
                "the app under your account. You must not use the app in any unlawful or harmful way.",
        fontSize = typography.body.fontSize,
        color = Color(0xFF444444),
        lineHeight = 20.sp
    )
}

@Composable
private fun PrivacyContent(
    typography: AppTypography, // adjust type name if different
    spacing: AppSpacing
) {
    Text(
        text = "Privacy Policy",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF111111)
    )
    Spacer(modifier = Modifier.height(spacing.sm))

    Text(
        text = "Confidentiality of your personal data is important to us. This Privacy Policy " +
                "describes what data we collect, how we use it, how we store it, and your rights " +
                "as a data subject.",
        fontSize = typography.body.fontSize,
        color = Color(0xFF444444),
        lineHeight = 20.sp
    )

    Spacer(modifier = Modifier.height(spacing.sm))

    Text(
        text = "We may collect personal data such as your name, mobile number, email address, " +
                "delivery address, order details, and usage information from the app. We use this " +
                "information to operate and improve Wrap and Carry app, process your orders, verify your " +
                "identity, provide customer support, and prevent fraud or misuse.",
        fontSize = typography.body.fontSize,
        color = Color(0xFF444444),
        lineHeight = 20.sp
    )

    Spacer(modifier = Modifier.height(spacing.sm))

    Text(
        text = "We do not sell your personal data. We may share it with service providers such as " +
                "payment processors, SMS gateways, and hosting providers, only as necessary to " +
                "deliver our services and only under appropriate data protection obligations.",
        fontSize = typography.body.fontSize,
        color = Color(0xFF444444),
        lineHeight = 20.sp
    )

    Spacer(modifier = Modifier.height(spacing.sm))

    Text(
        text = "We retain your data only for as long as necessary to fulfill the purposes described " +
                "in this Policy or as required by law. You may request access, correction, or deletion " +
                "of your personal data, subject to applicable legal requirements.",
        fontSize = typography.body.fontSize,
        color = Color(0xFF444444),
        lineHeight = 20.sp
    )

    Spacer(modifier = Modifier.height(spacing.sm))

    Text(
        text = "By continuing to use Wrap and Carry app, you acknowledge that you have read and understood " +
                "these terms and this Privacy Policy.",
        fontSize = typography.body.fontSize,
        color = Color(0xFF444444),
        lineHeight = 20.sp
    )
}
