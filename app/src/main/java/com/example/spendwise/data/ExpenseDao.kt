package com.example.spendwise.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE type = 'Income'")
    fun getTotalIncome(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE type = 'Expense'")
    fun getTotalExpenses(): Flow<Double?>
}
