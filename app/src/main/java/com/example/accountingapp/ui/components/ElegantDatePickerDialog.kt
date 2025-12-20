package com.example.accountingapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.accountingapp.ui.theme.*
import java.util.*

@Composable
fun ElegantDatePickerDialog(
    selectedDate: Long,
    onDateSelected: (Calendar) -> Unit,
    onDismiss: () -> Unit
) {
    var currentYear by remember {
        mutableStateOf(Calendar.getInstance().apply { timeInMillis = selectedDate }.get(Calendar.YEAR))
    }

    val selectedCal = remember(selectedDate) {
        Calendar.getInstance().apply { timeInMillis = selectedDate }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        // 半透明背景
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(InkBlack.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            // 日期选择卡片
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {} // 阻止点击穿透
                    ),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // 顶部标题栏
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "选择月份",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = InkBlack,
                            fontSize = 22.sp
                        )

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(SlateGray.copy(alpha = 0.1f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = SlateGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 年份选择器
                    YearSelector(
                        currentYear = currentYear,
                        onYearChange = { currentYear = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 月份网格
                    MonthGrid(
                        currentYear = currentYear,
                        selectedDate = selectedCal,
                        onMonthSelected = { month ->
                            val newCal = Calendar.getInstance().apply {
                                set(Calendar.YEAR, currentYear)
                                set(Calendar.MONTH, month)
                                set(Calendar.DAY_OF_MONTH, 1)
                            }
                            onDateSelected(newCal)
                            onDismiss()
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 快捷操作
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val today = Calendar.getInstance()
                                currentYear = today.get(Calendar.YEAR)
                                onDateSelected(today)
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = FreshAirBlue
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                FreshAirBlue.copy(alpha = 0.3f)
                            )
                        ) {
                            Text(
                                "本月",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YearSelector(
    currentYear: Int,
    onYearChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = FreshAirBlue.copy(alpha = 0.08f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 上一年
        IconButton(
            onClick = { onYearChange(currentYear - 1) },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PureWhite)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Year",
                tint = FreshAirBlue,
                modifier = Modifier.size(24.dp)
            )
        }

        // 年份显示
        Text(
            text = "${currentYear}年",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = FreshAirBlue,
            fontSize = 20.sp
        )

        // 下一年
        IconButton(
            onClick = { onYearChange(currentYear + 1) },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PureWhite)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next Year",
                tint = FreshAirBlue,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun MonthGrid(
    currentYear: Int,
    selectedDate: Calendar,
    onMonthSelected: (Int) -> Unit
) {
    val months = remember {
        listOf(
            "1月", "2月", "3月",
            "4月", "5月", "6月",
            "7月", "8月", "9月",
            "10月", "11月", "12月"
        )
    }

    val currentCal = Calendar.getInstance()
    val isCurrentYear = currentYear == currentCal.get(Calendar.YEAR)

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(months.size) { index ->
            val isSelected = currentYear == selectedDate.get(Calendar.YEAR) &&
                    index == selectedDate.get(Calendar.MONTH)
            val isCurrentMonth = isCurrentYear && index == currentCal.get(Calendar.MONTH)

            MonthCard(
                monthText = months[index],
                isSelected = isSelected,
                isCurrentMonth = isCurrentMonth,
                onClick = { onMonthSelected(index) }
            )
        }
    }
}

@Composable
fun MonthCard(
    monthText: String,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> FreshAirBlue
        isCurrentMonth -> FreshAirBlue.copy(alpha = 0.15f)
        else -> SlateGray.copy(alpha = 0.05f)
    }

    val textColor = when {
        isSelected -> PureWhite
        isCurrentMonth -> FreshAirBlue
        else -> InkBlack
    }

    Box(
        modifier = Modifier
            .aspectRatio(1.5f)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = monthText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isSelected || isCurrentMonth) FontWeight.Bold else FontWeight.Medium,
                color = textColor,
                fontSize = 16.sp
            )

            if (isCurrentMonth && !isSelected) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(FreshAirBlue)
                )
            }
        }
    }
}