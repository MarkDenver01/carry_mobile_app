package com.nathaniel.carryapp.presentation.ui.compose.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.presentation.utils.responsiveDp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PromoBannerCarousel(
    modifier: Modifier = Modifier,
    bannerHeight: Dp
) {
    val images = listOf(
        R.drawable.banner_wrap_n_carry,
        R.drawable.banner_wrap_n_carry,
        R.drawable.banner_wrap_n_carry
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll logic
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % images.size
            coroutineScope.launch {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    val dotSize = responsiveDp(8f)
    val dotSpacing = responsiveDp(4f)
    val bottomPadding = responsiveDp(8f)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "Promo Banner $page",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(dotSpacing * 2))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentHeight()
                .padding(bottom = bottomPadding)
        ) {
            repeat(images.size) { index ->
                val color =
                    if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
                    else Color.LightGray

                Box(
                    modifier = Modifier
                        .padding(horizontal = dotSpacing)
                        .size(dotSize)
                        .background(color = color, shape = MaterialTheme.shapes.small)
                )
            }
        }
    }
}
