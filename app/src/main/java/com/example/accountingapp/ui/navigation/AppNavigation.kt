package com.example.accountingapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import com.example.accountingapp.MainViewModel
import com.example.accountingapp.MainViewModelFactory
import com.example.accountingapp.data.AppDatabase
import com.example.accountingapp.data.AccountingRepository
import com.example.accountingapp.ui.components.BottomNavigationBar
import com.example.accountingapp.ui.home.HomeScreen
import com.example.accountingapp.ui.statistics.StatisticsScreen
import com.example.accountingapp.ui.bookkeeping.AddTransactionScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, CoroutineScope(Dispatchers.IO))
    val repository = AccountingRepository(database.transactionDao())
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(repository))
    
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Add.route && currentRoute?.startsWith("category_details") != true) {
                BottomNavigationBar(
                    navController = navController,
                    onAddClick = { navController.navigate(Screen.Add.route) }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onTransactionClick = { id ->
                        navController.navigate(Screen.TransactionDetail.createRoute(id))
                    }
                )
            }
            composable(Screen.Statistics.route) {
                StatisticsScreen(
                    viewModel = viewModel,
                    onCategoryClick = { categoryName ->
                        navController.navigate(Screen.CategoryDetails.createRoute(categoryName))
                    }
                )
            }
            composable(Screen.Add.route) {
                // This should probably not have the bottom bar, but for simplicity now it does.
                // In a real app we might hide it.
                AddTransactionScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            
            composable(
                route = Screen.CategoryDetails.route,
                arguments = listOf(androidx.navigation.navArgument("categoryName") { type = androidx.navigation.NavType.StringType })
            ) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                com.example.accountingapp.ui.statistics.CategoryDetailsScreen(
                    viewModel = viewModel,
                    categoryName = categoryName,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.TransactionDetail.route,
                arguments = listOf(androidx.navigation.navArgument("transactionId") { type = androidx.navigation.NavType.IntType })
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: 0
                com.example.accountingapp.ui.home.TransactionDetailScreen(
                    viewModel = viewModel,
                    transactionId = transactionId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
