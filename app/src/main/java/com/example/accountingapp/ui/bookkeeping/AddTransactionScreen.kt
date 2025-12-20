package com.example.accountingapp.ui.bookkeeping

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.accountingapp.ui.theme.*
import com.example.accountingapp.util.CategoryIcons

private enum class AddTransactionState {
    SELECT_CATEGORY,
    ENTER_AMOUNT
}

@Composable
fun AddTransactionScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var screenState by remember { mutableStateOf(AddTransactionState.SELECT_CATEGORY) }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    
    // Transaction Data
    var amountStr by remember { mutableStateOf("0") }
    var note by remember { mutableStateOf("") }

    val expenseCategories by viewModel.expenseCategories.collectAsState()
    val incomeCategories by viewModel.incomeCategories.collectAsState()
    
    val currentCategories = if (selectedType == TransactionType.EXPENSE) expenseCategories else incomeCategories

    // Reset data when entering selection mode if needed? 
    // No, maybe we want to persist if user goes back and forth.
    // However, if category changes, maybe reset amount? Let's keep it simple.

    AnimatedContent(
        targetState = screenState,
        transitionSpec = {
            if (targetState == AddTransactionState.ENTER_AMOUNT) {
                slideInHorizontally { width -> width } + fadeIn() togetherWith
                slideOutHorizontally { width -> -width } + fadeOut()
            } else {
                slideInHorizontally { width -> -width } + fadeIn() togetherWith
                slideOutHorizontally { width -> width } + fadeOut()
            }
        },
        label = "ScreenTransition"
    ) { state ->
        when (state) {
            AddTransactionState.SELECT_CATEGORY -> {
                CategorySelectionContent(
                    selectedType = selectedType,
                    onTypeSelected = { selectedType = it },
                    categories = currentCategories,
                    onCategoryClick = { category ->
                        selectedCategory = category
                        amountStr = "0" // Reset amount for new entry
                        note = ""
                        screenState = AddTransactionState.ENTER_AMOUNT
                    },
                    onBack = onBack
                )
            }
            AddTransactionState.ENTER_AMOUNT -> {
                TransactionEntryContent(
                    category = selectedCategory!!,
                    amountStr = amountStr,
                    note = note,
                    onAmountChange = { amountStr = it },
                    onNoteChange = { note = it },
                    onBack = { screenState = AddTransactionState.SELECT_CATEGORY },
                    onSave = {
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
                                onBack() // Exit screen
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CategorySelectionContent(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit,
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Toggle Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
             // We can just use a simple back icon if needed, or rely on system back. 
             // But UI shows a pill toggle at the top.
             // Let's add a back button for safety/consistency.
             Icon(
                 Icons.Default.ArrowBack,
                 contentDescription = "Back",
                 modifier = Modifier
                     .clickable { onBack() }
                     .padding(end = 16.dp)
             )
             
             Spacer(modifier = Modifier.weight(1f))
             
             // Pill Toggle
             Box(
                 modifier = Modifier
                     .clip(RoundedCornerShape(50))
                     .background(Color(0xFFF0F0F0))
                     .padding(4.dp)
             ) {
                 Row {
                     TypePill(
                         text = "支出",
                         isSelected = selectedType == TransactionType.EXPENSE,
                         selectedColor = SoftRed, // Or PastelRed
                         onClick = { onTypeSelected(TransactionType.EXPENSE) }
                     )
                     TypePill(
                         text = "收入",
                         isSelected = selectedType == TransactionType.INCOME,
                         selectedColor = SoftGreen, // Or PastelGreen
                         onClick = { onTypeSelected(TransactionType.INCOME) }
                     )
                 }
             }
             
             Spacer(modifier = Modifier.weight(1f))
             Spacer(modifier = Modifier.width(32.dp)) // Balance the back button space
        }

        // Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // UI shows 3 columns
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(categories) { category ->
                CategoryCard(category = category, onClick = { onCategoryClick(category) })
            }
            item {
                 CategoryCard(
                    category = Category(name = "设置", iconName = "settings", type = TransactionType.EXPENSE),
                    onClick = { /* TODO */ },
                    isSettings = true
                )
            }
        }
    }
}

@Composable
fun TypePill(
    text: String,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) selectedColor.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) selectedColor else Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit,
    isSettings: Boolean = false
) {
    // Generate a pastel color based on hash or just random/fixed
    // For now, let's pick from our palette
    val bgColors = listOf(PastelPink, PastelBlue, PastelGreen, PastelYellow, PastelPurple, PastelOrange)
    val bgColor = if (isSettings) Color(0xFFEEEEEE) else bgColors[category.id % bgColors.size]

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(72.dp) // Larger icons as per design
                .clip(RoundedCornerShape(24.dp)) // Squircle / Cat-like soft round
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSettings) Icons.Default.Settings else CategoryIcons.getIcon(category.iconName),
                contentDescription = category.name,
                tint = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
fun TransactionEntryContent(
    category: Category,
    amountStr: String,
    note: String,
    onAmountChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { onBack() }
            )
            Text(
                text = "添加记录", // Add Transaction
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large Amount Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFFAFAFA)) // Warm Paper
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$$amountStr", // Using $ as per design image, or ¥? Plan says ¥, image says $. User said "Text use Chinese". 
                // Let's use currency symbol based on locale or just nothing? 
                // The design image shows $. The previous code had ¥. Let's stick to simple display.
                // Or maybe just the number. Let's add $ as requested by the image look-alike, or ¥ if it's Chinese context.
                // Assuming Chinese context based on "文字还是用中文".
                color = SoftBlue,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Note Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selected Category Icon
            val bgColors = listOf(PastelPink, PastelBlue, PastelGreen, PastelYellow, PastelPurple, PastelOrange)
            val bgColor = bgColors[category.id % bgColors.size]
            
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = CategoryIcons.getIcon(category.iconName),
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Note Field
            TextField(
                value = note,
                onValueChange = onNoteChange,
                placeholder = { Text("备注...") }, // Notes
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Custom Keypad
        CustomKeypad(
            onNumberClick = { num ->
                if (amountStr == "0") onAmountChange(num) else onAmountChange(amountStr + num)
            },
            onDeleteClick = {
                if (amountStr.isNotEmpty()) {
                    val newStr = amountStr.dropLast(1)
                    onAmountChange(if (newStr.isEmpty()) "0" else newStr)
                }
            },
            onDateClick = { /* TODO: Date Picker */ },
            onOkClick = onSave
        )
    }
}
