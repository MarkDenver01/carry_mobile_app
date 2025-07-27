package com.nathaniel.carryapp.presentation.ui.compose.account.display_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.domain.enum.ButtonVariants
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.account.AccountViewModel
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.utils.DynamicButton
import com.nathaniel.carryapp.presentation.utils.IconButton
import com.nathaniel.carryapp.presentation.utils.responsiveDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayProfileScreen(
    navController: NavController,
    viewModel: AccountViewModel = hiltViewModel()
) {

    val navigateTo by viewModel.navigateTo.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current
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
                title = "User Profile",
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
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF2E7D32), Color(0xFF4CAF50))
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(topSpacing))

                // Profile Icon and Name
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color(0xFF2E7D32), CircleShape)
                            .background(Color.Black.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("MT", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(18.dp)
                                .background(Color.White, shape = CircleShape)
                                .padding(2.dp)
                        )
                    }

                    Text(
                        "Mang Tani",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = outerPadding)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Basic Information",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )

                        IconButton(
                            onClick = { viewModel.onEditAccount() },
                             Modifier.width(54.dp),
                            height = 34.dp,
                            fontSize = 13.sp,
                            content = "Edit",
                            variant = ButtonVariants.Outlined
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Info Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            ProfileField("FULL NAME", "User")
                            Divider()
                            ProfileField("MOBILE NUMBER", "09453678453")
                            Divider()
                            ProfileField("EMAIL ADDRESS", "N/A")
                            Divider()
                            ProfileField("ADDRESS", "N/A") // Note: Typo retained for accuracy
                            Divider()
                            ProfileField("Birthday", "June 24, 2003")
                            Divider()
                            ProfileField("Gender", "Male")
                            Divider()
                            ProfileField("Age Bracket", "18–24")
                        }
                    }

                    Spacer(modifier = Modifier.height(spacing.md))

                    DynamicButton(
                        onClick = {},
                        height = sizes.buttonHeight,
                        fontSize = sizes.buttonFontSize,
                        backgroundColor = Color(0xFF2E7D32),
                        content = "Delete Account"
                    )
                }

            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
        Text(value, color = Color(0xFF2E7D32))
    }
}