package com.example.accountingapp.ui.bookkeeping

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.accountingapp.MainViewModel
import com.example.accountingapp.ui.navigation.Screen
import kotlin.math.pow
import kotlin.math.sqrt

// 2 & 3. 修改 AddTransactionOverlay.kt
@Composable
fun AddTransactionOverlay(
    isVisible: Boolean,
    viewModel: MainViewModel,
    fabPosition: Offset, // 新增：FAB的屏幕位置
    screenSize: IntSize, // 新增：屏幕尺寸
    onDismiss: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isVisible) 45f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "Overlay FAB Rotation"
    )

    // 计算从FAB中心到屏幕最远角的距离（扩散半径）
    val maxRadius = remember(fabPosition, screenSize) {
        val corners = listOf(
            Offset(0f, 0f),
            Offset(screenSize.width.toFloat(), 0f),
            Offset(0f, screenSize.height.toFloat()),
            Offset(screenSize.width.toFloat(), screenSize.height.toFloat())
        )
        corners.maxOf { corner ->
            sqrt(
                (corner.x - fabPosition.x).pow(2) +
                        (corner.y - fabPosition.y).pow(2)
            )
        }
    }

    // 圆形扩散动画
    val revealRadius by animateFloatAsState(
        targetValue = if (isVisible) maxRadius else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "Reveal Radius"
    )

    // 内容缩放动画（稍微延迟）
    val contentScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 100,
            easing = FastOutSlowInEasing
        ),
        label = "Content Scale"
    )

    // 内容透明度动画
    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 250,
            delayMillis = 150
        ),
        label = "Content Alpha"
    )

    // 只有在有动画进行时才渲染
    if (isVisible || revealRadius > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {} // 拦截所有点击
                )
        ) {
            // 使用Canvas绘制圆形扩散遮罩
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.Black.copy(alpha = 0.5f),
                    radius = revealRadius,
                    center = fabPosition
                )
            }

            // 内容区域 - 应用圆形裁剪
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        clip = true
                        shape = CirclePath(fabPosition, revealRadius)
                        alpha = if (revealRadius > 0) 1f else 0f
                    }
            ) {
                // 浮层内容区域
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(
                            top = 80.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .graphicsLayer {
                                scaleX = contentScale
                                scaleY = contentScale
                                alpha = contentAlpha
                            }
                    ) {
                        if (contentAlpha > 0.5f) {
                            AddTransactionScreen(
                                viewModel = viewModel,
                                onBack = onDismiss
                            )
                        }
                    }
                }

                // 占位空间
                Spacer(modifier = Modifier.height(80.dp))
            }

            // × 按钮
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    FloatingActionButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .offset(y = (-16).dp)
                            .size(56.dp)
                            .rotate(rotation),
                        containerColor = MaterialTheme.colorScheme.primary,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 12.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "关闭",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

// 自定义圆形裁剪Shape
class CirclePath(private val center: Offset, private val radius: Float) : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: androidx.compose.ui.unit.Density
    ): Outline {
        return Outline.Generic(
            Path().apply {
                addOval(
                    Rect(
                        center.x - radius,
                        center.y - radius,
                        center.x + radius,
                        center.y + radius
                    )
                )
            }
        )
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    onAddClick: () -> Unit,
    isAddVisible: Boolean,
    onFabPositionChanged: (Offset) -> Unit // 新增：回调FAB位置
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val rotation by animateFloatAsState(
        targetValue = if (isAddVisible) 45f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "FAB Rotation"
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "首页") },
            label = { Text("首页") },
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        NavigationBarItem(
            icon = { Icon(Icons.Default.BarChart, contentDescription = "统计") },
            label = { Text("统计") },
            selected = currentRoute == Screen.Statistics.route,
            onClick = {
                navController.navigate(Screen.Statistics.route) {
                    popUpTo(Screen.Home.route)
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        FloatingActionButton(
            onClick = onAddClick,
            modifier = Modifier
                .offset(y = (-16).dp)
                .size(56.dp)
                .rotate(rotation)
                .onGloballyPositioned { coordinates ->
                    // 获取FAB中心位置
                    val rect = coordinates.boundsInWindow()
                    val center = Offset(
                        x = rect.left + rect.width / 2,
                        y = rect.top + rect.height / 2
                    )
                    onFabPositionChanged(center)
                },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = if (isAddVisible) "关闭" else "添加",
                tint = Color.White
            )
        }
    }
}

