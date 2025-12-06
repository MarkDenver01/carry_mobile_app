package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nathaniel.carryapp.R
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/* ---------------------------------------------------------
   OPTIONAL SHIMMER MODIFIER (CURRENTLY NOT USED)
--------------------------------------------------------- */
@Composable
fun Modifier.shimmerEffect(): Modifier {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.4f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.4f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing)
        ),
        label = "shimmerFloat"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translate, translate),
        end = Offset(translate + 200f, translate + 200f)
    )

    return this.background(brush)
}

/* ---------------------------------------------------------
   ROTATING LOADING ICON
   (Gamit ka ng sarili mong icon: ic_loading)
--------------------------------------------------------- */
@Composable
fun RotatingLoader(
    size: Dp = 36.dp,
    color: Color = Color(0xFF118B3C)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loader")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = LinearEasing)
        ),
        label = "rotationAnim"
    )

    Icon(
        painter = painterResource(R.drawable.ic_broken_image), // 👉 palitan mo ng sariling loading icon
        contentDescription = null,
        tint = color,
        modifier = Modifier
            .size(size)
            .rotate(angle)
    )
}

/* ---------------------------------------------------------
   PRODUCT CARD
--------------------------------------------------------- */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductCard(
    imageHeight: Dp = 140.dp,
    cardWidth: Dp? = null,
    cardHeight: Dp? = null,
    nameMaxLines: Int = 2,
    nameEllipsis: Boolean = true,
    imageUrl: String,
    name: String,
    weight: String,
    sold: Int,
    price: Double,
    expiryDate: String?,
    onFavorite: () -> Unit,
    onAdd: () -> Unit,
    onMinus: () -> Unit,
    onDeduct: () -> Unit,
    onRestore: () -> Unit,
    onDetailClick: () -> Unit
) {
    // ================= EXPIRY LOGIC =================
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun parseDaysLeft(exp: String?): Int? {
        return try {
            if (exp.isNullOrBlank()) return null
            val dateOnly = exp.substringBefore(" ")
            ChronoUnit.DAYS.between(today, LocalDate.parse(dateOnly, formatter)).toInt()
        } catch (e: Exception) {
            Timber.e("ProductCard expiry parse error: ${e.message}")
            null
        }
    }

    val daysLeft = parseDaysLeft(expiryDate)
    val showPromo = daysLeft != null && daysLeft in 0..60

    var promoPressed by remember { mutableStateOf(false) }
    val promoColor = if (promoPressed) Color(0xFF0C6A2D) else Color(0xFF16A34A)

    var qty by remember { mutableStateOf(0) }
    val remainingStock = (sold - qty).coerceAtLeast(0)

    // ================= IMAGE LOADING STATES =================
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var showLoader by remember { mutableStateOf(true) }   // controls visibility of loader

    Card(
        modifier = Modifier
            .then(if (cardWidth != null) Modifier.width(cardWidth) else Modifier.fillMaxWidth())
            .then(if (cardHeight != null) Modifier.height(cardHeight) else Modifier.wrapContentHeight())
            .clickable { onDetailClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color(0xFFE6ECEF), RoundedCornerShape(16.dp))
                .padding(10.dp)
        ) {

            // ================= IMAGE SECTION =================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F5F6))
            ) {
                // IMAGE REQUEST
                AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                    onLoading = {
                        isLoading = true
                        isError = false
                        showLoader = true
                    },
                    onSuccess = {
                        isLoading = false
                        isError = false
                        // Huwag mag-LaunchedEffect DITO
                    },
                    onError = {
                        isLoading = false
                        isError = true
                    }
                )

                // ✅ 1-SECOND DELAY HANDLED HERE (COMPOSABLE CONTEXT)
                LaunchedEffect(isLoading) {
                    if (!isLoading) {       // success or error
                        kotlinx.coroutines.delay(500)
                        showLoader = false
                    } else {
                        showLoader = true
                    }
                }

                // ⭐ SHOW ROTATING LOADER WHILE LOADING
                if (showLoader) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color(0x30FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        RotatingLoader(size = 40.dp)
                    }
                }

                // ❌ ERROR OVERLAY (AFTER LOADER HIDDEN)
                if (isError && !showLoader) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color(0xFFECECEC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_broken_image),
                            contentDescription = "Error",
                            tint = Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // ⭐ PROMO BADGE
                if (showPromo) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(promoColor)
                            .clickable { promoPressed = !promoPressed }
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = R.drawable.discount,
                            contentScale = ContentScale.Fit,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
            }

            // ================= BODY =================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color(0xFF0E1F22),
                        maxLines = nameMaxLines,
                        overflow = if (nameEllipsis) TextOverflow.Ellipsis else TextOverflow.Clip,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(4.dp))
                    Text(weight, fontSize = 12.sp, color = Color(0xFF6B7D85))
                    Spacer(Modifier.height(4.dp))
                    Text("$remainingStock Stocks", fontSize = 12.sp, color = Color(0xFF118B3C))
                }

                Column {
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = price.toString(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = Color(0xFF0E1F22)
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFF118B3C), RoundedCornerShape(10.dp))
                            .background(Color.White),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        TextButton(
                            onClick = {
                                if (qty > 0) {
                                    qty--
                                    onMinus()
                                    onRestore()
                                }
                            },
                            modifier = Modifier.width(48.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                "–",
                                color = Color(0xFF6B7D85),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(qty.toString(), fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

                        TextButton(
                            onClick = {
                                if (remainingStock > 0) {
                                    qty++
                                    onAdd()
                                    onDeduct()
                                }
                            },
                            modifier = Modifier.width(48.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                "+",
                                color = Color(0xFF118B3C),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
