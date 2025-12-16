package com.example.accountingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val categoryId: Int,
    val categoryName: String, // Denormalized for easier display
    val categoryIcon: String,
    val type: TransactionType,
    val note: String,
    val date: Long, // Timestamp
    val bookId: Int = 1 // Default book
)
