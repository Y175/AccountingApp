package com.example.accountingapp.ui.statistics

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.accountingapp.ui.components.AnimatedItem
import com.example.accountingapp.ui.components.LineChart
import com.example.accountingapp.ui.components.PieChart
import com.example.accountingapp.ui.home.TypeTab
import com.example.accountingapp.ui.theme.ApricotOrange
import com.example.accountingapp.ui.theme.ChartBlue
import com.example.accountingapp.ui.theme.ChartGreen
import com.example.accountingapp.ui.theme.ChartOrange
import com.example.accountingapp.ui.theme.ChartPurple
import com.example.accountingapp.ui.theme.FreshAirBlue
import com.example.accountingapp.ui.theme.SoftGreen
import com.example.accountingapp.ui.theme.SoftRed
import com.example.accountingapp.ui.theme.WarmPaper
import com.example.accountingapp.util.CategoryIcons
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

    // For date display
    val anchorDate by viewModel.statsAnchorDate.collectAsState()

    // Local State
    var showDatePicker by remember { mutableStateOf(false) }
    var isLineChart by remember { mutableStateOf(true) }
    var selectedOffset by remember { mutableStateOf(0) }

    LaunchedEffect(selectedTimeRange) {
        selectedOffset = 0
        viewModel.setStatsAnchorDate(Calendar.getInstance())
    }

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
            .background(ApricotOrange) // Header Color
    ) {
        // Fixed Header - 固定在顶部
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                TypeTab("支出", selectedType == TransactionType.EXPENSE) {
                    viewModel.setStatsType(TransactionType.EXPENSE)
                }
                Spacer(modifier = Modifier.width(16.dp))
                TypeTab("收入", selectedType == TransactionType.INCOME) {
                    viewModel.setStatsType(TransactionType.INCOME)
                }
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

        // Scrollable Content - Floating White Sheet
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmPaper, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .padding(horizontal = 16.dp), // Add padding to content inside
             contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 80.dp, top = 24.dp)
        ) {
            // Time Range Selector
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
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
                                    if (selectedTimeRange == range) ApricotOrange else Color.Transparent,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    if (range == TimeRange.CUSTOM) {
                                        showDatePicker = true
                                    } else {
                                        viewModel.setStatsTimeRange(range)
                                        selectedOffset = 0
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
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Date Display
            item {
                if (selectedTimeRange == TimeRange.CUSTOM) {
                    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${dateFormat.format(Date(customStartDate.timeInMillis))} - ${
                                dateFormat.format(Date(customEndDate.timeInMillis))
                            }",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    // Week/Month/Year Selector
                    val currentCalendar = Calendar.getInstance()
                    val currentYear = currentCalendar.get(Calendar.YEAR)
                    val currentWeek = currentCalendar.get(Calendar.WEEK_OF_YEAR)
                    val currentMonth = currentCalendar.get(Calendar.MONTH) + 1

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        when (selectedTimeRange) {
                            TimeRange.WEEK -> {
                                for (offset in -3..0) {
                                    val displayWeek = currentWeek + offset
                                    val displayYear = if (displayWeek <= 0) currentYear - 1 else currentYear
                                    val adjustedWeek = if (displayWeek <= 0) 52 + displayWeek else displayWeek

                                    Box(
                                        modifier = Modifier
                                            .then(
                                                if (offset == selectedOffset) {
                                                    Modifier.background(ApricotOrange, RoundedCornerShape(8.dp))
                                                } else {
                                                    Modifier
                                                }
                                            )
                                            .clickable {
                                                selectedOffset = offset
                                                viewModel.navigateToWeek(offset)
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            "$displayYear\n第${adjustedWeek}周",
                                            fontWeight = if (selectedOffset == offset) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selectedOffset == offset) Color.Black else Color.Gray,
                                            style = MaterialTheme.typography.labelSmall,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }

                            TimeRange.MONTH -> {
                                for (offset in -3..0) {
                                    val displayMonth = currentMonth + offset
                                    val displayYear = if (displayMonth <= 0) currentYear - 1 else currentYear
                                    val adjustedMonth = if (displayMonth <= 0) 12 + displayMonth else displayMonth

                                    Box(
                                        modifier = Modifier
                                            .then(
                                                if (offset == selectedOffset) {
                                                    Modifier.background(ApricotOrange, RoundedCornerShape(8.dp))
                                                } else {
                                                    Modifier
                                                }
                                            )
                                            .clickable {
                                                selectedOffset = offset
                                                viewModel.navigateToMonth(offset)
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            "$displayYear\n${adjustedMonth}月",
                                            fontWeight = if (selectedOffset == offset) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selectedOffset == offset) Color.Black else Color.Gray,
                                            style = MaterialTheme.typography.labelSmall,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }

                            TimeRange.YEAR -> {
                                for (offset in -3..0) {
                                    val displayYear = currentYear + offset

                                    Box(
                                        modifier = Modifier
                                            .then(
                                                if (offset == selectedOffset) {
                                                    Modifier.background(ApricotOrange, RoundedCornerShape(8.dp))
                                                } else {
                                                    Modifier
                                                }
                                            )
                                            .clickable {
                                                selectedOffset = offset
                                                viewModel.navigateToYear(offset)
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            "${displayYear}年",
                                            fontWeight = if (selectedOffset == offset) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selectedOffset == offset) Color.Black else Color.Gray,
                                            style = MaterialTheme.typography.labelSmall,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Stats Summary
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            if (selectedType == TransactionType.EXPENSE) "总支出" else "总收入",
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            "¥${String.format("%.2f", totalAmount)}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color.LightGray)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "平均值/天",
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            "¥${String.format("%.2f", averageAmount)}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Charts - 增大图表高度
            item {
                Box(
                    modifier = Modifier
                        .height(280.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLineChart) {
                        Column {
                            LineChart(
                                dataPoints = chartData,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            )
                            // X Axis Labels
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val labels = when (selectedTimeRange) {
                                    TimeRange.WEEK -> {
                                        listOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
                                            .take(chartData.size)
                                    }
                                    TimeRange.MONTH -> {
                                        // 根据数据点数量均匀分布标签
                                        val dataSize = chartData.size
                                        if (dataSize <= 1) {
                                            listOf("1日")
                                        } else {
                                            List(minOf(dataSize, 5)) { index ->
                                                val day = (index * 30 / (minOf(dataSize, 5) - 1)) + 1
                                                "${day}日"
                                            }
                                        }
                                    }
                                    TimeRange.YEAR -> {
                                        // 根据数据点数量显示月份
                                        val dataSize = chartData.size
                                        if (dataSize <= 1) {
                                            listOf("1月")
                                        } else {
                                            List(minOf(dataSize, 6)) { index ->
                                                val month = (index * 12 / (minOf(dataSize, 6) - 1)) + 1
                                                "${month}月"
                                            }
                                        }
                                    }
                                    else -> {
                                        when {
                                            chartData.isEmpty() -> listOf()
                                            chartData.size == 1 -> listOf("当前")
                                            else -> listOf("开始", "结束")
                                        }
                                    }
                                }
                                labels.forEach {
                                    Text(
                                        it,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray,
                                        fontSize = 10.sp
                                    )
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
                                    ChartBlue, ChartOrange, ChartPurple, ChartGreen,
                                    SoftRed, SoftGreen, FreshAirBlue
                                )
                                val coloredData = pieChartData.mapIndexed { index, item ->
                                    item.copy(color = colors[index % colors.size])
                                }
                                PieChart(data = coloredData, modifier = Modifier.size(240.dp))
                            } else {
                                Text("暂无数据", color = Color.Gray)
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Ranking Title
            item {
                Text(
                    if (selectedType == TransactionType.EXPENSE) "支出排行" else "收入排行",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Ranking List - 使用 items 直接添加
            items(rankingData) { item ->
                AnimatedItem {
                    RankingListItem(
                        item = item,
                        onClick = { onCategoryClick(item.categoryName) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // 底部留白
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun RankingListItem(
    item: CategoryRanking,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = CategoryIcons.getIcon(item.iconName),
                contentDescription = item.categoryName,
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
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
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = ApricotOrange,
                    trackColor = Color(0xFFEEEEEE)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "${(item.percentage * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}
