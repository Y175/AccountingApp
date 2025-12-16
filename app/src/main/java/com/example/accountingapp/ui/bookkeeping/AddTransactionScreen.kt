package com.example.accountingapp.ui.bookkeeping

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.MainViewModel
import com.example.accountingapp.data.Category
import com.example.accountingapp.data.Transaction
import com.example.accountingapp.data.TransactionType
import com.example.accountingapp.ui.components.CustomKeypad
import com.example.accountingapp.ui.theme.YellowPrimary
import com.example.accountingapp.util.CategoryIcons

@Composable
fun AddTransactionScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amountStr by remember { mutableStateOf("0") }
    var note by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    val expenseCategories by viewModel.expenseCategories.collectAsState()
    val incomeCategories by viewModel.incomeCategories.collectAsState()
    
    val currentCategories = if (selectedType == TransactionType.EXPENSE) expenseCategories else incomeCategories

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with Type Switcher
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(YellowPrimary)
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .clickable { onBack() }
            )
            
            Row(
                modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = Arrangement.Center
            ) {
                TypeTab(
                    text = "支出",
                    selected = selectedType == TransactionType.EXPENSE,
                    onClick = { selectedType = TransactionType.EXPENSE }
                )
                Spacer(modifier = Modifier.width(16.dp))
                TypeTab(
                    text = "收入",
                    selected = selectedType == TransactionType.INCOME,
                    onClick = { selectedType = TransactionType.INCOME }
                )
            }
        }

        // Category Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            items(currentCategories) { category ->
                CategoryItem(
                    category = category,
                    isSelected = selectedCategory?.id == category.id,
                    onClick = { selectedCategory = category }
                )
            }
            item {
                CategoryItem(
                    category = Category(name = "设置", iconName = "settings", type = TransactionType.EXPENSE), // Dummy
                    isSelected = false,
                    onClick = { /* Open Settings */ },
                    isSettings = true
                )
            }
        }

        // Input Area
        androidx.compose.animation.AnimatedVisibility(
            visible = selectedCategory != null,
            enter = androidx.compose.animation.slideInVertically { it } + androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.slideOutVertically { it } + androidx.compose.animation.fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
            ) {
// Amount and Note Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(YellowPrimary, CircleShape)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // 显示选中分类的实际图标
                        Icon(
                            imageVector = CategoryIcons.getIcon(selectedCategory?.iconName ?: "Category"),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = selectedCategory?.name ?: "选择分类",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "¥$amountStr",  // 建议加上货币符号
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Note Input (Simplified)
                TextField(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = { Text("点击写备注...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                // Keypad
                CustomKeypad(
                    onNumberClick = { num ->
                        if (amountStr == "0") amountStr = num else amountStr += num
                    },
                    onDeleteClick = {
                        if (amountStr.isNotEmpty()) {
                            amountStr = amountStr.dropLast(1)
                            if (amountStr.isEmpty()) amountStr = "0"
                        }
                    },
                    onDateClick = { /* Show Date Picker */ },
                    onOkClick = {
                        if (selectedCategory != null) {
                            val amount = amountStr.toDoubleOrNull() ?: 0.0
                            if (amount > 0) {
                                viewModel.addTransaction(
                                    Transaction(
                                        amount = amount,
                                        categoryId = selectedCategory!!.id,
                                        categoryName = selectedCategory!!.name,
                                        categoryIcon = selectedCategory!!.iconName,
                                        type = selectedType,
                                        note = note,
                                        date = System.currentTimeMillis()
                                    )
                                )
                                onBack()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TypeTab(text: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(2.dp)
                    .background(Color.Black)
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit,
    isSettings: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (isSelected) YellowPrimary else Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSettings) {
                    Icons.Default.Settings
                } else {
                    CategoryIcons.getIcon(category.iconName)  // 使用工具类
                },
                contentDescription = category.name,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)  // 可选：统一图标大小
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black
        )
    }
}
