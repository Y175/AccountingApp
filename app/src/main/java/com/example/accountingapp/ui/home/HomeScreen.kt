package com.example.accountingapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.MainViewModel
import com.example.accountingapp.data.Transaction
import com.example.accountingapp.data.TransactionType
import com.example.accountingapp.ui.components.AnimatedItem
import com.example.accountingapp.ui.components.OverviewCard
import com.example.accountingapp.ui.components.TransactionItem
import com.example.accountingapp.ui.theme.BlackPrimary
import com.example.accountingapp.ui.theme.ExpenseRed
import com.example.accountingapp.ui.theme.GrayText
import com.example.accountingapp.ui.theme.IncomeGreen
import com.example.accountingapp.ui.theme.YellowBackground
import com.example.accountingapp.ui.theme.YellowPrimary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, onTransactionClick: (Int) -> Unit) {
    val transactions by viewModel.currentTransactions.collectAsState()
    val income by viewModel.currentIncome.collectAsState()
    val expense by viewModel.currentExpense.collectAsState()
    val dateDisplay by viewModel.dateDisplay.collectAsState()

    // 显示日期筛选弹窗
    var showDatePicker by remember { mutableStateOf(false) }

    // Group transactions by date
    val groupedTransactions = transactions.groupBy {
        SimpleDateFormat("MM.dd EEEE", Locale.CHINA).format(Date(it.date))
    }

    // Delete Dialog State
    var showDeleteDialog by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    if (showDeleteDialog && transactionToDelete != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("删除账单") },
            text = { Text("确定要删除这条记录吗？") },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        viewModel.deleteTransaction(transactionToDelete!!)
                        showDeleteDialog = false
                        transactionToDelete = null
                    }
                ) {
                    Text("删除", color = Color.Red)
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showDeleteDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    // 日期筛选弹窗
    if (showDatePicker) {
        FilterDatePickerDialog(
            viewModel = viewModel,
            onDismiss = { showDatePicker = false }
        )
    }

    Scaffold(
        floatingActionButton = {
            // Animated FAB
            androidx.compose.material3.FloatingActionButton(
                onClick = { /* Add navigation if needed, currently done in AppNavigation */ },
                containerColor = YellowPrimary,
                contentColor = BlackPrimary,
                shape = RoundedCornerShape(16.dp),
                elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                 Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        },
        containerColor = YellowBackground // Match the updated theme background
    ) { padding ->
        Column(
             modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(YellowBackground)
        ) {
            // Top Header based on Design
            Row(
                 modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                 Column {
                    Text(
                        text = "欢迎光临，喵~",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = BlackPrimary
                    )
                     Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "今天也要好好记账哦",
                        style = MaterialTheme.typography.labelMedium,
                        color = GrayText
                    )
                 }
                 // Mascot placeholder
                 Box(
                     modifier = Modifier
                        .size(48.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, YellowPrimary.copy(alpha=0.5f), CircleShape)
                 )
            }

            // Main Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 80.dp)
            ) {
                // Overview Card
                item {
                    OverviewCard(
                        income = income,
                        expense = expense,
                        balance = income - expense,
                        currentMonth = dateDisplay,
                        onMonthClick = { showDatePicker = true }
                    )
                }

                // Transactions List
                groupedTransactions.forEach { (dateString, txs) ->
                    stickyHeader {
                        DateHeader(dateString, txs)
                    }
                    items(txs) { transaction ->
                        AnimatedItem { 
                             TransactionItem(
                                transaction = transaction,
                                onClick = {
                                    onTransactionClick(transaction.id)
                                },
                                onLongClick = {
                                    transactionToDelete = transaction
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateHeader(dateString: String, transactions: List<Transaction>) {
    val dailyIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val dailyExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    
    // Glassy/Modern Sticky Header
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(YellowBackground.copy(alpha = 0.95f)) // Translucent background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Date Indicator Pill
                Box(
                    modifier = Modifier
                        .background(BlackPrimary, RoundedCornerShape(4.dp))
                        .size(4.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = BlackPrimary
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (dailyIncome > 0) {
                    Text(
                        text = "收 ${String.format("%.2f", dailyIncome)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = IncomeGreen,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                if (dailyExpense > 0) {
                    Text(
                        text = "支 ${String.format("%.2f", dailyExpense)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = ExpenseRed,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FilterDatePickerDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Month, 1: Week
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    var selectedYear by remember { mutableStateOf(currentYear) }

    androidx.compose.material3.ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Tab Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabItem(text = "按月", isSelected = selectedTab == 0) { selectedTab = 0 }
                TabItem(text = "按周", isSelected = selectedTab == 1) { selectedTab = 1 }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedTab == 0) {
                // --- MONTH SELECTION ---
                // Year Selector with "Past 3 Years" quick select
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { selectedYear-- }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Prev Year")
                    }
                    Text(text = "${selectedYear}年", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { selectedYear++ }) {
                         Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next Year")
                    }
                }
                
                // Past 3 Years Quick Chips
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.Center) {
                    val years = listOf(currentYear, currentYear - 1, currentYear - 2)
                    years.forEach { year ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clickable { selectedYear = year }
                                .background(
                                    if (selectedYear == year) YellowPrimary.copy(alpha=0.3f) else Color.Transparent, 
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(text = "$year", fontSize = 12.sp, color = if (selectedYear == year) BlackPrimary else GrayText)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Month Grid
                Column {
                    for (row in 0..2) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            for (col in 1..4) {
                                val month = row * 4 + col
                                MonthItem(month = month, isSelected = false /* handled by logic */, onClick = {
                                    val cal = Calendar.getInstance()
                                    cal.set(Calendar.YEAR, selectedYear)
                                    cal.set(Calendar.MONTH, month - 1)
                                    viewModel.setDateFilter(com.example.accountingapp.DateFilterType.MONTH, cal)
                                    onDismiss()
                                })
                            }
                        }
                        if (row < 2) Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            } else {
                // --- WEEK SELECTION ---
                Text("最近三周", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                val currentCal = Calendar.getInstance()
                // Logic to generate last 3 weeks
                val weeks = (0..2).map { offset ->
                    val cal = currentCal.clone() as Calendar
                    cal.add(Calendar.WEEK_OF_YEAR, -offset)
                    cal
                }

                weeks.forEach { weekCal ->
                    val weekNum = weekCal.get(Calendar.WEEK_OF_YEAR)
                    val year = weekCal.get(Calendar.YEAR)
                    
                    // Calculate start/end dates for display
                    val startCal = weekCal.clone() as Calendar
                    startCal.firstDayOfWeek = Calendar.MONDAY
                    startCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                    val dateFormat = SimpleDateFormat("MM.dd", Locale.getDefault())
                    val startStr = dateFormat.format(startCal.time)
                    
                    startCal.add(Calendar.DAY_OF_WEEK, 6)
                    val endStr = dateFormat.format(startCal.time)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setDateFilter(com.example.accountingapp.DateFilterType.WEEK, weekCal)
                                onDismiss()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(YellowPrimary.copy(alpha=0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "W$weekNum", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "${year}年 第${weekNum}周", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                            Text(text = "$startStr - $endStr", style = MaterialTheme.typography.bodySmall, color = GrayText)
                        }
                    }
                    androidx.compose.material3.HorizontalDivider(thickness = 0.5.dp, color = GrayText.copy(alpha=0.1f))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .background(if (isSelected) Color.White else Color.Transparent, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) BlackPrimary else GrayText
        )
    }
}

@Composable
fun MonthItem(month: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                if (isSelected) YellowPrimary else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .border(1.dp, if (isSelected) Color.Transparent else GrayText.copy(alpha=0.1f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${month}月",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = BlackPrimary
        )
    }
}