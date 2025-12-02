import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.presentation.theme.LocalAppColors
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.utils.DynamicButton

@Composable
fun SignUpSuccessScreen(
    onProceed: () -> Unit,
    onBack: () -> Unit
) {
    val sizes = LocalResponsiveSizes.current
    val colors = LocalAppColors.current
    val spacing = 16.dp

    // Load your uploaded Lottie file
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.ic_reg_success)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onBack() },
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                DynamicButton(
                    onClick = onProceed,
                    height = sizes.buttonHeight,
                    backgroundColor = colors.primary,
                    pressedBackgroundColor = colors.primaryDark,
                    content = "Proceed",
                    fontSize = sizes.buttonFontSize
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress }
                )
            }

            Spacer(modifier = Modifier.height(spacing))

            Text(
                text = "Registration Successful!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your customer has been successfully registered!",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}
