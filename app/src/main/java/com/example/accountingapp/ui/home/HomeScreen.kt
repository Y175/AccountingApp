package com.example.accountingapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.MainViewModel
import com.example.accountingapp.data.Transaction
import com.example.accountingapp.data.TransactionType
import com.example.accountingapp.ui.components.AnimatedItem
import com.example.accountingapp.ui.components.OverviewCard
import com.example.accountingapp.ui.components.TransactionItem
import com.example.accountingapp.ui.theme.YellowPrimary
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val transactions by viewModel.currentMonthTransactions.collectAsState()
    val income by viewModel.currentMonthIncome.collectAsState()
    val expense by viewModel.currentMonthExpense.collectAsState()
    
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(YellowPrimary) // Top background is yellow
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "欢迎光临，喵~",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))

            }
            // Mascot placeholder (Circle)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, RoundedCornerShape(24.dp))
            ) {
                // Image would go here
            }
        }

        // Main Content (White background with rounded top corners)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.05f), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    OverviewCard(
                        income = income,
                        expense = expense,
                        balance = income - expense,
                        currentMonth = "2025\n12月", // Dynamic later
                        onMonthClick = {}
                    )
                }

                groupedTransactions.forEach { (dateString, txs) ->
                    stickyHeader {
                        DateHeader(dateString, txs)
                    }
                    items(txs) { transaction ->
                        AnimatedItem {
                            TransactionItem(
                                transaction = transaction,
                                onLongClick = {
                                    transactionToDelete = transaction
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
                
                // Bottom spacer for FAB
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun DateHeader(dateString: String, transactions: List<Transaction>) {
    val dailyIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val dailyExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5)) // Light gray background for sticky header
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(16.dp)
                    .background(YellowPrimary, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = dateString,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        Row {
            if (dailyIncome > 0) {
                Text(
                    text = "收入 ${String.format("%.2f", dailyIncome)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            if (dailyExpense > 0) {
                Text(
                    text = "支出 ${String.format("%.2f", dailyExpense)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}
