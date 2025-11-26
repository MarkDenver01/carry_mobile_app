package com.nathaniel.carryapp.presentation.ui.compose.orders.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopBottomBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopHeader
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopSearchBar

@Composable
fun ProductDetailScreen(
    imageUrl: String,
    name: String,
    size: String,
    description: String,
    price: String,
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        // IMAGE
        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(size, fontSize = 14.sp, color = Color.Gray)

        Spacer(Modifier.height(12.dp))

        Text(description, fontSize = 14.sp)

        Spacer(Modifier.height(16.dp))

        Text(price, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF118B3C))
    }
}
