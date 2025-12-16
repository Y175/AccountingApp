package com.example.accountingapp.data

import kotlinx.coroutines.flow.Flow

class AccountingRepository(private val transactionDao: TransactionDao) {

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    fun getTransactionsByDateRange(start: Long, end: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(start, end)
    }

    suspend fun insert(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }
    
    suspend fun delete(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    fun getIncomeSum(start: Long, end: Long): Flow<Double?> {
        return transactionDao.getIncomeSum(start, end)
    }

    fun getExpenseSum(start: Long, end: Long): Flow<Double?> {
        return transactionDao.getExpenseSum(start, end)
    }

    fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return transactionDao.getCategoriesByType(type)
    }
}
