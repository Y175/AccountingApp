package com.example.accountingapp.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.MainViewModel
import com.example.accountingapp.TimeRange
import com.example.accountingapp.data.CategoryRanking
import com.example.accountingapp.data.TransactionType
import com.example.accountingapp.ui.components.LineChart
import com.example.accountingapp.ui.theme.YellowPrimary
import com.example.accountingapp.ui.bookkeeping.TypeTab
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.accountingapp.ui.components.AnimatedItem
import com.example.accountingapp.ui.components.PieChart
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: MainViewModel,
    onCategoryClick: (String) -> Unit
) {
    val selectedType by viewModel.statsType.collectAsState()
    val selectedTimeRange by viewModel.statsTimeRange.collectAsState()
    val chartData by viewModel.statsChartData.collectAsState()
    val pieChartData by viewModel.statsPieChartData.collectAsState()
    val totalAmount by viewModel.statsTotal.collectAsState()
    val averageAmount by viewModel.statsAverage.collectAsState()
    val rankingData by viewModel.statsRanking.collectAsState()
    
    // Custom Date Range
    val customStartDate by viewModel.customStartDate.collectAsState()
    val customEndDate by viewModel.customEndDate.collectAsState()

    // For date display (Simplified)
    val anchorDate by viewModel.statsAnchorDate.collectAsState()
    val year = anchorDate.get(Calendar.YEAR)
    val week = anchorDate.get(Calendar.WEEK_OF_YEAR)

    // Local State
    var showDatePicker by remember { mutableStateOf(false) }
    var isLineChart by remember { mutableStateOf(true) }

    if (showDatePicker) {
        val datePickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = customStartDate.timeInMillis,
            initialSelectedEndDateMillis = customEndDate.timeInMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val start = datePickerState.selectedStartDateMillis
                    val end = datePickerState.selectedEndDateMillis
                    if (start != null && end != null) {
                        viewModel.setCustomDateRange(start, end)
                        viewModel.setStatsTimeRange(TimeRange.CUSTOM)
                    }
                    showDatePicker = false
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("取消")
                }
            }
        ) {
            DateRangePicker(
                state = datePickerState,
                modifier = Modifier.weight(1f),
                title = {
                    Text(
                        text = "选择日期范围",
                        modifier = Modifier.padding(16.dp)
                    )
                },
                headline = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = datePickerState.selectedStartDateMillis?.let {
                                SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
                                    .format(Date(it))
                            } ?: "开始日期",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = datePickerState.selectedEndDateMillis?.let {
                                SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
                                    .format(Date(it))
                            } ?: "结束日期",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(YellowPrimary)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            Row {
                TypeTab("支出", selectedType == TransactionType.EXPENSE) { viewModel.setStatsType(TransactionType.EXPENSE) }
                Spacer(modifier = Modifier.width(16.dp))
                TypeTab("收入", selectedType == TransactionType.INCOME) { viewModel.setStatsType(TransactionType.INCOME) }
            }

            // Chart Type Toggle
            IconButton(onClick = { isLineChart = !isLineChart }) {
                Icon(
                    imageVector = if (isLineChart) Icons.Default.PieChart else Icons.Default.ShowChart,
                    contentDescription = "Toggle Chart",
                    tint = Color.Black
                )
            }
        }

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp)
        ) {
            // Time Range Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val ranges = listOf(
                    "周" to TimeRange.WEEK, 
                    "月" to TimeRange.MONTH, 
                    "年" to TimeRange.YEAR,
                    "自定义" to TimeRange.CUSTOM
                )
                ranges.forEach { (label, range) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (selectedTimeRange == range) YellowPrimary else Color.Transparent,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { 
                                if (range == TimeRange.CUSTOM) {
                                    showDatePicker = true
                                } else {
                                    viewModel.setStatsTimeRange(range) 
                                }
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontWeight = if (selectedTimeRange == range) FontWeight.Bold else FontWeight.Normal,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date Display
            if (selectedTimeRange == TimeRange.CUSTOM) {
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${dateFormat.format(Date(customStartDate.timeInMillis))} - ${dateFormat.format(Date(customEndDate.timeInMillis))}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Week/Month/Year Selector (Simplified Placeholder)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("$year\n${week-3}周", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    Text("$year\n${week-2}周", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    Text("$year\n${week-1}周", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    Box(
                        modifier = Modifier
                            .background(YellowPrimary, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("$year\n${week}周", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (selectedType == TransactionType.EXPENSE) "总支出" else "总收入", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    Text("¥${String.format("%.2f", totalAmount)}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
                Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.LightGray))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("平均值/天", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    Text("¥${String.format("%.2f", averageAmount)}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Charts
            Box(modifier = Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                if (isLineChart) {
                    Column {
                        LineChart(dataPoints = chartData, modifier = Modifier.weight(1f).fillMaxWidth())
                        // X Axis Labels (Simplified)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val labels = if (selectedTimeRange == TimeRange.WEEK) {
                                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            } else {
                                listOf("Start", "...", "End")
                            }
                            labels.forEach {
                                Text(it, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 10.sp)
                            }
                        }
                    }
                } else {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = true,
                        enter = androidx.compose.animation.scaleIn() + androidx.compose.animation.fadeIn()
                    ) {
                        if (pieChartData.isNotEmpty()) {
                            val colors = listOf(
                                Color(0xFFFFC107), Color(0xFFFF5722), Color(0xFF4CAF50), 
                                Color(0xFF2196F3), Color(0xFF9C27B0), Color(0xFF607D8B)
                            )
                            val coloredData = pieChartData.mapIndexed { index, item ->
                                item.copy(color = colors[index % colors.size])
                            }
                            PieChart(data = coloredData, modifier = Modifier.size(180.dp))
                        } else {
                            Text("暂无数据", color = Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(if (selectedType == TransactionType.EXPENSE) "支出排行" else "收入排行", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(rankingData) { item ->
                    AnimatedItem {
                        RankingListItem(
                            item = item,
                            onClick = { onCategoryClick(item.categoryName) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RankingListItem(item: CategoryRanking, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder icon
            Icon(Icons.Default.Category, contentDescription = null, modifier = Modifier.size(16.dp))
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.categoryName, fontWeight = FontWeight.Bold)
                Text("¥${String.format("%.2f", item.amount)}", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = item.percentage,
                    modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = YellowPrimary,
                    trackColor = Color(0xFFEEEEEE)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("${(item.percentage * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}
