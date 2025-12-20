package com.example.accountingapp.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.accountingapp.ui.theme.PureWhite

@Composable
fun BreathingFAB(
    listState: LazyListState? = null,
    onClick: () -> Unit
) {
    val shouldReduceOpacity by remember {
        derivedStateOf {
            if (listState == null) return@derivedStateOf false

            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo

            if (visibleItems.isEmpty()) return@derivedStateOf false

            val fabBottom = layoutInfo.viewportEndOffset - 24
            val fabTop = fabBottom - 100

            visibleItems.any { item ->
                val itemBottom = item.offset + item.size
                val itemTop = item.offset
                val verticalOverlap = itemBottom > fabTop && itemTop < fabBottom
                verticalOverlap && itemBottom > fabTop + 20
            }
        }
    }

    val targetAlpha = if (shouldReduceOpacity) 0.3f else 0.9f
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "fab_alpha"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing_scale"
    )

    androidx.compose.material3.FloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFF6B8AFF), // 更符合当前 UI 的蓝紫色调
        contentColor = PureWhite,
        shape = CircleShape,
        elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 12.dp,
            focusedElevation = 8.dp,
            hoveredElevation = 10.dp
        ),
        modifier = Modifier
            .size(60.dp) // 稍微增大按钮
            .graphicsLayer {
                scaleX = breathingScale
                scaleY = breathingScale
                this.alpha = alpha
            }
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Add Transaction",
            modifier = Modifier.size(32.dp), // 增大图标尺寸
            tint = PureWhite
        )
    }
}