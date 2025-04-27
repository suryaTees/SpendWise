package com.example.spendwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.AppDatabase
import com.example.spendwise.data.Expense
import com.example.spendwise.ui.theme.SpendWiseTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ViewAllExpensesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpendWiseTheme {
                ViewAllExpensesScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllExpensesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val expenseDao = remember { AppDatabase.getDatabase(context).expenseDao() }
    val scope = rememberCoroutineScope()
    var expenses by remember { mutableStateOf(emptyList<Expense>()) }

    // Collect expenses from Room
    LaunchedEffect(Unit) {
        scope.launch {
            expenseDao.getAllExpenses().collectLatest {
                expenses = it
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Transactions") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF1565C0), Color(0xFFFFC107))))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn {
                items(expenses) { expense ->
                    ExpenseItem(expense)
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (expense.type == "Income") Color(0xFFDFFFD6) else Color(0xFFFFE5E5)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = expense.title, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(text = "₹%.2f".format(expense.amount))
            Text(text = expense.type, color = if (expense.type == "Income") Color(0xFF2E7D32) else Color(0xFFC62828))
            Text(text = expense.date, fontSize = 12.sp, color = Color.Gray)
            if (!expense.note.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Note: ${expense.note}", fontSize = 12.sp)
            }
        }
    }
}
