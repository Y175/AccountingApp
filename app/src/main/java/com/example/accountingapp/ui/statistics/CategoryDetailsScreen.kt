package com.example.accountingapp.ui.statistics

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.accountingapp.MainViewModel
import com.example.accountingapp.ui.components.AnimatedItem
import com.example.accountingapp.ui.components.TransactionItem
import com.example.accountingapp.ui.theme.ApricotOrange
import com.example.accountingapp.ui.theme.WarmPaper

@Composable
fun CategoryDetailsScreen(
    viewModel: MainViewModel,
    categoryName: String,
    onBack: () -> Unit
) {
    val statsTransactions by viewModel.statsTransactions.collectAsState()
    val categoryTransactions = statsTransactions.filter { it.categoryName == categoryName }
    val totalAmount = categoryTransactions.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ApricotOrange)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
            
            Text(
                text = categoryName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Summary Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("总计", style = MaterialTheme.typography.labelMedium, color = Color.Black.copy(alpha = 0.6f))
            Text(
                text = "¥${String.format("%.2f", totalAmount)}",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Transaction List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmPaper, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            if (categoryTransactions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("暂无记录", color = Color.Gray)
                }
            } else {
                LazyColumn {
                    items(categoryTransactions) { transaction ->
                        AnimatedItem {
                            TransactionItem(transaction = transaction)
                        }
                    }
                }
            }
        }
    }
}
