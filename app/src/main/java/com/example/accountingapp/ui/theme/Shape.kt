package com.example.accountingapp.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp), // Card default
    large = RoundedCornerShape(32.dp),  // BottomSheet default
    extraLarge = RoundedCornerShape(48.dp) // Expressive Container
)

// Cat-like Asymmetric Shape for Expressive Containers
val AsymmetricShape = RoundedCornerShape(
    topStart = 48.dp,
    topEnd = 20.dp,
    bottomEnd = 48.dp,
    bottomStart = 20.dp
)

val FullPillShape = CircleShape // Semantic alias
