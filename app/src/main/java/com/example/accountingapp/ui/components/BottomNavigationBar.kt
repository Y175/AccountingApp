package com.example.accountingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.accountingapp.ui.navigation.Screen
import com.example.accountingapp.ui.theme.YellowPrimary

@Composable
fun BottomNavigationBar(navController: NavController, onAddClick: () -> Unit) {
    val items = listOf(
        Screen.Home,
        Screen.Statistics
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(contentAlignment = Alignment.BottomCenter) {
        NavigationBar(
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            // Home
            NavigationBarItem(
                icon = { Icon(Screen.Home.icon, contentDescription = Screen.Home.title) },
                label = { Text(Screen.Home.title) },
                selected = currentRoute == Screen.Home.route,
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = Color.Black,
                    indicatorColor = YellowPrimary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )

            // Spacer for the middle button
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = {},
                enabled = false
            )

            // Statistics
            NavigationBarItem(
                icon = { Icon(Screen.Statistics.icon, contentDescription = Screen.Statistics.title) },
                label = { Text(Screen.Statistics.title) },
                selected = currentRoute == Screen.Statistics.route,
                onClick = {
                    navController.navigate(Screen.Statistics.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = Color.Black,
                    indicatorColor = YellowPrimary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }

        // Custom Middle Button
        FloatingActionButton(
            onClick = onAddClick,
            containerColor = YellowPrimary,
            contentColor = Color.Black,
            shape = CircleShape,
            modifier = Modifier
                .offset(y = (-20).dp)
                .size(64.dp),
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(32.dp))
        }
    }
}
