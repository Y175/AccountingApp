package com.example.accountingapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.accountingapp.ui.navigation.Screen

@Composable
fun BottomNavigationBar(
    navController: NavController,
    onAddClick: () -> Unit,
    isAddVisible: Boolean
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
                .offset(y = (-16).dp) // 从 -28dp 改为 -16dp，降低位置
                .size(56.dp)
                .rotate(rotation),
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