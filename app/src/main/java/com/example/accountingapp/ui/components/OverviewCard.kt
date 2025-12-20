package com.example.accountingapp.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.R
import com.example.accountingapp.ui.theme.FreshAirBlue
import com.example.accountingapp.ui.theme.InkBlack
import com.example.accountingapp.ui.theme.PureWhite
import com.example.accountingapp.ui.theme.SlateGray
import com.example.accountingapp.ui.theme.SoftGreen
import com.example.accountingapp.ui.theme.SoftRed
import java.util.Calendar

@Composable
fun OverviewCard(
    income: Double,
    expense: Double,
    balance: Double,
    currentDate: Long,
    onDateSelect: (Calendar) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Month Selector (Top) - 新设计
                ElegantMonthSelector(
                    selectedDate = currentDate,
                    onDateSelect = onDateSelect,
                    onCalendarClick = onFilterClick
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 2. Income & Expense Row (Middle)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Income
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            "收入",
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "¥${String.format("%,.0f", income)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = SoftGreen,
                            fontSize = 28.sp
                        )
                    }

                    // Decorative Icon
                    Image(
                        painter = painterResource(id = R.drawable.ic_income_expense_arrow),
                        contentDescription = "Income to Expense",
                        modifier = Modifier.size(56.dp)
                    )

                    // Expense
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "支出",
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "¥${String.format("%,.0f", expense)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = SoftRed,
                            fontSize = 28.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // 3. Balance Section (Bottom)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "本月结余",
                        style = MaterialTheme.typography.labelMedium,
                        color = SlateGray.copy(alpha=0.6f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_coins_balance),
                            contentDescription = "Balance Coins",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "¥${String.format("%,.0f", balance)}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = FreshAirBlue,
                            fontSize = 40.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ElegantMonthSelector(
    selectedDate: Long,
    onDateSelect: (Calendar) -> Unit,
    onCalendarClick: () -> Unit
) {
    val selectedCal = remember(selectedDate) {
        Calendar.getInstance().apply { timeInMillis = selectedDate }
    }

    val currentCal = Calendar.getInstance()
    val isCurrentMonth = isSameMonth(selectedCal, currentCal)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = FreshAirBlue.copy(alpha = 0.06f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左箭头 - 上个月
        IconButton(
            onClick = {
                val newCal = selectedCal.clone() as Calendar
                newCal.add(Calendar.MONTH, -1)
                onDateSelect(newCal)
            },
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(PureWhite)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Month",
                tint = FreshAirBlue,
                modifier = Modifier.size(24.dp)
            )
        }

        // 中间日期显示 - 带动画
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onCalendarClick() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = selectedDate,
                transitionSpec = {
                    if (targetState > initialState) {
                        // 向后翻 - 从右滑入
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width } + fadeOut())
                    } else {
                        // 向前翻 - 从左滑入
                        (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> width } + fadeOut())
                    }.using(androidx.compose.animation.SizeTransform(clip = false))
                },
                label = "month_animation"
            ) { targetDate ->
                val cal = Calendar.getInstance().apply { timeInMillis = targetDate }
                val isCurrent = isSameMonth(cal, currentCal)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 年份（如果不是当前年）
                    if (cal.get(Calendar.YEAR) != currentCal.get(Calendar.YEAR)) {
                        Text(
                            text = "${cal.get(Calendar.YEAR)}年",
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateGray.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // 月份
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isCurrent) "本月" else "${cal.get(Calendar.MONTH) + 1}月",
                            style = MaterialTheme.typography.titleMedium,
                            color = FreshAirBlue,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (isCurrent) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(FreshAirBlue)
                            )
                        }
                    }
                }
            }
        }

        // 右箭头 - 下个月
        IconButton(
            onClick = {
                val newCal = selectedCal.clone() as Calendar
                newCal.add(Calendar.MONTH, 1)
                onDateSelect(newCal)
            },
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(PureWhite)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next Month",
                tint = FreshAirBlue,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * 检查两个 Calendar 是否在同一年同一月
 */
private fun isSameMonth(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
}