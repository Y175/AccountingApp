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
import androidx.compose.ui.draw.shadow
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
            containerColor = com.example.accountingapp.ui.theme.PureWhite,
            contentColor = com.example.accountingapp.ui.theme.InkBlack,
            tonalElevation = 8.dp
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
                    selectedIconColor = com.example.accountingapp.ui.theme.FreshAirBlue,
                    selectedTextColor = com.example.accountingapp.ui.theme.FreshAirBlue,
                    indicatorColor = com.example.accountingapp.ui.theme.FreshAirBlue.copy(alpha=0.1f),
                    unselectedIconColor = com.example.accountingapp.ui.theme.SlateGray,
                    unselectedTextColor = com.example.accountingapp.ui.theme.SlateGray
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
                    selectedIconColor = com.example.accountingapp.ui.theme.ApricotOrange,
                    selectedTextColor = com.example.accountingapp.ui.theme.ApricotOrange,
                    indicatorColor = com.example.accountingapp.ui.theme.ApricotOrange.copy(alpha=0.1f),
                    unselectedIconColor = com.example.accountingapp.ui.theme.SlateGray,
                    unselectedTextColor = com.example.accountingapp.ui.theme.SlateGray
                )
            )
        }

        // Custom Middle Button (Lilac)
        FloatingActionButton(
            onClick = onAddClick,
            containerColor = com.example.accountingapp.ui.theme.LilacAccent,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .offset(y = (-24).dp)
                .size(64.dp)
                .shadow(8.dp, CircleShape),
            elevation = FloatingActionButtonDefaults.elevation(0.dp) // Handled by shadow
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(32.dp))
        }
    }
}
