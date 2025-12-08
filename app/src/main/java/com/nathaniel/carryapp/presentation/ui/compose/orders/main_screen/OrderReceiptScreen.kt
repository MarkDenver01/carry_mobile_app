package com.nathaniel.carryapp.presentation.ui.compose.orders.main_screen

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.nathaniel.carryapp.domain.response.CustomerOrderResponse
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import timber.log.Timber

@Composable
fun OrderReceiptScreen(
    navController: NavController,
    orderId: Long?
) {
    val viewModel: OrderViewModel = sharedViewModel()
    val orders by viewModel.orders.collectAsState()
    val context = LocalContext.current

    val receiptView = remember { mutableStateOf<View?>(null) }

    val order = orders.firstOrNull { it.orderId == orderId }

    if (order == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Receipt not found")
        }
        return
    }

    // ✅ BUTTON IS NOW SEPARATED USING SCAFFOLD
    androidx.compose.material3.Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Button(
                onClick = {
                    receiptView.value?.let {
                        saveReceiptAsImage(context, it, order)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF118B3C))
            ) {
                Text("Download Receipt", color = Color.White)
            }
        }
    ) { padding ->

        // ✅ ONLY THIS PART IS CAPTURED AS IMAGE
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            factory = { ctx ->
                FrameLayout(ctx).also { receiptView.value = it }
            }
        ) { rootView ->

            val composeView = ComposeView(rootView.context).apply {
                setContent {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()     // ✅ IMPORTANT: wrap content
                            .background(Color.White)
                            .padding(20.dp)
                    ) {

                        // ✅ HEADER
                        Text(
                            "Successfully Paid",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(Modifier.height(24.dp))

                        // ✅ CUSTOMER
                        Text(order.customerName, fontWeight = FontWeight.Bold)
                        Text("Payment Method: ${order.paymentMethod}")

                        Spacer(Modifier.height(20.dp))

                        Divider()

                        Spacer(Modifier.height(12.dp))

                        // ✅ TOTAL
                        ReceiptRow("Amount Paid", order.totalAmount)
                        ReceiptRow("Delivery Fee", order.deliveryFee)
                        ReceiptRow("Discount", order.discount)

                        Spacer(Modifier.height(12.dp))

                        Divider()

                        Spacer(Modifier.height(12.dp))

                        Text("Reference No: ${order.orderId}")
                        Text("Date: ${order.createdAt}")

                        Spacer(Modifier.height(20.dp))
                    }
                }
            }

            rootView.removeAllViews()
            rootView.addView(composeView)
        }
    }
}


@Composable
fun ReceiptRow(label: String, value: Double) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text("₱${"%,.2f".format(value)}", fontWeight = FontWeight.Bold)
    }
}

fun saveReceiptAsImage(
    context: Context,
    receiptView: View,
    order: CustomerOrderResponse
) {
    try {
        // ✅ 1. Convert View to Bitmap
        val bitmap = Bitmap.createBitmap(
            receiptView.width,
            receiptView.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        receiptView.draw(canvas)

        // ✅ 2. Prepare File Name
        val fileName = "receipt_order_${order.orderId}.png"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/WrapAndCarry"
            )
        }

        val resolver = context.contentResolver

        val imageUri =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (imageUri != null) {
            resolver.openOutputStream(imageUri)?.let { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            }

            Toast
                .makeText(context, "Receipt saved to Downloads ✅", Toast.LENGTH_LONG)
                .show()

            Timber.d("✅ Receipt saved: $fileName")
        } else {
            Timber.e("❌ Failed to create MediaStore entry")
        }
    } catch (e: Exception) {
        Timber.e(e, "❌ Error saving receipt image")
    }
}
