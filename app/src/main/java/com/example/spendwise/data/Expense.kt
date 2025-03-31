package com.example.spendwise.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: String, // "Income" or "Expense"
    val date: String, // You can convert to Date later
    val note: String? = null
)
