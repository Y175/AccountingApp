package com.example.accountingapp.ui.home

import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.MainViewModel
import com.example.accountingapp.data.Category
import com.example.accountingapp.data.TransactionType
import com.example.accountingapp.ui.theme.InkBlack
import com.example.accountingapp.ui.theme.SlateGray
import com.example.accountingapp.ui.theme.PureWhite
import com.example.accountingapp.ui.theme.WarmPaper
import com.example.accountingapp.ui.theme.FreshAirBlue
import com.example.accountingapp.ui.theme.ApricotOrange
import com.example.accountingapp.ui.theme.GrayText
import com.example.accountingapp.util.CategoryIcons
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    viewModel: MainViewModel,
    transactionId: Int,  // 改为 Long
    onBack: () -> Unit
) {
    val transaction by viewModel.getTransaction(transactionId).collectAsState(initial = null)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Edit States
    var showCategorySheet by remember { mutableStateOf(false) }
    var showAmountSheet by remember { mutableStateOf(false) }
    var showDateSheet by remember { mutableStateOf(false) }
    var showNoteSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Temporary states for editing
    var tempAmount by remember { mutableStateOf("") }
    var tempNote by remember { mutableStateOf("") }

    LaunchedEffect(transaction) {
        transaction?.let {
            tempAmount = it.amount.toString()
            tempNote = it.note
        }
    }

    if (transaction == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("加载中...")
        }
        return
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FreshAirBlue)
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回", tint = PureWhite)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "账单详情",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "删除", tint = PureWhite)
                }
            }
        },
        containerColor = FreshAirBlue
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(FreshAirBlue)
        ) {
            // Top Icon + Category - Large and Centered
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FreshAirBlue)
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(PureWhite, CircleShape)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = CategoryIcons.getIcon(transaction!!.categoryIcon),
                        contentDescription = transaction!!.categoryName,
                        modifier = Modifier.size(40.dp),
                        tint = InkBlack
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = transaction!!.categoryName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
            }

            // Details List - Card Style
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .offset(y = (-20).dp) // Overlap with header
                    .background(WarmPaper, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .padding(vertical = 8.dp)
            ) {
                DetailItem(
                    label = "类型",
                    value = if (transaction!!.type == TransactionType.INCOME) "收入" else "支出",
                    onClick = { showCategorySheet = true }
                )
                DetailItem(
                    label = "金额",
                    value = String.format("%.2f", transaction!!.amount),
                    onClick = {
                        tempAmount = transaction!!.amount.toString()
                        showAmountSheet = true
                    }
                )
                DetailItem(
                    label = "日期",
                    value = SimpleDateFormat(
                        "yyyy年MM月dd日",
                        Locale.CHINA
                    ).format(Date(transaction!!.date)),
                    onClick = { showDateSheet = true }
                )
                DetailItem(
                    label = "备注",
                    value = transaction!!.note.ifEmpty { "无备注" },
                    onClick = {
                        tempNote = transaction!!.note
                        showNoteSheet = true
                    }
                )
            }
        }

        // --- Bottom Sheets ---

        // 1. Category & Type Sheet
        if (showCategorySheet) {
            CategorySelectionSheet(
                currentType = transaction!!.type,
                currentCategoryId = transaction!!.categoryId,
                viewModel = viewModel,
                onDismiss = { showCategorySheet = false },
                onCategorySelected = { category ->
                    scope.launch {
                        viewModel.updateTransaction(
                            transaction!!.copy(
                                categoryId = category.id,
                                categoryName = category.name,
                                categoryIcon = category.iconName,
                                type = category.type
                            )
                        )
                        showCategorySheet = false
                    }
                }
            )
        }

        // 2. Amount Sheet
        if (showAmountSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAmountSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("编辑金额", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = tempAmount,
                        onValueChange = { tempAmount = it },
                        label = { Text("金额") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val amount = tempAmount.toDoubleOrNull()
                            if (amount != null && amount > 0) {
                                scope.launch {
                                    viewModel.updateTransaction(transaction!!.copy(amount = amount))
                                    showAmountSheet = false
                                }
                            } else {
                                Toast.makeText(context, "请输入有效金额", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ApricotOrange)
                    ) {
                        Text("确定", color = PureWhite)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        // 3. Date Sheet
        if (showDateSheet) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = transaction!!.date
            )
            DatePickerDialog(
                onDismissRequest = { showDateSheet = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { newDate ->
                            scope.launch {
                                viewModel.updateTransaction(transaction!!.copy(date = newDate))
                                showDateSheet = false
                            }
                        }
                    }) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDateSheet = false }) {
                        Text("取消")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // 4. Note Sheet
        if (showNoteSheet) {
            ModalBottomSheet(
                onDismissRequest = { showNoteSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("编辑备注", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = tempNote,
                        onValueChange = { tempNote = it },
                        label = { Text("备注") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.updateTransaction(transaction!!.copy(note = tempNote))
                                showNoteSheet = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ApricotOrange)
                    ) {
                        Text("确定", color = PureWhite)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        // Delete Dialog
        if (showDeleteDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("删除账单") },
                text = { Text("确定要删除这条记录吗？") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteTransaction(transaction!!)
                            showDeleteDialog = false
                            onBack()
                        }
                    ) {
                        Text("删除", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = SlateGray)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = InkBlack
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = GrayText.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
    // Divider
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 8.dp),
        thickness = 0.5.dp,
        color = GrayText.copy(alpha = 0.1f)
    )
}

// Category Selection Sheet with Type Switching
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionSheet(
    currentType: TransactionType,
    currentCategoryId: Int,
    viewModel: MainViewModel,
    onDismiss: () -> Unit,
    onCategorySelected: (Category) -> Unit
) {
    var selectedType by remember { mutableStateOf(currentType) }
    val incomeCategories by viewModel.incomeCategories.collectAsState()
    val expenseCategories by viewModel.expenseCategories.collectAsState()

    val categories =
        if (selectedType == TransactionType.EXPENSE) expenseCategories else incomeCategories

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        ) {
            // Type Tab
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TypeTab(
                    text = "支出",
                    isSelected = selectedType == TransactionType.EXPENSE,
                    onClick = { selectedType = TransactionType.EXPENSE }
                )
                Spacer(modifier = Modifier.width(16.dp))
                TypeTab(
                    text = "收入",
                    isSelected = selectedType == TransactionType.INCOME,
                    onClick = { selectedType = TransactionType.INCOME }
                )
            }

            // Category Grid
            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 32.dp)
            ) {
                items(categories.size) { index ->
                    val cat = categories[index]
                    CategoryItemInSheet(
                        category = cat,
                        isSelected = cat.id == currentCategoryId && cat.type == selectedType,
                        onClick = { onCategorySelected(cat) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryItemInSheet(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (isSelected) ApricotOrange else com.example.accountingapp.ui.theme.LightGray,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = CategoryIcons.getIcon(category.iconName),
                contentDescription = category.name,
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.name,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}

@Composable
fun TypeTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) ApricotOrange else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) PureWhite else InkBlack
        )
    }
}