package com.example.spendwise

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.AppDatabase
import com.example.spendwise.ui.theme.SpendWiseTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpendWiseTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val expenseDao = remember { AppDatabase.getDatabase(context).expenseDao() }

    var totalIncome by remember { mutableStateOf(0.0) }
    var totalExpenses by remember { mutableStateOf(0.0) }
    val scope = rememberCoroutineScope()

    // Observe total income and expenses using LaunchedEffect + Flow
    LaunchedEffect(Unit) {


        scope.launch {
            expenseDao.getTotalIncome().collectLatest { income ->
                totalIncome = income ?: 0.0
            }
        }

        scope.launch {
            expenseDao.getTotalExpenses().collectLatest { expense ->
                totalExpenses = expense ?: 0.0
            }
        }
    }

    val balance = totalIncome - totalExpenses

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1565C0), Color(0xFFFFC107))
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SpendWise Dashboard",
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            DashboardCard("Total Income", totalIncome)
            DashboardCard("Total Expenses", totalExpenses)
            DashboardCard("Balance", balance)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    context.startActivity(android.content.Intent(context, AddExpenseActivity::class.java))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Expense")
            }

            Button(
                onClick = {
                    context.startActivity(Intent(context, AddIncomeActivity::class.java))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Income")
            }

            Spacer(modifier = Modifier.height(16.dp))



            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    context.startActivity(android.content.Intent(context, ViewAllExpensesActivity::class.java))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View All Expenses")
            }


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    context.startActivity(Intent(context, StatisticsActivity::class.java))
                },

                        modifier = Modifier.fillMaxWidth()
            ) {
                Text("Statistics & Insights")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    context.startActivity(Intent(context, SettingsActivity::class.java))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
            ) {
                Text("Settings", color = Color.White)
            }

        }
    }
}

@Composable
fun DashboardCard(title: String, value: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Text(
                text = "₹ %.2f".format(value),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
