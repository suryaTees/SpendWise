package com.example.spendwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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



class StatisticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpendWiseTheme {
                StatisticsScreen()
            }
        }
    }
}

@Composable
fun StatisticsScreen() {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).expenseDao() }
    val scope = rememberCoroutineScope()

    var income by remember { mutableStateOf(0.0) }
    var expenses by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        scope.launch {
            dao.getTotalIncome().collectLatest { income = it ?: 0.0 }
        }
        scope.launch {
            dao.getTotalExpenses().collectLatest { expenses = it ?: 0.0 }
        }
    }

    val total = income + expenses
    val balance = income - expenses

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1565C0), Color(0xFFFFC107))))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Statistics & Insights",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (total > 0) {
                SimplePieChart(income = income, expense = expenses)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ChartLegend(color = Color(0xFF66BB6A), label = "Income")
                    ChartLegend(color = Color(0xFFEF5350), label = "Expense")
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No data to visualize", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Summary", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Total Income: ₹%.2f".format(income), color = Color(0xFF2E7D32))
                    Text("Total Expenses: ₹%.2f".format(expenses), color = Color(0xFFD32F2F))
                    Text(
                        "Net Balance: ₹%.2f".format(balance),
                        color = if (balance >= 0) Color(0xFF1B5E20) else Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ChartLegend(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = MaterialTheme.shapes.small)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, color = Color.White, fontSize = 14.sp)
    }
}



@Composable
fun SimplePieChart(income: Double, expense: Double) {
    val total = income + expense
    val incomeAngle = if (total > 0) (income / total) * 360 else 0.0
    val expenseAngle = 360 - incomeAngle

    Canvas(modifier = Modifier.size(200.dp)) {
        drawArc(
            color = Color(0xFF66BB6A), // Green for income
            startAngle = 0f,
            sweepAngle = incomeAngle.toFloat(),
            useCenter = true
        )
        drawArc(
            color = Color(0xFFEF5350), // Red for expense
            startAngle = incomeAngle.toFloat(),
            sweepAngle = expenseAngle.toFloat(),
            useCenter = true
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(" Income", color = Color.White)
        Text(" Expense", color = Color.White)
    }
}
