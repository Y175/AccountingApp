package com.example.accountingapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.accountingapp.MainViewModel
import com.example.accountingapp.MainViewModelFactory
import com.example.accountingapp.data.AccountingRepository
import com.example.accountingapp.data.AppDatabase
import com.example.accountingapp.ui.home.HomeScreen
import com.example.accountingapp.ui.statistics.StatisticsScreen
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

    var isAddTransactionOverlayVisible by remember { mutableStateOf(false) }
    var fabPosition by remember { mutableStateOf(Offset.Zero) }

    // 获取屏幕尺寸
    val configuration = LocalConfiguration.current
    val screenSize = remember(configuration) {
        IntSize(
            configuration.screenWidthDp.dp.value.toInt() * configuration.densityDpi / 160,
            configuration.screenHeightDp.dp.value.toInt() * configuration.densityDpi / 160
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                if (currentRoute == Screen.Home.route || currentRoute == Screen.Statistics.route) {
                    com.example.accountingapp.ui.bookkeeping.BottomNavigationBar(
                        navController = navController,
                        onAddClick = {
                            isAddTransactionOverlayVisible = !isAddTransactionOverlayVisible
                        },
                        isAddVisible = isAddTransactionOverlayVisible,
                        onFabPositionChanged = { fabPosition = it }
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

        // 浮层覆盖整个屏幕
        com.example.accountingapp.ui.bookkeeping.AddTransactionOverlay(
            isVisible = isAddTransactionOverlayVisible,
            viewModel = viewModel,
            fabPosition = fabPosition,
            screenSize = screenSize,
            onDismiss = { isAddTransactionOverlayVisible = false }
        )
    }
}