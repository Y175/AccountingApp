package com.example.accountingapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "首页", Icons.Default.Home)
    object Add : Screen("add_transaction", "去记账", Icons.Default.Add)
    object Statistics : Screen("statistics", "账单", Icons.Default.PieChart)
    object CategoryDetails : Screen("category_details/{categoryName}", "分类明细", Icons.Default.PieChart) {
        fun createRoute(categoryName: String) = "category_details/$categoryName"
    }
}
