package com.nathaniel.carryapp.presentation.ui.compose.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nathaniel.carryapp.presentation.utils.responsiveDp
import com.nathaniel.carryapp.presentation.utils.responsiveSp

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val items = BottomNavItem.item
    val centerIndex = items.size / 2

    val barHeight = responsiveDp(100f)
    val iconSize = responsiveDp(24f)
    val centerIconSize = responsiveDp(28f)
    val centerSize = responsiveDp(56f)
    val labelFontSize = responsiveSp(11f)
    val offsetY = responsiveDp(24f)
    val horizontalPadding = responsiveDp(8f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight)
            .navigationBarsPadding()
            .background(
                color = Color(0xFF2E7D32),
                shape = fancyBottomNavShape()
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = item.route == currentRoute
                val animatedColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else Color(0xFFB2DFDB),
                    label = "NavColorAnim"
                )
                val animatedScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.25f else 1f,
                    label = "NavScaleAnim"
                )

                if (index == centerIndex) {
                    // Floating Center Button
                    Box(
                        modifier = Modifier
                            .offset(y = -offsetY)
                            .size(centerSize)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = LocalIndication.current
                            ) {
                                if (!isSelected) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                            .graphicsLayer {
                                scaleX = animatedScale
                                scaleY = animatedScale
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(centerIconSize)
                        )
                    }
                } else {
                    // Side Items
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(50)) // Makes ripple rounded
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = LocalIndication.current
                            ) {
                                if (!isSelected) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                            .padding(vertical = responsiveDp(8f)) // Optional: better hit target
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = animatedColor,
                            modifier = Modifier
                                .size(iconSize)
                                .graphicsLayer {
                                    scaleX = animatedScale
                                    scaleY = animatedScale
                                }
                        )
                        Text(
                            text = item.label,
                            fontSize = labelFontSize,
                            color = animatedColor
                        )
                    }
                }
            }
        }
    }
}
