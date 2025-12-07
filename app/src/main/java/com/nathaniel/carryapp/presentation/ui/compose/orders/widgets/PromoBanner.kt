package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.model.ProductBanner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PromoBanner(
    banners: List<ProductBanner>,
    autoScrollDuration: Long = 3000L
) {
    if (banners.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { banners.size }
    )

    val coroutineScope = rememberCoroutineScope()

    // ✅ Auto-scroll
    LaunchedEffect(pagerState.currentPage) {
        delay(autoScrollDuration)
        val nextPage = (pagerState.currentPage + 1) % banners.size
        coroutineScope.launch {
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        ) { page ->

            val banner = banners[page]

            // ✅ SAME IMAGE LOADING STATES AS ProductCard
            var isLoading by remember(banner.id) { mutableStateOf(true) }
            var isError by remember(banner.id) { mutableStateOf(false) }
            var showLoader by remember(banner.id) { mutableStateOf(true) }

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF1F5F6))
                ) {

                    // ✅ IMAGE REQUEST (SAME LOGIC)
                    AsyncImage(
                        model = banner.bannerUrl,
                        contentDescription = "Promo Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        onLoading = {
                            isLoading = true
                            isError = false
                            showLoader = true
                        },
                        onSuccess = {
                            isLoading = false
                            isError = false
                        },
                        onError = {
                            isLoading = false
                            isError = true
                        }
                    )

                    // ✅ SAME 500ms DELAY LOGIC
                    LaunchedEffect(isLoading) {
                        if (!isLoading) {      // success OR error
                            kotlinx.coroutines.delay(500)
                            showLoader = false
                        } else {
                            showLoader = true
                        }
                    }

                    // ⭐ SAME ROTATING LOADER
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

                    // ❌ SAME ERROR OVERLAY
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
                }
            }
        }

        // ✅ INDICATOR DOTS (UNCHANGED)
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        ) {
            repeat(banners.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (isSelected) 10.dp else 6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) Color(0xFF118B3C)
                            else Color(0xFFD1D5D4)
                        )
                )
            }
        }
    }
}
