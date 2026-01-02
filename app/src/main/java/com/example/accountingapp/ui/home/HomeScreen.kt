package com.example.accountingapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.MainViewModel
import com.example.accountingapp.R
import com.example.accountingapp.data.Transaction
import com.example.accountingapp.ui.components.AnimatedItem
import com.example.accountingapp.ui.components.ElegantDatePickerDialog
import com.example.accountingapp.ui.components.OverviewCard
import com.example.accountingapp.ui.components.TransactionItem
import com.example.accountingapp.ui.theme.FreshAirBlue
import com.example.accountingapp.ui.theme.PureWhite
import com.example.accountingapp.ui.theme.SlateGray
import com.example.accountingapp.ui.theme.WarmPaper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, onTransactionClick: (Int) -> Unit) {
    val transactions by viewModel.currentTransactions.collectAsState()
    val income by viewModel.currentIncome.collectAsState()
    val expense by viewModel.currentExpense.collectAsState()
    val currentFilterStart by viewModel.currentFilterStart.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    val groupedTransactions = transactions.groupBy {
        SimpleDateFormat("MM.dd EEEE", Locale.CHINA).format(Date(it.date))
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    // LazyColumn 滚动状态
    val listState = rememberLazyListState()

    // 删除确认对话框
    if (showDeleteDialog && transactionToDelete != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "删除账单",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("确定要删除这条记录吗？")
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        viewModel.deleteTransaction(transactionToDelete!!)
                        showDeleteDialog = false
                        transactionToDelete = null
                    }
                ) {
                    Text("删除", color = Color(0xFFFF6B6B))
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("取消", color = SlateGray)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = PureWhite
        )
    }

    // 优雅日期选择器
    if (showDatePicker) {
        ElegantDatePickerDialog(
            selectedDate = currentFilterStart,
            onDateSelected = { selectedCal ->
                viewModel.setDateFilter(
                    com.example.accountingapp.DateFilterType.MONTH,
                    selectedCal
                )
            },
            onDismiss = { showDatePicker = false }
        )
    }

    Scaffold(
        containerColor = FreshAirBlue
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FreshAirBlue)
            ) {
                // 顶部头像区域
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(
                                id = com.example.accountingapp.R.drawable.ic_avatar_cat
                            ),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(52.dp)
                                .background(PureWhite, CircleShape)
                                .border(2.dp, PureWhite.copy(alpha = 0.5f), CircleShape)
                                .padding(4.dp)
                                .clip(CircleShape),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "你的记账助手",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = PureWhite
                            )
                            Text(
                                text = "今天你记账了吗~",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PureWhite.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 主内容区域
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = WarmPaper,
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    LazyColumn(
                        state = listState,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            bottom = 96.dp,
                            top = 24.dp
                        )
                    ) {
                        // 概览卡片
                        item {
                            OverviewCard(
                                income = income,
                                expense = expense,
                                balance = income - expense,
                                currentDate = currentFilterStart,
                                onDateSelect = { selectedCal ->
                                    viewModel.setDateFilter(
                                        com.example.accountingapp.DateFilterType.MONTH,
                                        selectedCal
                                    )
                                },
                                onFilterClick = { showDatePicker = true }
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "最近记账明细",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SlateGray.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // 交易列表
                        var globalIndex = 0
                        groupedTransactions.forEach { (dateString, txs) ->
                            stickyHeader {
                                DateHeader(dateString, txs)
                            }
                            items(txs, key = { it.id }) { transaction ->
                                AnimatedItem(index = globalIndex++) {
                                    TransactionItem(
                                        transaction = transaction,
                                        onClick = { onTransactionClick(transaction.id) },
                                        onLongClick = {
                                            transactionToDelete = transaction
                                            showDeleteDialog = true
                                        }
                                    )
                                }
                            }
                        }

// 空状态提示
                        if (transactions.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 48.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.empty_transaction),
                                            contentDescription = "空状态图标",
                                            modifier = Modifier.size(160.dp), // 根据需要调整大小
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = "还没有账单记录",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = SlateGray,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "点击+按钮开始记账吧~",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = SlateGray.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }


        }
    }
}



@Composable
fun DateHeader(
    dateString: String,
    transactions: List<Transaction>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(WarmPaper)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = dateString.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = SlateGray,
            letterSpacing = 0.5.sp
        )
    }
}