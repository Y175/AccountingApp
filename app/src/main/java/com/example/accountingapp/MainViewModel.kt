package com.example.accountingapp

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.accountingapp.data.AccountingRepository
import com.example.accountingapp.data.Category
import com.example.accountingapp.data.CategoryRanking
import com.example.accountingapp.data.Transaction
import com.example.accountingapp.data.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
// Helper extension for flatMapLatest since it's experimental in some versions or requires specific imports
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class MainViewModel(private val repository: AccountingRepository) : ViewModel() {

    // Current selected month (start and end timestamps)
    // Date Filter State
    private val _filterType = MutableStateFlow(DateFilterType.MONTH)
    val filterType: StateFlow<DateFilterType> = _filterType

    private val _currentDateRangeStart = MutableStateFlow(getStartOfMonth(Calendar.getInstance()))
    private val _currentDateRangeEnd = MutableStateFlow(getEndOfMonth(Calendar.getInstance()))

    // Transactions for the current filtered date range
    val currentTransactions: StateFlow<List<Transaction>> = combine(
        _currentDateRangeStart,
        _currentDateRangeEnd
    ) { start, end ->
        repository.getTransactionsByDateRange(start, end)
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Financial Overview (Income/Expense)
    val currentIncome: StateFlow<Double> = combine(
        _currentDateRangeStart,
        _currentDateRangeEnd
    ) { start, end ->
        repository.getIncomeSum(start, end)
    }.flatMapLatest { it }
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val currentExpense: StateFlow<Double> = combine(
        _currentDateRangeStart,
        _currentDateRangeEnd
    ) { start, end ->
        repository.getExpenseSum(start, end)
    }.flatMapLatest { it }
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Categories
    val expenseCategories: StateFlow<List<Category>> = repository.getCategoriesByType(TransactionType.EXPENSE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val incomeCategories: StateFlow<List<Category>> = repository.getCategoriesByType(TransactionType.INCOME)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
    }

    fun updateTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.update(transaction)
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.delete(transaction)
    }

    fun getTransaction(id: Int): Flow<Transaction?> = repository.getTransaction(id)

    // Current Display String (e.g., "2024年\n5月" or "2024年\n第12周")
    private val _dateDisplay = MutableStateFlow("")
    val dateDisplay: StateFlow<String> = _dateDisplay

    init {
        updateDateDisplay(Calendar.getInstance(), DateFilterType.MONTH)
    }

    fun setDateFilter(type: DateFilterType, calendar: Calendar) {
        _filterType.value = type
        updateDateDisplay(calendar, type)

        val newStart: Long
        val newEnd: Long

        when (type) {
            DateFilterType.MONTH -> {
                newStart = getStartOfMonth(calendar)
                newEnd = getEndOfMonth(calendar)
            }
            DateFilterType.WEEK -> {
                newStart = getStartOfWeek(calendar)
                newEnd = getEndOfWeek(calendar)
            }
        }
        _currentDateRangeStart.value = newStart
        _currentDateRangeEnd.value = newEnd
    }

    private fun updateDateDisplay(calendar: Calendar, type: DateFilterType) {
        val year = calendar.get(Calendar.YEAR)
        if (type == DateFilterType.MONTH) {
            val month = calendar.get(Calendar.MONTH) + 1
            _dateDisplay.value = "${year}年\n${month}月"
        } else {
            val week = calendar.get(Calendar.WEEK_OF_YEAR)
            _dateDisplay.value = "${year}年\n第${week}周"
        }
    }
    
    private fun getStartOfWeek(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getEndOfWeek(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }
    
    private fun getStartOfMonth(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getEndOfMonth(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    // --- Statistics Logic ---

    private val _statsType = MutableStateFlow(TransactionType.EXPENSE)
    val statsType: StateFlow<TransactionType> = _statsType

    private val _statsTimeRange = MutableStateFlow(TimeRange.WEEK)
    val statsTimeRange: StateFlow<TimeRange> = _statsTimeRange

    // Anchor date for statistics (defaults to now)
    private val _statsAnchorDate = MutableStateFlow(Calendar.getInstance())
    val statsAnchorDate: StateFlow<Calendar> = _statsAnchorDate

    // Custom Date Range State
    private val _customStartDate = MutableStateFlow(Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -7) })
    val customStartDate: StateFlow<Calendar> = _customStartDate
    
    private val _customEndDate = MutableStateFlow(Calendar.getInstance())
    val customEndDate: StateFlow<Calendar> = _customEndDate

    // Calculated start/end for the current stats view
    private val _statsDateRange = combine(_statsTimeRange, _statsAnchorDate, _customStartDate, _customEndDate) { range, anchor, customStart, customEnd ->
        calculateDateRange(range, anchor, customStart, customEnd)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(0L, 0L))

    val statsTransactions = _statsDateRange.flatMapLatest { (start, end) ->
        repository.getTransactionsByDateRange(start, end)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val statsTotal = combine(statsTransactions, _statsType) { transactions, type ->
        transactions.filter { it.type == type }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val statsAverage = combine(statsTotal, _statsDateRange) { total, (start, end) ->
        val days = ((end - start) / (1000 * 60 * 60 * 24)) + 1
        if (days > 0) total / days else 0.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val statsRanking = combine(statsTransactions, _statsType, statsTotal) { transactions, type, total ->
        val filtered = transactions.filter { it.type == type }
        filtered.groupBy { it.categoryName }
            .map { (name, txs) ->
                val sum = txs.sumOf { it.amount }
                val firstTx = txs.first()
                CategoryRanking(
                    categoryName = name,
                    amount = sum,
                    percentage = if (total > 0) (sum / total).toFloat() else 0f,
                    iconName = firstTx.categoryIcon
                )
            }
            .sortedByDescending { it.amount }
            .take(5)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val statsChartData = combine(statsTransactions, _statsType, _statsTimeRange, _statsDateRange) { transactions, type, range, (start, end) ->
        val filtered = transactions.filter { it.type == type }
        generateChartData(filtered, range, start, end)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val statsPieChartData = combine(statsTransactions, _statsType) { transactions, type ->
        val filtered = transactions.filter { it.type == type }
        val total = filtered.sumOf { it.amount }
        filtered.groupBy { it.categoryName }
            .map { (name, txs) ->
                val sum = txs.sumOf { it.amount }
                PieChartData(
                    name = name,
                    value = sum,
                    percentage = if (total > 0) (sum / total).toFloat() else 0f,
                    color = Color.Gray // Placeholder
                )
            }
            .sortedByDescending { it.value }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setStatsType(type: TransactionType) {
        _statsType.value = type
    }

    fun setStatsTimeRange(range: TimeRange) {
        _statsTimeRange.value = range
    }
    
    fun setStatsAnchorDate(calendar: Calendar) {
        _statsAnchorDate.value = calendar
    }

    fun setCustomDateRange(start: Long, end: Long) {
        val startCal = Calendar.getInstance().apply { timeInMillis = start }
        val endCal = Calendar.getInstance().apply { timeInMillis = end }
        _customStartDate.value = startCal
        _customEndDate.value = endCal
    }

    private fun calculateDateRange(range: TimeRange, anchor: Calendar, customStart: Calendar, customEnd: Calendar): Pair<Long, Long> {
        val start = anchor.clone() as Calendar
        val end = anchor.clone() as Calendar
        
        when (range) {
            TimeRange.WEEK -> {
                start.firstDayOfWeek = Calendar.MONDAY
                start.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                start.set(Calendar.HOUR_OF_DAY, 0)
                start.set(Calendar.MINUTE, 0)
                start.set(Calendar.SECOND, 0)
                start.set(Calendar.MILLISECOND, 0)

                end.firstDayOfWeek = Calendar.MONDAY
                end.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                end.set(Calendar.HOUR_OF_DAY, 23)
                end.set(Calendar.MINUTE, 59)
                end.set(Calendar.SECOND, 59)
                end.set(Calendar.MILLISECOND, 999)
            }
            TimeRange.MONTH -> {
                start.set(Calendar.DAY_OF_MONTH, 1)
                start.set(Calendar.HOUR_OF_DAY, 0)
                start.set(Calendar.MINUTE, 0)
                start.set(Calendar.SECOND, 0)
                start.set(Calendar.MILLISECOND, 0)

                end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH))
                end.set(Calendar.HOUR_OF_DAY, 23)
                end.set(Calendar.MINUTE, 59)
                end.set(Calendar.SECOND, 59)
                end.set(Calendar.MILLISECOND, 999)
            }
            TimeRange.YEAR -> {
                start.set(Calendar.DAY_OF_YEAR, 1)
                start.set(Calendar.HOUR_OF_DAY, 0)
                start.set(Calendar.MINUTE, 0)
                start.set(Calendar.SECOND, 0)
                start.set(Calendar.MILLISECOND, 0)

                end.set(Calendar.DAY_OF_YEAR, end.getActualMaximum(Calendar.DAY_OF_YEAR))
                end.set(Calendar.HOUR_OF_DAY, 23)
                end.set(Calendar.MINUTE, 59)
                end.set(Calendar.SECOND, 59)
                end.set(Calendar.MILLISECOND, 999)
            }
            TimeRange.CUSTOM -> {
                start.timeInMillis = customStart.timeInMillis
                start.set(Calendar.HOUR_OF_DAY, 0)
                start.set(Calendar.MINUTE, 0)
                start.set(Calendar.SECOND, 0)
                start.set(Calendar.MILLISECOND, 0)

                end.timeInMillis = customEnd.timeInMillis
                end.set(Calendar.HOUR_OF_DAY, 23)
                end.set(Calendar.MINUTE, 59)
                end.set(Calendar.SECOND, 59)
                end.set(Calendar.MILLISECOND, 999)
            }
        }
        return Pair(start.timeInMillis, end.timeInMillis)
    }

    private fun generateChartData(transactions: List<Transaction>, range: TimeRange, start: Long, end: Long): List<Double> {
        val points = mutableListOf<Double>()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = start
        
        when (range) {
            TimeRange.WEEK -> {
                for (i in 0 until 7) {
                    val currentDayStart = calendar.timeInMillis
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                    val currentDayEnd = calendar.timeInMillis
                    val sum = transactions.filter { it.date >= currentDayStart && it.date < currentDayEnd }.sumOf { it.amount }
                    points.add(sum)
                }
            }
            TimeRange.MONTH -> {
                val days = ((end - start) / (1000 * 60 * 60 * 24)).toInt() + 1
                for (i in 0 until days) {
                    val currentDayStart = calendar.timeInMillis
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                    val currentDayEnd = calendar.timeInMillis
                    val sum = transactions.filter { it.date >= currentDayStart && it.date < currentDayEnd }.sumOf { it.amount }
                    points.add(sum)
                }
            }
            TimeRange.YEAR -> {
                for (i in 0 until 12) {
                    val currentMonthStart = calendar.timeInMillis
                    calendar.add(Calendar.MONTH, 1)
                    val currentMonthEnd = calendar.timeInMillis
                    val sum = transactions.filter { it.date >= currentMonthStart && it.date < currentMonthEnd }.sumOf { it.amount }
                    points.add(sum)
                }
            }
            TimeRange.CUSTOM -> {
                val days = ((end - start) / (1000 * 60 * 60 * 24)).toInt() + 1
                if (days <= 31) {
                    for (i in 0 until days) {
                        val currentDayStart = calendar.timeInMillis
                        calendar.add(Calendar.DAY_OF_MONTH, 1)
                        val currentDayEnd = calendar.timeInMillis
                        val sum = transactions.filter { it.date >= currentDayStart && it.date < currentDayEnd }.sumOf { it.amount }
                        points.add(sum)
                    }
                } else {
                    // Just one point for total if range is too large for daily points
                    points.add(transactions.sumOf { it.amount })
                }
            }
        }
        return points
    }
    // 在 MainViewModel 中添加
    fun navigateToWeek(offset: Int) {
        val newAnchor = Calendar.getInstance().apply {
            // 👇 改为从当前日期开始计算，而不是从 statsAnchorDate
            add(Calendar.WEEK_OF_YEAR, offset)
        }
        _statsAnchorDate.value = newAnchor
    }

    fun navigateToMonth(offset: Int) {
        val newAnchor = Calendar.getInstance().apply {
            // 👇 改为从当前日期开始计算
            add(Calendar.MONTH, offset)
        }
        _statsAnchorDate.value = newAnchor
    }

    fun navigateToYear(offset: Int) {
        val newAnchor = Calendar.getInstance().apply {
            // 👇 改为从当前日期开始计算
            add(Calendar.YEAR, offset)
        }
        _statsAnchorDate.value = newAnchor
    }

}

enum class TimeRange {
    WEEK, MONTH, YEAR, CUSTOM
}

enum class DateFilterType {
    MONTH, WEEK
}

data class PieChartData(
    val name: String,
    val value: Double,
    val percentage: Float,
    val color: androidx.compose.ui.graphics.Color
)

class MainViewModelFactory(private val repository: AccountingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


