package com.example.accountingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val iconName: String, // Resource name or identifier for the icon
    val type: TransactionType, // INCOME or EXPENSE
    val isCustom: Boolean = false,
    val parentId: Int? = null // For sub-categories
)

enum class TransactionType {
    INCOME, EXPENSE
}
