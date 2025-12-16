package com.example.accountingapp.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.ui.theme.BlackPrimary
import com.example.accountingapp.ui.theme.ExpenseRed
import com.example.accountingapp.ui.theme.GrayText
import com.example.accountingapp.ui.theme.IncomeGreen
import com.example.accountingapp.ui.theme.YellowPrimary

@Composable
fun OverviewCard(
    income: Double,
    expense: Double,
    balance: Double,
    currentMonth: String,
    onMonthClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.White, YellowPrimary.copy(alpha=0.08f))
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Month Selector - Centered and elegant
                Box(
                    modifier = Modifier
                        .clickable { onMonthClick() }
                        .background(YellowPrimary.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentMonth.replace("\n", " "),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.ArrowDropDown, 
                            contentDescription = null, 
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Balance Layout
                Text(
                    text = "本月结余",
                    style = MaterialTheme.typography.labelMedium,
                    color = GrayText,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = String.format("%.2f", balance),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 48.sp,
                        // 或者直接使用权重值
                        fontWeight = FontWeight(900),
                        letterSpacing = (-1).sp
                    ),
                    color = BlackPrimary
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Income/Expense Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Income
                    Column(horizontalAlignment = Alignment.Start) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(IncomeGreen, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("收入", style = MaterialTheme.typography.labelMedium, color = GrayText)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("%.2f", income),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = BlackPrimary
                        )
                    }
                    
                    // Expense
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(ExpenseRed, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("支出", style = MaterialTheme.typography.labelMedium, color = GrayText)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("%.2f", expense),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = BlackPrimary
                        )
                    }
                }
            }
        }
    }
}
