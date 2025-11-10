package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.ExperimentalFoundationApi

data class BannerItem(
    val imageRes: Int,
    val title: String = "",
    val subtitle: String = ""
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PromoBanner(
    banners: List<BannerItem>,
    autoScrollDuration: Long = 3000L // 3 seconds per banner
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { banners.size })
    val coroutineScope = rememberCoroutineScope()

    // ✅ Auto-scroll every [autoScrollDuration] ms
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
            Card(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = banner.imageRes),
                        contentDescription = "Promo Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp)
                    ) {
                        if (banner.title.isNotEmpty()) {
                            Text(
                                text = banner.title,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp
                            )
                            Spacer(Modifier.height(6.dp))
                        }
                        if (banner.subtitle.isNotEmpty()) {
                            Text(
                                text = banner.subtitle,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // ✅ Indicator Dots
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
