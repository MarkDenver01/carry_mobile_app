package com.nathaniel.carryapp.presentation.utils.otp

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.carryapp.presentation.theme.LocalAppColors
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes

@Composable
fun OtpTextField(
    otpText: String,
    onOtpTextChange: (String, Boolean) -> Unit,
    otpCount: Int = 5
) {
    val focusRequester = remember { FocusRequester() }
    val colors = LocalAppColors.current
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current

    BasicTextField(
        value = otpText,
        onValueChange = { input ->
            if (input.length <= otpCount) {
                onOtpTextChange(input, input.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = LocalTextStyle.current.copy(color = Color.Transparent),
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .padding(vertical = spacing.md),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(otpCount) { index ->
                    val isActive = index == otpText.length
                    val boxColor by animateColorAsState(
                        targetValue = if (isActive) colors.primary.copy(alpha = 0.15f) else Color(0xFFE6E6E6)
                    )
                    val borderColor by animateColorAsState(
                        targetValue = if (isActive) colors.primary else Color(0xFFCCCCCC)
                    )
                    val boxSize by animateDpAsState(
                        targetValue = if (isActive) 56.dp else 52.dp
                    )

                    val char = otpText.getOrNull(index)?.toString() ?: ""

                    Box(
                        modifier = Modifier
                            .size(boxSize)
                            .border(
                                width = 2.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(boxColor, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111111),
                            textAlign = TextAlign.Center
                        )
                    }

                    if (index < otpCount - 1) {
                        Spacer(modifier = Modifier.width(spacing.lsm))
                    }
                }
            }
        }
    )

    // Auto-focus
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
